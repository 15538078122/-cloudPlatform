package com.hd.microauservice.mapper;

import com.hd.microauservice.entity.SyUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyUserMapper extends BaseMapper<SyUserEntity> {
    @Select("select sleep(5)")
    Long sleep();
}
