package com.hd.microauservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Delete;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.hd.common.RetResult;
import com.hd.common.utils.HttpUtil;
import com.hd.common.vo.SyUserVo;
import com.hd.microauservice.entity.SyUserEntity;
import com.hd.microauservice.mapper.SyUserMapper;
import com.hd.microauservice.service.SyUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.microauservice.utils.RedisLockUtil;
import com.hd.microauservice.utils.VoConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
@Service
public class SyUserServiceImpl extends ServiceImpl<SyUserMapper, SyUserEntity> implements SyUserService {


    @Value("${config.user-center-uri}")
    String  userCenterUri;
    @Value("${config.USER_OP_IDENTIFICATION}")
    String  USER_OP_IDENTIFICATION;

    @Autowired
    RedisLockUtil redisLockUtil;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor ={Exception.class},isolation = Isolation.DEFAULT)
    public synchronized  void createUser(SyUserVo syUserVo) throws Exception {
        long timeout = 15;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        // UUID 作为 value
        String lockValue= UUID.randomUUID().toString();
        if (!redisLockUtil.lock("createUser", lockValue, timeout, timeUnit)){
            throw new Exception("系统繁忙!");
        }
        try{
            String account=syUserVo.getAccount();
            if(account==null||account.isEmpty()){
                throw new Exception("用户不能为空!");
            }
            //检查用户是否存在
            QueryWrapper queryWrapper=new QueryWrapper(){{
                eq("account",syUserVo.getAccount());
                eq("enterprise_id",syUserVo.getEnterpriseId());
                eq("delete_flag",0);
            }};
            if (getOne(queryWrapper)!=null){
                throw new Exception("用户已存在!");
            }

            SyUserEntity syUserEntity=new SyUserEntity();
            VoConvertUtils.convertObject(syUserVo,syUserEntity);
            save(syUserEntity);
            //检查用户中心是否有该用户
            if(!userExistInCenter(syUserVo)){
                if(!createUserForCenter(syUserVo)){
                    throw  new Exception("创建用户失败!");
                }
            }
            else {
                throw new Exception("认证中心用户已存在!");
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally {
            if (!redisLockUtil.unlock("createUser", lockValue)) {
                log.error("redis分布式锁解锁异常 key 为"+"createUser");
            }
        }
    }

    @Override
    public void removeUser(SyUserVo syUserVo) throws Exception {
        String account=syUserVo.getAccount();
        if(account==null||account.isEmpty()){
            throw new Exception("用户不能为空!");
        }
        //检查用户是否存在
        QueryWrapper queryWrapper=new QueryWrapper(){{
            eq("account",syUserVo.getAccount());
            eq("enterprise_id",syUserVo.getEnterpriseId());
            eq("delete_flag",0);
        }};

        if (getOne(queryWrapper)==null){
            throw new Exception("用户不存在!");
        }
        remove(queryWrapper);

        if(!removeUserForCenter(syUserVo)){
            throw  new Exception("删除用户失败.");
        }
    }

    @Override
    @Cacheable(value = "account",key="'h-'+#account")
    public SyUserEntity getOneFromCach(String account) {
        QueryWrapper queryWrapper=new QueryWrapper(){{
            eq("account",account);
            eq("delete_flag",0);
        }};
        SyUserEntity syUserEntity = getOne(queryWrapper);
        return  syUserEntity;
    }

    private boolean removeUserForCenter(SyUserVo syUserVo) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("enterprise", syUserVo.getEnterpriseId() );
        RetResult retResult = HttpUtil.httpDelWithIdenHeader(userCenterUri + "/account/" + syUserVo.getAccount(), params,USER_OP_IDENTIFICATION);
        return  retResult.getCode()== HttpStatus.OK.value();
    }

    private Boolean createUserForCenter(SyUserVo syUserVo) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("enterprise", syUserVo.getEnterpriseId() );
        params.add("password",syUserVo.getPasswordMd5() );
        RetResult retResult = HttpUtil.httpPostWithIdenHeader(userCenterUri + "/account/" + syUserVo.getAccount(), params,USER_OP_IDENTIFICATION);
        return  retResult.getCode()== HttpStatus.OK.value();
    }
    private Boolean userExistInCenter(SyUserVo syUserVo) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("enterprise", syUserVo.getEnterpriseId() );
        RetResult retResult = HttpUtil.httpGetWithIdenHeader(userCenterUri + "/account/" + syUserVo.getAccount(), params,USER_OP_IDENTIFICATION);
        return  retResult.getCode()== HttpStatus.OK.value();
    }
}
