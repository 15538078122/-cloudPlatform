package com.hd.microsysservice.service;

import com.hd.common.vo.SyFuncOperatorVo;
import com.hd.microsysservice.entity.SyFuncOperatorEntity;
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
public interface SyFuncOperatorService extends IService<SyFuncOperatorEntity> {

    List<SyFuncOperatorVo> getOprsByFuncId(Long id);
}
