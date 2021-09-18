package com.hd.microsysservice.service;

import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyEnterpriseService extends IService<SyEnterpriseEntity> {

    void createEnterprise(SyEnterpriseEntity syEnterpriseEntity,Boolean createRoles) throws Exception;

    void removeEnterpriseById(Long id) throws Exception;

    void deleteEnterprisePhysically(Long id);
}
