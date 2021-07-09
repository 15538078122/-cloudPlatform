package com.hd.microauservice.mapper;

import com.hd.microauservice.entity.SyMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表 Mapper 接口
 * </p>
 *
 * @author wli
 * @since 2021-07-09
 */
public interface SyMenuMapper extends BaseMapper<SyMenuEntity> {

}
