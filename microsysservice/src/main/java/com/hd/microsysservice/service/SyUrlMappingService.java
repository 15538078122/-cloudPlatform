package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyUrlMappingVo;
import com.hd.microsysservice.entity.SyUrlMappingEntity;
import com.hd.microsysservice.utils.VoConvertUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwei
 * @since 2021-07-08
 */
public interface SyUrlMappingService extends IService<SyUrlMappingEntity> {
    class SyUrlMappingVoConvertUtils  extends VoConvertUtils<SyUrlMappingEntity, SyUrlMappingVo> {
    }
}
