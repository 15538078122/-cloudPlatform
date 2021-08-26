package com.hd.microauservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hd.microauservice.entity.SyRoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyRoleMapper extends BaseMapper<SyRoleEntity> {
    List<SyRoleEntity> getUserRole(@Param("userId") Long userId);
}
