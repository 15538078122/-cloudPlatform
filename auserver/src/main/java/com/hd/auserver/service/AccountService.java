package com.hd.auserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.auserver.entity.AccountEntity;
import com.hd.common.MyPage;
import org.springframework.stereotype.Service;

/**
 * @Author: liwei
 * @Description:
 */

public interface AccountService extends IService<AccountEntity> {
    MyPage<AccountEntity> selectAccounts(int pageNum, int pageSize, QueryWrapper queryWrapper);
}
