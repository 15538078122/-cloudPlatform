package com.hd.auserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hd.auserver.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
public interface CustomMapper extends BaseMapper {
        List<AccountEntity> selectUser(@Param("account") String account, IPage page);
}
