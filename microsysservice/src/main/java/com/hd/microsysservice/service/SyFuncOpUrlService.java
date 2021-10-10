package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.vo.SyFuncOpUrlVo;
import com.hd.microsysservice.entity.SyFuncOpUrlEntity;

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
    String getPermissionCode(String method, String uri);
    List<SyFuncOpUrlEntity> getUrlTemplateList();
    List<SyFuncOpUrlVo> getfunOpUrl(Long funcOprId);
}
