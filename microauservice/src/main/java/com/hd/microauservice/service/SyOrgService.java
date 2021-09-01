package com.hd.microauservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyOrgVo;
import com.hd.common.vo.SyUserVo;
import com.hd.microauservice.entity.SyOrgEntity;

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

    List<SyOrgVo> getMyOrgTree();
    List<SyOrgVo> getMyOrgTreeWithMen();

    List<SyOrgEntity> getMyOrgList();

    boolean haveTopOrg(String enterpriseId);

    void createOrg(SyOrgVo syOrgVo) throws Exception;

    void delOrg(Long orgId);

    List<SyUserVo> getMyOrgMen();
}
