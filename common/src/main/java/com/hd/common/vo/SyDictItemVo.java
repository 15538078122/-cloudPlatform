package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.hd.common.utils.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("字典项的值")
public class SyDictItemVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    /**
     * 字典项id
     */
    @NotNull(message = "字典项id不能为空！")
    @ApiModelProperty(value = "字典项id")
    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long dictId;

    /**
     * 状态{1:启用,0:停用}
     */
    private Integer enabled;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sort;

    /**
     * 值
     */
    @NotNull(message = "值不能为空！")
    @NotBlank(message = "值不能为空！")
    @ApiModelProperty(value = "值")
    private String code;

    /**
     * 显示名称
     */
    @NotNull(message = "名称不能为空！")
    @NotBlank(message = "名称不能为空！")
    @ApiModelProperty(value = "名称")
    private String name;

}
