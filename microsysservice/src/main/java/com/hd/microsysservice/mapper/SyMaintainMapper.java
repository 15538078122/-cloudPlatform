package com.hd.microsysservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hd.microsysservice.entity.SyUserEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyMaintainMapper extends BaseMapper<SyUserEntity> {

    void deleteEnterprisePhysically(String enterpeiseId);
}
