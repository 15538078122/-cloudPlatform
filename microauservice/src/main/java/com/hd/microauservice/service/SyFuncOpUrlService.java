package com.hd.microauservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.microauservice.entity.SyFuncOpUrlEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyFuncOpUrlService extends IService<SyFuncOpUrlEntity> {
    List<String> selectUserPerm(Long userId);
}
