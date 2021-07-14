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
@ApiModel("扫描的uri，供选择用")
public class SyUrlMappingVo implements Serializable {

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
     * controller的className.method
     */
    @ApiModelProperty(value = "响应方法，controller的className.method")
    private String handler;

    /**
     * 注释
     */

    private String notes;

}
