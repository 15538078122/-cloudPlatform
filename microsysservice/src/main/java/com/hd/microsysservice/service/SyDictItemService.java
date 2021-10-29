package com.hd.microsysservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.vo.SyDictItemVo;
import com.hd.microsysservice.entity.SyDictItemEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-07-30
 */
public interface SyDictItemService extends IService<SyDictItemEntity> {
    MyPage<SyDictItemVo> dictItembycode(PageQueryExpressionList pageQueryExpressionList);
    MyPage<SyDictItemVo> dictItembycode(String code,String enterpriseId,String orderby,Integer pageNum,Integer pageSize);
}
