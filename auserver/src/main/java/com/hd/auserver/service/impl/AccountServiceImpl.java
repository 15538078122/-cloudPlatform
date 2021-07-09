package com.hd.auserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hd.auserver.entity.AccountEntity;
import com.hd.auserver.mapper.AccountMapper;
import com.hd.auserver.mapper.CustomMapper;
import com.hd.auserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hd.common.MyPage;

import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements AccountService {
    @Autowired
    CustomMapper customMapper;

    @Override
    public MyPage<AccountEntity> selectAccounts(int pageNum, int pageSize, QueryWrapper queryWrapper) {
        Page page1=new Page(2,2,false);
        List<AccountEntity> accountEntities = customMapper.selectUser(page1);
        Page<AccountEntity> page = new Page<>(pageNum, pageSize);
        Page<AccountEntity> accountEntityPage = this.baseMapper.selectPage(page, queryWrapper);
        return new MyPage<>(accountEntityPage.getCurrent(), accountEntityPage.getSize(), accountEntityPage.getTotal(), accountEntityPage.getRecords());
    }
}
