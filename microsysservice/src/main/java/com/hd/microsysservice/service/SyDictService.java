package com.hd.microsysservice.service;

import com.hd.microsysservice.entity.SyDictEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-30
 */
public interface SyDictService extends IService<SyDictEntity> {

    void removeDict(Long id);
}
