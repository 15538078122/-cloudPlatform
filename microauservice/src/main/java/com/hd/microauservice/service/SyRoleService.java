package com.hd.microauservice.service;

import com.hd.common.vo.SyRoleVo;
import com.hd.microauservice.entity.SyRoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyRoleService extends IService<SyRoleEntity> {
    void  createRole(SyRoleVo syRoleVo) throws Exception;

    void updateRole(SyRoleVo syRoleVo);

    void removeRoleId(Long roleId);

    SyRoleVo getRoleDetail(Long roleId);
}
