package com.hd.microsysservice.service;

import com.hd.common.vo.SyEnterpriseVo;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.microsysservice.entity.SyMenuBtnEntity;
import com.hd.microsysservice.utils.VoConvertUtils;

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
    void recoverEnterprise(Long id) throws Exception;
    void deleteEnterprisePhysically(Long id);
    class SyEnterpriseVoConvertUtils  extends VoConvertUtils<SyEnterpriseEntity, SyEnterpriseVo> {
    }
}
