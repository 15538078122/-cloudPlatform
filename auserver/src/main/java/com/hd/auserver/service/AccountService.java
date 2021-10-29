package com.hd.auserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.auserver.entity.AccountEntity;
import com.hd.common.MyPage;

/**
 * @Author: liwei
 * @Description:
 */

public interface AccountService extends IService<AccountEntity> {
    MyPage<AccountEntity> selectAccounts(int pageNum, int pageSize, QueryWrapper queryWrapper);

    void changePwd(String account, String enterprise, String password, String passwordOld) throws Exception;

    void resetPwd(Long id) throws Exception;
}
