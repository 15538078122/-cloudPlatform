package com.hd.micromonitorservice.service;

import com.hd.common.MyPage;
import com.hd.common.vo.OperatorVo;
import com.hd.common.vo.UriCostVo;
import com.hd.micromonitorservice.entity.Operator;
import com.hd.micromonitorservice.entity.UriCost;
import com.hd.micromonitorservice.utils.VoConvertUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wli
 * @since 2021-09-01
 */
public interface UriCostService {
        MyPage<UriCostVo> getMaxCost2Sec(long pageNum, long pageSize);
        MyPage<UriCostVo> getAverageCost2Sec(long pageNum, long pageSize);

        MyPage<OperatorVo> getOperators (long pageNum, long pageSize,String sTime,String eTime,String enterpriseId);

        UriCost save(UriCost uriCost);
        class UriCostVoConvertUtils  extends VoConvertUtils<UriCost,UriCostVo> {

        }
        class UriOperatorVoConvertUtils  extends VoConvertUtils<Operator,OperatorVo> {

        }
}
