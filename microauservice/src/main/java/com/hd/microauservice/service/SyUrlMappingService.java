package com.hd.microauservice.service;

import com.hd.microauservice.entity.SyUrlMappingEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwei
 * @since 2021-07-08
 */
public interface SyUrlMappingService extends IService<SyUrlMappingEntity> {

    String getPermissionCode(String method, String uri);
    List<SyUrlMappingEntity> getUrlTemplateList();
}
