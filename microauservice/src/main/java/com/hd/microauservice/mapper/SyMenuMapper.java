package com.hd.microauservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hd.microauservice.entity.SyMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表 Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-09
 */
public interface SyMenuMapper extends BaseMapper<SyMenuEntity> {
    List<SyMenuEntity> selectUserMenu(@Param("userId") String userId,@Param("enterpriseId") String enterpriseId);
}
