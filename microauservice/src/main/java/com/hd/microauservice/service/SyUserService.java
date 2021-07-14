package com.hd.microauservice.service;

import com.hd.common.vo.SyUserVo;
import com.hd.microauservice.entity.SyUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyUserService extends IService<SyUserEntity> {

    void createUser(SyUserVo syUserVo) throws Exception;
    void removeUser(SyUserVo syUserVo) throws Exception;
}
