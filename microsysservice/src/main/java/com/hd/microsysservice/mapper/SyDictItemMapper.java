package com.hd.microsysservice.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.vo.SyDictItemVo;
import com.hd.microsysservice.entity.SyDictItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-30
 */
public interface SyDictItemMapper extends BaseMapper<SyDictItemEntity> {

    List<SyDictItemVo> dictItembycode(@Param("enterId") String enterId,@Param("code") String code,@Param("orderby") String ordrby, Page page);
}
