package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
@ApiModel("用户操作")
public class OperatorVo {
    @ApiModelProperty(value = "操作时间")
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    String tm;
    @ApiModelProperty(value = "操作模块")
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    String operModul;
    @ApiModelProperty(value = "操作类型")
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    String operType;
    @ApiModelProperty(value = "操作描述")
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    String operDesc;
    @ApiModelProperty(value = "用户账号")
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    String account;
    @ApiModelProperty(value = "参数")
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    String params;;
    @ApiModelProperty(value = "异常描述")
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    String err;
}
