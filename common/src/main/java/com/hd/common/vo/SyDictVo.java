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
@ApiModel("字典项")
public class SyDictVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    private String enterpriseId;

    /**
     * 编号
     */
    @NotNull(message = "字典编码不能为空！")
    @NotBlank(message = "字典编码不能为空！")
    @ApiModelProperty(value = "字典编号")
    private String code;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态{1:启用,0:停用}
     */
    private Integer enabled;

    /**
     * 类别
     */
    private String cate;

    /**
     * 名称
     */
    @NotNull(message = "字典名称不能为空！")
    @NotBlank(message = "字典名称不能为空！")
    @ApiModelProperty(value = "字典名称")
    private String name;

    /**
     * 备注
     */
    private String note;
}
