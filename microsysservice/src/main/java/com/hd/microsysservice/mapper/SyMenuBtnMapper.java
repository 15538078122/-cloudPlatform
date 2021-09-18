package com.hd.microsysservice.mapper;

import com.hd.microsysservice.entity.SyMenuBtnEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-12
 */
public interface SyMenuBtnMapper extends BaseMapper<SyMenuBtnEntity> {
    List<SyMenuBtnEntity> getUserMenuBtns(@Param("userId") Long userId,@Param("menuId") Long menuId);

}
