package com.hd.microauservice.service;

import com.hd.microauservice.entity.SyEnterpriseEntity;
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

    void createEnterprise(SyEnterpriseEntity syEnterpriseEntity);
}
