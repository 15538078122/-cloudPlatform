package com.hd.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@ApiModel("uri请求监视")
public class UriCostVo {
    @ApiModelProperty(value = "请求时间")
    String tm;

    String uri;
    @ApiModelProperty(value = "请求耗时")
    Integer cost;

    @ApiModelProperty(value = "请求平均耗时")
    Float avgcost;

    @ApiModelProperty(value = "请求总耗时")
    Integer totalCost;

    @ApiModelProperty(value = "请求次数")
    Integer count;
}
