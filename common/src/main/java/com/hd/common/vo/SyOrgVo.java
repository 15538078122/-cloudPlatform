package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.hd.common.utils.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("部门")
public class SyOrgVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;
    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级部门id")
    private Long parentId;

    private String enterpriseId;

    /**
     * 本级编号
     */
    private String levelCode;

    /**
     * 树形编号
     */
    private String pathCode;

    /**
     * 类别{0：组织；1:部门}
     */
    @ApiModelProperty(value = "类别{0：组织；1:部门}")
    private short type;

    /**
     * 状态{0：停用，1：启用}
     */
    private Integer enabled;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    @ApiModelProperty(value = "部门简称")
    private String shortName;

    /**
     * 备注
     */
    private String note;

    /**
     * 图标样式
     */
    @ApiModelProperty(value = "图标样式")
    private String iconClass;

}
