package com.hd.auserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.auserver.config.MyBCryptPasswordEncoder;
import com.hd.auserver.entity.AccountEntity;
import com.hd.auserver.mapper.AccountMapper;
import com.hd.auserver.mapper.CustomMapper;
import com.hd.auserver.service.AccountService;
import com.hd.common.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements AccountService {
    @Autowired
    /**
     * 自定义分页测试
     */
    CustomMapper customMapper;

    @Override
    public MyPage<AccountEntity> selectAccounts(int pageNum, int pageSize, QueryWrapper queryWrapper) {
        Page page1=new Page(2,2,true);
        List<AccountEntity> accountEntities = customMapper.selectUser("li",page1);
        Page<AccountEntity> page = new Page<>(pageNum, pageSize);
        Page<AccountEntity> accountEntityPage = this.baseMapper.selectPage(page, queryWrapper);
        return new MyPage<>(accountEntityPage.getCurrent(), accountEntityPage.getSize(), accountEntityPage.getTotal(), accountEntityPage.getRecords());
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void changePwd(String account, String enterprise, String password, String passwordOld) throws Exception {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("account",account);
        qw.eq("enterprise",enterprise);
        AccountEntity accountEntity =  getOne(qw);
        if(accountEntity==null){
            throw  new Exception("账号不存在!");
        }
        if(!bCryptPasswordEncoder.matches(passwordOld,accountEntity.getPassword())){
            throw  new Exception("旧密码不匹配!");
        }
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("account",account);
        updateWrapper.eq("enterprise",enterprise);
        String pwd1 = password;
        String pwd2 = null;
        try {
            //TODO: 此处将前端加密的pwd转化为明文，临时测试
            pwd1=((MyBCryptPasswordEncoder)bCryptPasswordEncoder).RsaEncodePwd(pwd1);
            pwd2 = ((MyBCryptPasswordEncoder)bCryptPasswordEncoder).RsaDecodePwd(pwd1);
        } catch (Exception e) {
            throw  new Exception("密码数据异常!");
        }
        updateWrapper.set("password",bCryptPasswordEncoder.encode(pwd2));
        update(updateWrapper);
    }
}
