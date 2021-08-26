package com.hd.microauservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hd.microauservice.entity.SyFuncOpUrlEntity;
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
public interface SyFuncOpUrlMapper extends BaseMapper<SyFuncOpUrlEntity> {
    List<SyFuncOpUrlEntity> selectUserPerm(@Param("userId") Long userId);
}
