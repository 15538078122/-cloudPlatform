package com.hd.microauservice.service;

import com.hd.microauservice.entity.SyMenuEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表 服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-09
 */
public interface SyMenuService extends IService<SyMenuEntity> {

}
