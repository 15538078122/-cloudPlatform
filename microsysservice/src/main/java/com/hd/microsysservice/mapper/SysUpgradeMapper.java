package com.hd.microsysservice.mapper;

import com.hd.microsysservice.entity.SysUpgradeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-09-22
 */
public interface SysUpgradeMapper extends BaseMapper<SysUpgradeEntity> {

    @Select("SELECT max(version) FROM sys_upgrade WHERE type=#{type} and enterprise_id=#{enterpriseId}")
    Integer getMaxVersionByType(Integer type,String enterpriseId);
}
