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
@ApiModel("功能")
public class SyFunctionVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    private String name;

    /**
     * 本级编号
     */
    private String levelCode;

    /**
     * 树形编号
     */

    private String pathCode;

    /**
     * 0:目录；1:功能
     */
    @ApiModelProperty(value = "菜单类型->0:目录,1:功能")
    private Short type;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 备注
     */
    private String note;

    @ApiModelProperty(value = "子菜单")
    private List<SyFunctionVo> childs;

    @ApiModelProperty(value = "操作")
    private List<SyFuncOperatorVo> oprs;
}
