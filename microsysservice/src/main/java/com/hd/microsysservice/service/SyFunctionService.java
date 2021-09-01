package com.hd.microsysservice.service;

import com.hd.common.vo.SyFunctionVo;
import com.hd.microsysservice.entity.SyFunctionEntity;
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
public interface SyFunctionService extends IService<SyFunctionEntity> {

    List<SyFunctionVo> getFuncTree();
    void deleteFunc(Long funcId) throws Exception;

    void updateFunc(Long funcId, SyFunctionVo syFuncVo);
}
