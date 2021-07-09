package com.hd.auserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hd.auserver.entity.AccountEntity;

import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
public interface CustomMapper extends BaseMapper {
        List<AccountEntity> selectUser(IPage page);
}
