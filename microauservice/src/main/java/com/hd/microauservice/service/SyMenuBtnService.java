package com.hd.microauservice.service;

import com.hd.common.vo.SyMenuBtnVo;
import com.hd.microauservice.entity.SyMenuBtnEntity;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<SyMenuBtnVo> getBtnsByMenuId(Long menuId);

}
