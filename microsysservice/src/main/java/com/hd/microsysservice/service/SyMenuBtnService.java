package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.microsysservice.entity.SyMenuBtnEntity;
import com.hd.microsysservice.utils.VoConvertUtils;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-12
 */
public interface SyMenuBtnService extends IService<SyMenuBtnEntity> {

    List<SyMenuBtnVo> getUserMenuBtns(Long userId, Long menuId);
    List<SyMenuBtnVo> getBtnsByMenuId(Long menuId,Boolean isAll);
    class SyMenuBtnVoConvertUtils  extends VoConvertUtils<SyMenuBtnEntity, SyMenuBtnVo> {
    }
}

