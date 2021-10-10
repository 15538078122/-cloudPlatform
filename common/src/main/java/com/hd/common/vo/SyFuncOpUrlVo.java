package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.hd.common.utils.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@ApiModel("操作对应的权限")
public class SyFuncOpUrlVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    /**
     * url地址
     */
    @ApiModelProperty(value = "请求类型+uri")
    private String url;

    /**
     * controller的className.method
     */
    @ApiModelProperty(value = "权限字符串标识")
    private String permCode;

    /**
     * 功能id
     */
    @ApiModelProperty(value = "对应的操作id")
    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long funcOpId;

    private String notes;

}
