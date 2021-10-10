package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyUpgradeVo;
import com.hd.microsysservice.entity.SysUpgradeEntity;
import com.hd.microsysservice.utils.VoConvertUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-09-22
 */
public interface SysUpgradeService extends IService<SysUpgradeEntity> {
    void uploadAppVersion(SyUpgradeVo syUpgradeVo);

    class SyUpgradeVoConvertUtils  extends VoConvertUtils<SysUpgradeEntity, SyUpgradeVo> {
    }
    SyUpgradeVo getAppVersion();
}
