package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.model.DataPrivilege;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.entity.SyUserEntity;

import java.util.List;

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
    void removeUser(Long userId) throws Exception;

    SyUserEntity getUserByAccount(String account, String enterpriseId);
    DataPrivilege getUserDataPrivilege(Long userId);
    void updateUser(SyUserVo syUserVo) throws Exception;

    SyUserVo getUser(String userId) throws Exception;

    List<SyUserVo> getOrgUser(Long orgId);

    void changepwd(SyUserVo syUserVo) throws Exception;
}
