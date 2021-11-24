package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyOrgVo;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.entity.SyOrgEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-13
 */
public interface SyOrgService extends IService<SyOrgEntity> {
    List<SyOrgVo> getOrgTree(String enterpriseId);

    List<SyOrgVo> getMyOrgTree(Boolean includeDel);
    List<SyOrgVo> getMyOrgTreeWithMen();

    List<SyOrgEntity> getMyOrgList();

    boolean haveTopOrg(String enterpriseId);

    Long createOrg(SyOrgVo syOrgVo) throws Exception;

    void delOrg(Long orgId) throws Exception;

    List<SyUserVo> getMyOrgMen();
    void updateOrg(SyOrgVo syOrgVo) throws Exception;
    void moveUserToChild(List<SyOrgVo> syOrgVos);
}
