package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.hd.common.utils.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@ApiModel("按钮")
public class SyMenuBtnVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    private String enterpriseId;

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long menuId;

    private Integer enabled;

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long funcOpId;

    private String name;

    @ApiModelProperty(value = "html的控件id")
    private String htmlId;

    @ApiModelProperty(value = "js的处理函数入口")
    private String jshandler;

    @ApiModelProperty(value = "图标class")
    private String iconClass;

    private Integer isVisible;


}
