package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.hd.common.utils.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Data
@Accessors(chain = true)
@ApiModel("角色")
public class SyRoleVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;
    /**
     * 企业id
     */
    @NotNull(message = "企业Id不能为空！")
    @NotBlank(message = "企业Id不能为空！")
    private String enterpriseId;

    /**
     * 状态{1：启用，0：停用}
     */
    private Integer enabled;

    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空！")
    @NotBlank(message = "名称不能为空！")
    private String name;
    /**
     * 数据权限：0：所有；1：本级及以下；2：仅本级
     */
    @ApiModelProperty(value = "数据权限：0：所有；1：本级及以下；2：仅本级")
    private short dataPrivilege;

    /**     * 备注
     */
    private String note;

    @ApiModelProperty(value = "角色的权限,不更新时，此字段为null")
    private List<SyMenuBtnVo> syMenuBtnVos;
}
