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
@ApiModel("操作")
public class SyFuncOperatorVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    @JSONField(serializeUsing = LongToStringSerializer.class)
    @ApiModelProperty(value = "功能id")
    private Long funcId;

    private String name;

    private String note;
}
