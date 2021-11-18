package com.hd.microsysservice.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResult;
import com.hd.common.model.DataPrivilege;
import com.hd.common.model.KeyValuePair;
import com.hd.common.model.QueryExpression;
import com.hd.common.model.TokenInfo;
import com.hd.common.utils.ParseQueryUtil;
import com.hd.common.vo.SyDictItemVo;
import com.hd.common.vo.SyRoleVo;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.conf.GeneralConfig;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.*;
import com.hd.microsysservice.mapper.SyRoleMapper;
import com.hd.microsysservice.mapper.SyUserMapper;
import com.hd.microsysservice.service.*;
import com.hd.microsysservice.utils.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
@Service
@Slf4j
public class SyUserServiceImpl extends ServiceImpl<SyUserMapper, SyUserEntity> implements SyUserService {

    @Value("${config.USER_OP_IDENTIFICATION}")
    String USER_OP_IDENTIFICATION;

    @Autowired
    RedisLockUtil redisLockUtil;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SyRoleMapper syRoleMapper;

    @Autowired
    SyEnterpriseService syEnterpriseService;

    @Autowired
    SyUserRoleService syUserRoleService;

    @Autowired
    private org.springframework.data.redis.core.RedisTemplate redisTemplate;

    @Autowired
    SyUserService syUserService;

    @Autowired
    SyDictItemService syDictItemService;

    @Autowired
    ExecuteFeignService executeFeignService;

    @Autowired
    SyOrgService syOrgService;

    @Autowired
    LicenseCheckUtil licenseCheckUtil;

    @GlobalTransactional(rollbackFor = Exception.class)
//    @Override
//    public void createUser(SyUserVo syUserVo) throws Exception {
//      RetResult retResponse1 = userCenterFeignService.Add("test","root","2323ewwe");
//        String xid = RootContext.getXID();
//        RetResult retResponse2 = userCenterFeignService.Add("test","root","2323ewwe");
//        if(true) throw new Exception("3");
//    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    /**
     * 尽量不要使用synchronized，防止大并发是阻塞线程
     */
    //    public synchronized  void createUser(SyUserVo syUserVo) throws Exception {
    public void createUser(SyUserVo syUserVo) throws Exception {
        //root企业不允许新加用户
        Assert.isTrue(syUserVo.getEnterpriseId().compareTo(GeneralConfig.ROOT_ENTERPRISE_ID)!=0,String.format("root企业不允许新增用户!"));
        if(SecurityContext.GetCurTokenInfo().getEnterpriseId().compareTo(GeneralConfig.ROOT_ENTERPRISE_ID)!=0){
            licenseCheckUtil.JudgeLicenseForCreateUser(syUserVo.getEnterpriseId());
        }

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
            VoConvertUtils.copyObjectProperties(syUserVo, syUserEntity);
            syUserEntity.setCreateTime(new Date());
            syUserEntity.setModifiedTime(syUserEntity.getCreateTime());

            save(syUserEntity);
            if (syUserVo.getSyRoleVos() != null) {
                //syUserEntity = getOne(queryWrapper);
                updateRoles(syUserEntity.getId(), syUserVo.getSyRoleVos());
            }
            //检查用户中心是否有该用户
            //两个请求最长20s
            Long centerUserId = userExistInCenter(syUserVo);
            if (centerUserId == -1) {
                centerUserId = createUserForCenter(syUserVo);
                if (centerUserId == -1) {
                    throw new Exception("创建用户失败!");
                }
            } else {
                //throw new Exception("认证中心用户已存在!");
            }

            //更新用户对应的认证中心用户id
            SyUserEntity syUserEntity2 = new SyUserEntity();
            syUserEntity2.setId(syUserEntity.getId());
            syUserEntity2.setIdCenter(centerUserId);
            //if(true) throw new Exception("创建用户失败!");
            try {
                updateById(syUserEntity2);
            } catch (Exception ex) {
                //removeUserForCenter(centerUserId);
                //不用显示移除，使用seata at模式回滚
                throw new Exception("创建用户失败!");
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
        redisTemplate.delete(String.format("%s::%s", "userMenu", userId));
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void removeUser(Long userId) throws Exception {
        SyUserEntity syUserEntity = getById(userId);
        if (syUserEntity == null) {
            throw new Exception("用户不存在!");
        }
        VerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());
        Assert.isTrue(!(syUserEntity.getEnterpriseId().compareTo("root") == 0 && syUserEntity.getAccount().compareTo("root") == 0), "超级用户不能删除!");
        Assert.isTrue(userId!=Long.parseLong(SecurityContext.GetCurTokenInfo().getId()), String.format("不能删除自己!"));

        //判断改用户是否有任务存在
        RetResult undone = executeFeignService.undone(syUserEntity.getId());
        JSONObject objUndone = (JSONObject)undone.getData();
        Integer patrolSheetCount = objUndone.getInteger("patrolSheet");
        Integer orderCount = objUndone.getInteger("orderCount");
        Assert.isTrue((orderCount+patrolSheetCount) <= 0,String.format("用户%s还有%s个任务未完成,无法删除!",syUserEntity.getName(),orderCount+patrolSheetCount));

        removeById(syUserEntity.getId());

        //删除用户角色
        updateRoles(syUserEntity.getId(), new ArrayList<>());

//        SyUserVo syUserVo = new SyUserVo() {{
//            setAccount(syUserEntity.getAccount());
//            setEnterpriseId(syUserEntity.getEnterpriseId());
//        }};
        //seata at 模式事务管理
        if (!removeUserForCenter(syUserEntity.getIdCenter())) {
            throw new Exception("删除用户失败.");
        }
        //if(true) throw new Exception("2222.");
        //redisTemplate.delete(String.format("%s::%s-%s", "account", syUserEntity.getEnterpriseId(), syUserEntity.getAccount()));
        //缓存清除
        //@Cacheable(value = "account", key = "'centerUserId:'+#centerUserId", unless = "#result == null")
        redisTemplate.delete(String.format("%s::%s", "centerId2userId", syUserEntity.getIdCenter()));
    }

    @Override
    //@Cacheable(value = "account", key = "#enterpriseId+'-'+#account", unless = "#result == null")
    public SyUserEntity getUserByAccount(String account, String enterpriseId) {
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
        VerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());
        Assert.isTrue(syUserVo.getEnterpriseId().compareTo(syUserEntity.getEnterpriseId()) == 0, "企业编码异常!");
        Assert.isTrue(syUserVo.getAccount() == null || syUserVo.getAccount().compareTo(syUserEntity.getAccount()) == 0, "账号不能修改!");
        Assert.isTrue(
                (syUserEntity.getName().compareTo("admin") != 0)
                        || (
                        syUserEntity.getName().compareTo("admin") == 0 && SecurityContext.GetCurTokenInfo().getEnterpriseId().compareTo("root") == 0
                ),
                String.format("%s特殊账号不可删除!", "admin"));

        SyUserEntity syUserEntityNew = new SyUserEntity();
        VoConvertUtils.copyObjectProperties(syUserVo, syUserEntityNew);
        syUserEntityNew.setModifiedTime(new Date());
        updateById(syUserEntityNew);
        //更新role
        if (syUserVo.getSyRoleVos() != null) {
            updateRoles(syUserVo.getId(), syUserVo.getSyRoleVos());
        }
        //删除用户菜单缓存
        redisTemplate.delete(String.format("%s::%s","userMenu",syUserEntityNew.getId()));
        //删除用户centerId2userId 缓存，账户类型可能修改
        redisTemplate.delete(String.format("%s::%s","centerId2userId",syUserEntity.getIdCenter()));

//        redisTemplate.opsForValue().set(
//                String.format("%s::%s-%s", "account", syUserEntityNew.getEnterpriseId(), syUserEntityNew.getAccount())
//                , syUserEntityNew
//                , Duration.ofMinutes(20)
//        );
    }

    @Override
    public SyUserVo getUser(String userId) throws Exception {
        SyUserEntity syUserEntity = getById(userId);
        if (syUserEntity == null) {
            throw new Exception(String.format("用户Id:%s不存在!", userId));
        }
        VerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());

        SyUserVo syUserVo = new SyUserVo();
        VoConvertUtils.copyObjectProperties(syUserEntity, syUserVo);
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
            ne("account","admin");
        }};
        List<SyUserEntity> syUserEntities = list(queryWrapper);
        List<SyUserVo> syUserVos = new ArrayList<>();
        for (SyUserEntity syUserEntity : syUserEntities) {
            SyUserVo syUserVo = new SyUserVo();
            VoConvertUtils.copyObjectProperties(syUserEntity, syUserVo);
            syUserVos.add(syUserVo);
        }
        return syUserVos;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.DEFAULT)
    public void changepwd(SyUserVo syUserVo) throws Exception {
        SyUserEntity syUserEntity = getById(syUserVo.getId());
        if (syUserEntity == null) {
            throw new Exception("用户不存在!");
        }
        VerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());
        //Assert.isTrue(syUserVo.getEnterpriseId().compareTo(syUserEntity.getEnterpriseId()) == 0, "企业编码异常!");
        //修改用户中心
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//        params.add("enterprise", syUserVo.getEnterpriseId());
//        params.add("account", syUserVo.getAccount());
//        params.add("password", syUserVo.getPasswordMd5());
//        params.add("passwordOld", syUserVo.getPasswordMd5Old());
//        //params.add("account", syUserVo.getAccount());
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
//        String userOpIdentificationEncode = Base64.encode(cipherData);
//        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
//        RetResult retResult = HttpUtil.httpPutWithIdenHeader(userCenterUri + "/account", params, userOpIdentificationEncode);
        RetResult retResult = userCenterFeignService.changepwd(syUserEntity.getAccount(), syUserEntity.getEnterpriseId(), syUserVo.getPasswordMd5(), syUserVo.getPasswordMd5Old());
        //retResult =userCenterFeignService.changepwd(syUserVo.getAccount(),syUserVo.getEnterpriseId(),syUserVo.getPasswordMd5(),syUserVo.getPasswordMd5Old());
        if (retResult.getCode() != HttpStatus.OK.value()) {
            throw new Exception(String.format("修改密码失败，%s",retResult.getMsg()));
        }
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void resetpwd(Long userId) throws Exception {
        SyUserEntity syUserEntity = getById(userId);
        if (syUserEntity == null) {
            throw new Exception("用户不存在!");
        }
        VerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());
        //Assert.isTrue(syUserVo.getEnterpriseId().compareTo(syUserEntity.getEnterpriseId()) == 0, "企业编码异常!");
        RetResult retResult = userCenterFeignService.resetpwd(syUserEntity.getIdCenter());
        if (retResult.getCode() != HttpStatus.OK.value()) {
            throw new Exception(retResult.getMsg());
        }
    }

    private boolean removeUserForCenter(Long centerUserId) throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
//        String userOpIdentificationEncode = Base64.encode(cipherData);
//        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
//        RetResult retResult = HttpUtil.httpDelWithIdenHeader(userCenterUri + "/account/" + centerUserId.toString(), params, userOpIdentificationEncode);
        RetResult retResult = userCenterFeignService.remove(centerUserId);
        return retResult.getCode() == HttpStatus.OK.value();
    }

    @Override
    public boolean removeAllUserForCenter(String enterpriseId) throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//        params.add("enterprise", enterpriseId);
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
//        String userOpIdentificationEncode = Base64.encode(cipherData);
//        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
//        RetResult retResult = HttpUtil.httpDelWithIdenHeader(userCenterUri + "/account/all", params, userOpIdentificationEncode);
//        return retResult.getCode() == HttpStatus.OK.value();
        return true;
    }

    @Override
    public MyPage<SyUserVo> userbyrole(PageQueryExpressionList pageQueryExpressionList) {
        //提取role
        List<String> roles = new ArrayList<>();
        Long orgId = null;
        for (QueryExpression condition : pageQueryExpressionList.getQueryData()) {
            if (condition.getColumn().compareTo("role") == 0) {
                roles.add(condition.getValue());
            }
            if (condition.getColumn().compareTo("orgId") == 0) {
                orgId = Long.parseLong(condition.getValue());
            }
        }
        String ordrby = "";
        //提取order
        for (KeyValuePair keyValuePair : pageQueryExpressionList.getOrderby()) {
            if (ordrby.compareTo("") != 0) {
                ordrby += ",";
            }
            ordrby += String.format("%s %s", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, keyValuePair.getKey()), keyValuePair.getValue());
        }
        if (ordrby.compareTo("") != 0) {
            ordrby = "order by " + ordrby;
        }
        //自定义分页查询
        Page page = new Page(pageQueryExpressionList.getPageNum(), pageQueryExpressionList.getPageSize(), true);
        List<SyUserVo> syUserVos=new ArrayList<>();
        if(roles.size()>0){
            syUserVos = baseMapper.userbyrole(SecurityContext.GetCurTokenInfo().getEnterpriseId(), roles
                    , orgId
                    //,Long.parseLong(SecurityContext.GetCurTokenInfo().getOrgId())
                    , ordrby, page);
        }else {
            page.setTotal(0);
        }

        return new MyPage<>(page.getCurrent(), page.getSize(), page.getTotal(), syUserVos);
    }

    @Override
    public SyUserVo getCurrentUser() throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        if (tokenInfo == null) {
            throw new Exception("没有当前用户!");
        }

        QueryWrapper queryWrapper = new QueryWrapper() {{
//            eq("account", tokenInfo.getAccount());
//            eq("enterprise_id", tokenInfo.getEnterpriseId());
//            eq("delete_flag", 0);
            eq("id", tokenInfo.getId());
        }};
        SyUserEntity syUserEntity = syUserService.getOne(queryWrapper);
        //SyUserEntity syUserEntity = syUserService.getUserByAccount(tokenInfo.getAccount(), tokenInfo.getEnterpriseId());
        SyUserVo syUserVo = new SyUserVo();
        VoConvertUtils.copyObjectProperties(syUserEntity, syUserVo);
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
        //判断是否是管理员
        AtomicReference<Short> userFlag = new AtomicReference<>((short) 1);
        MyPage<SyDictItemVo> syDictItemVoMyPage = syDictItemService.dictItembycode("admin_role", tokenInfo.getEnterpriseId(), "order by code asc", 1, 1000);
        syDictItemVoMyPage.getRecords().forEach(item -> {
            String roleName = item.getCode();
            syRoleVos.forEach(r -> {
                if (r.getName().compareTo(roleName) == 0) {
                    userFlag.set((short) 0);
                }
            });
        });
        syUserVo.setUserFlag(userFlag.get());
        return syUserVo;
    }

    @Override
    public void enableUser(String userId, Boolean enabled) {
        Assert.notNull(userId, String.format("参数%s不能为空!", "userId"));
        SyUserEntity syUserEntity = syUserService.getById(Long.parseLong(userId));
        Assert.notNull(syUserEntity, String.format("用户%s不存在!", userId));
        VerifyUtil.verifyEnterId(syUserEntity.getEnterpriseId());
        Assert.isTrue(userId.compareTo(SecurityContext.GetCurTokenInfo().getId())!=0, String.format("不能禁用自己!"));
        if(enabled==false){
            //判断改用户是否有任务存在
            RetResult undone = executeFeignService.undone(syUserEntity.getId());
            JSONObject objUndone = (JSONObject)undone.getData();
            Integer patrolSheetCount = objUndone.getInteger("patrolSheet");
            Integer orderCount = objUndone.getInteger("orderCount");
            Assert.isTrue((orderCount+patrolSheetCount) <= 0,String.format("用户%s还有%s个任务未完成,无法禁用!",syUserEntity.getName(),orderCount+patrolSheetCount));
        }
        UpdateWrapper updateWrapper = new UpdateWrapper() {{
            eq("id", userId);
        }};
        updateWrapper.set("enabled", enabled ? 1 : 0);
        update(updateWrapper);

        redisTemplate.delete(String.format("%s::%s", "centerId2userId", syUserEntity.getIdCenter()));
    }

    @Override
    public MyPage getOrgUserList(PageQueryExpressionList pageQuery,Boolean withAccess) throws Exception {
        QueryExpression queryExpression = pageQuery.getQueryExpressionByColumn("orgId");
        Assert.isTrue(queryExpression != null, "查询参数错误!");
        Long orgId = Long.parseLong(queryExpression.getValue());
        if(withAccess){
            //判断该用户是否有对应的部门权限，根据数据权限设置
            List<SyOrgEntity> syOrgEntities = syOrgService.getMyOrgList();
            SyOrgEntity syOrgEntityFind = null;
            for (SyOrgEntity item : syOrgEntities) {
                if (item.getId().equals(Long.valueOf(orgId))) {
                    syOrgEntityFind = item;
                }
            }
            if (syOrgEntityFind == null) {
                throw new Exception("没有权限!");
            }
        }
        queryExpression = new QueryExpression();
        queryExpression.setColumn("delete_flag");
        queryExpression.setValue("0");
        queryExpression.setType("eq");
        pageQuery.getQueryData().add(queryExpression);

        Page<SyUserEntity> syUserEntityPage = ParseQueryUtil.selectPage(pageQuery, syUserService);
        List<SyUserVo> listVo = new ArrayList<>();
        for (SyUserEntity syUserEntity : syUserEntityPage.getRecords()) {
            SyUserVo syUserVo = new SyUserVo();
            VoConvertUtils.copyObjectProperties(syUserEntity, syUserVo);
            listVo.add(syUserVo);
        }
        //检索角色
        listVo.forEach(vo->{
            List<SyRoleEntity> syRoleEntities = syRoleMapper.getUserRole(vo.getId());
            List<SyRoleVo> syRoleVos = new ArrayList<>();
            for (SyRoleEntity syRoleEntity : syRoleEntities) {
                SyRoleVo syRoleVo = new SyRoleVo() {{
                    setId(syRoleEntity.getId());
                    setName(syRoleEntity.getName());
                }};
                syRoleVos.add(syRoleVo);
            }
            vo.setSyRoleVos(syRoleVos);
        });
        return new MyPage<>(syUserEntityPage.getCurrent(), syUserEntityPage.getSize(), syUserEntityPage.getTotal(), listVo);
    }

    @Autowired
    UserCenterFeignService userCenterFeignService;

    private Long createUserForCenter(SyUserVo syUserVo) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("enterprise", syUserVo.getEnterpriseId());
        params.add("password", syUserVo.getPasswordMd5());
        params.add("account", syUserVo.getAccount());

        RetResult retResult = userCenterFeignService.add(syUserVo.getAccount(), syUserVo.getEnterpriseId(), syUserVo.getPasswordMd5());

//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
//        String userOpIdentificationEncode = Base64.encode(cipherData);
//        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
//        RetResult retResult = HttpUtil.httpPostWithIdenHeader(userCenterUri + "/account" , params, userOpIdentificationEncode);
        if (retResult.getCode() == HttpStatus.OK.value()) {
            Long centerUserId = (Long) retResult.getData();
            return centerUserId;
        }
        return -1L;
    }

    private Long userExistInCenter(SyUserVo syUserVo) throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//        params.add("enterprise", syUserVo.getEnterpriseId());
//        params.add("account", syUserVo.getAccount());
//
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
//        String userOpIdentificationEncode = Base64.encode(cipherData);
//        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
//
//        RetResult retResult = HttpUtil.httpGetWithIdenHeader(userCenterUri + "/account", params, userOpIdentificationEncode);
        RetResult retResult = userCenterFeignService.userExist(syUserVo.getAccount(),syUserVo.getEnterpriseId());
        if (retResult.getCode() == HttpStatus.OK.value()) {
            return (Long) retResult.getData();
        }
        return -1L;
    }
}
