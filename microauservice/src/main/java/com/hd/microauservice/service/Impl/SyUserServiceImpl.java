package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.common.RetResult;
import com.hd.common.model.DataPrivilege;
import com.hd.common.utils.HttpUtil;
import com.hd.common.utils.RSAEncrypt;
import com.hd.common.vo.SyRoleVo;
import com.hd.common.vo.SyUserVo;
import com.hd.microauservice.entity.SyRoleEntity;
import com.hd.microauservice.entity.SyUserEntity;
import com.hd.microauservice.entity.SyUserRoleEntity;
import com.hd.microauservice.mapper.SyRoleMapper;
import com.hd.microauservice.mapper.SyUserMapper;
import com.hd.microauservice.service.SyUserRoleService;
import com.hd.microauservice.service.SyUserService;
import com.hd.microauservice.utils.EnterpriseVerifyUtil;
import com.hd.microauservice.utils.JwtUtils;
import com.hd.microauservice.utils.RedisLockUtil;
import com.hd.microauservice.utils.VoConvertUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
@Service
public class SyUserServiceImpl extends ServiceImpl<SyUserMapper, SyUserEntity> implements SyUserService {


    @Value("${config.user-center-uri}")
    String userCenterUri;
    @Value("${config.USER_OP_IDENTIFICATION}")
    String USER_OP_IDENTIFICATION;

    @Autowired
    RedisLockUtil redisLockUtil;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SyRoleMapper syRoleMapper;

    @Autowired
    SyUserRoleService syUserRoleService;

    @Autowired
    private org.springframework.data.redis.core.RedisTemplate redisTemplate;

    @Autowired
    SyUserService syUserService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    /**
     * 尽量不要使用synchronized，防止大并发是阻塞线程
     */
    //public synchronized  void createUser(SyUserVo syUserVo) throws Exception {
    public void createUser(SyUserVo syUserVo) throws Exception {
        //注意timeout的设置，大于执行块可能需要的最大时间，否则锁失效造成异常
        long timeout = 30;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        // UUID 作为 value
        String lockValue = UUID.randomUUID().toString();
        //log.debug("获取分布式锁解锁 为"+lockValue);
        if (!redisLockUtil.lock("createUser", lockValue, timeout, timeUnit)) {
            throw new Exception("系统繁忙!");
        }
        //log.debug("已获取分布式锁解锁 为"+lockValue);
        try {
            //利用数据库select sleep 延时
            //baseMapper.sleep();
            String account = syUserVo.getAccount();
            if (account == null || account.isEmpty()) {
                throw new Exception("用户不能为空!");
            }
            //检查用户是否存在
            //两个sql执行，最长10s
            QueryWrapper queryWrapper = new QueryWrapper() {{
                eq("account", syUserVo.getAccount());
                eq("enterprise_id", syUserVo.getEnterpriseId());
                eq("delete_flag", 0);
            }};
            if (getOne(queryWrapper) != null) {
                throw new Exception("用户已存在!");
            }

            SyUserEntity syUserEntity = new SyUserEntity();
            VoConvertUtils.convertObject(syUserVo, syUserEntity);
            save(syUserEntity);
            if (syUserVo.getSyRoleVos() != null) {
                syUserEntity = getOne(queryWrapper);
                updateRoles(syUserEntity.getId(), syUserVo.getSyRoleVos());
            }
            //检查用户中心是否有该用户
            //两个请求最长20s
            if (!userExistInCenter(syUserVo)) {
                if (!createUserForCenter(syUserVo)) {
                    throw new Exception("创建用户失败!");
                }
            } else {
                throw new Exception("认证中心用户已存在!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (!redisLockUtil.unlock("createUser", lockValue)) {
                log.error("redis分布式锁解锁异常 key：" + "createUser-" + lockValue);
            }
            //log.debug("释放分布式锁解锁 为"+lockValue);
        }
    }

    private void updateRoles(Long userId, List<SyRoleVo> syRoleVos) {
        //删除旧的
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        syUserRoleService.remove(queryWrapper);
        //创建新的旧的
        List<SyUserRoleEntity> syUserRoleEntities = new ArrayList<>();
        for (SyRoleVo syRoleVo : syRoleVos) {
            SyUserRoleEntity syUserRoleEntity = new SyUserRoleEntity() {{
                setUserId(userId);
                setRoleId(syRoleVo.getId());
            }};
            syUserRoleEntities.add(syUserRoleEntity);
        }
        syUserRoleService.saveBatch(syUserRoleEntities);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void removeUser(Long userId) throws Exception {
        SyUserEntity syUserEntity = getById(userId);
        if (syUserEntity == null) {
            throw new Exception("用户不存在!");
        }
        EnterpriseVerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());
        Assert.isTrue(syUserEntity.getEnterpriseId().compareTo("root")!=0 && syUserEntity.getAccount().compareTo("root")!=0,"超级用户不能删除!");
        removeById(syUserEntity.getId());
        //删除用户角色
        updateRoles(syUserEntity.getId(), new ArrayList<>());

        SyUserVo syUserVo = new SyUserVo() {{
            setAccount(syUserEntity.getAccount());
            setEnterpriseId(syUserEntity.getEnterpriseId());
        }};
        if (!removeUserForCenter(syUserVo)) {
            throw new Exception("删除用户失败.");
        }
        redisTemplate.delete(String.format("%s::%s-%s", "account", syUserEntity.getEnterpriseId(), syUserEntity.getAccount()));
    }

    @Override
    @Cacheable(value = "account", key = "#enterpriseId+'-'+#account", unless = "#result == null")
    public SyUserEntity getOneFromCach(String account, String enterpriseId) {
        QueryWrapper queryWrapper = new QueryWrapper() {{
            eq("account", account);
            eq("delete_flag", 0);
            eq("enterprise_id", enterpriseId);
        }};
        SyUserEntity syUserEntity = getOne(queryWrapper);
        return syUserEntity;
    }

    @Override
    public DataPrivilege getUserDataPrivilege(Long userId) {
        Integer privilege = baseMapper.getUserDataPrivilege(userId);
        if (privilege == null) {
            privilege = DataPrivilege.LEVEL_ONLY.getValue();
        }
        return DataPrivilege.getByValue(privilege);
    }

    @Override
    public void updateUser(SyUserVo syUserVo) throws Exception {
        if (syUserVo.getId() == null) {
            throw new Exception("用户Id不能为空!");
        }
        SyUserEntity syUserEntity = getById(syUserVo.getId());
        if (syUserEntity == null) {
            throw new Exception("用户不存在!");
        }
        EnterpriseVerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());
        Assert.isTrue(syUserVo.getEnterpriseId().compareTo(syUserEntity.getEnterpriseId()) == 0, "企业编码异常!");

        SyUserEntity syUserEntityNew = new SyUserEntity();
        VoConvertUtils.convertObject(syUserVo, syUserEntityNew);
        updateById(syUserEntityNew);
        //更新role
        if (syUserVo.getSyRoleVos() != null) {
            updateRoles(syUserVo.getId(), syUserVo.getSyRoleVos());
        }

        redisTemplate.opsForValue().set(
                String.format("%s::%s-%s", "account", syUserEntityNew.getEnterpriseId(), syUserEntityNew.getAccount())
                , syUserEntityNew
                , Duration.ofMinutes(20)
        );
    }

    @Override
    public SyUserVo getUser(String userId) throws Exception {
        SyUserEntity syUserEntity = getById(userId);
        if (syUserEntity == null) {
            throw new Exception(String.format("用户Id:%s不存在!", userId));
        }
        EnterpriseVerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());

        SyUserVo syUserVo = new SyUserVo();
        VoConvertUtils.convertObject(syUserEntity, syUserVo);
        //检索角色
        List<SyRoleEntity> syRoleEntities = syRoleMapper.getUserRole(syUserVo.getId());
        List<SyRoleVo> syRoleVos = new ArrayList<>();
        for (SyRoleEntity syRoleEntity : syRoleEntities) {
            SyRoleVo syRoleVo = new SyRoleVo() {{
                setId(syRoleEntity.getId());
                setName(syRoleEntity.getName());
            }};
            syRoleVos.add(syRoleVo);
        }
        syUserVo.setSyRoleVos(syRoleVos);
        return syUserVo;
    }

    @Override
    public List<SyUserVo> getOrgUser(Long orgId) {
        QueryWrapper queryWrapper = new QueryWrapper() {{
            eq("delete_flag", 0);
            eq("org_id", orgId);
        }};
        List<SyUserEntity> syUserEntities = list(queryWrapper);
        List<SyUserVo> syUserVos = new ArrayList<>();
        for(SyUserEntity syUserEntity:syUserEntities){
            SyUserVo syUserVo=new SyUserVo();
            VoConvertUtils.convertObject(syUserEntity,syUserVo);
            syUserVos.add(syUserVo);
        }
        return syUserVos;
    }

    private boolean removeUserForCenter(SyUserVo syUserVo) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("enterprise", syUserVo.getEnterpriseId());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
        String userOpIdentificationEncode = Base64.encode(cipherData);
        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
        RetResult retResult = HttpUtil.httpDelWithIdenHeader(userCenterUri + "/account/" + syUserVo.getAccount(), params, userOpIdentificationEncode);
        return retResult.getCode() == HttpStatus.OK.value();
    }

    private Boolean createUserForCenter(SyUserVo syUserVo) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("enterprise", syUserVo.getEnterpriseId());
        params.add("password", syUserVo.getPasswordMd5());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
        String userOpIdentificationEncode = Base64.encode(cipherData);
        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
        RetResult retResult = HttpUtil.httpPostWithIdenHeader(userCenterUri + "/account/" + syUserVo.getAccount(), params, userOpIdentificationEncode);
        return retResult.getCode() == HttpStatus.OK.value();
    }

    private Boolean userExistInCenter(SyUserVo syUserVo) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("enterprise", syUserVo.getEnterpriseId());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
        String userOpIdentificationEncode = Base64.encode(cipherData);
        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");

        RetResult retResult = HttpUtil.httpGetWithIdenHeader(userCenterUri + "/account/" + syUserVo.getAccount(), params, userOpIdentificationEncode);
        return retResult.getCode() == HttpStatus.OK.value();
    }
}
