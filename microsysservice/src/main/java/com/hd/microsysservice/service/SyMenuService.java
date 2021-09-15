package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyMenuVo;
import com.hd.microsysservice.entity.SyMenuEntity;
import com.hd.microsysservice.utils.VoConvertUtils;

import java.util.List;

/**
 * <p>
 * 每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表 服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-09
 */
public interface SyMenuService extends IService<SyMenuEntity> {

    List<SyMenuVo> getCurrentUserMenu();
    List<SyMenuVo> getAllMenu(String enterpriseId);
    void createMenu(SyMenuEntity syMenuEntity);
    void deleteMenu(Long menuId);
    //void update(SyMenuEntity syMenuEntity);

    void updateMenu(Long menuId, SyMenuVo syMenuVo) throws Exception;
    class SyMenuVoConvertUtils  extends VoConvertUtils<SyMenuEntity, SyMenuVo> {
    }
}
