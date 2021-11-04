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
@ApiModel("系统/企业")
public class SyEnterpriseVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    /**
     * 企业编号(root：特殊企业，系统配置员使用关联菜)
     */
    @NotNull(message = "企业编码不能为空！")
    @NotBlank(message = "企业编码不能为空！")
    @ApiModelProperty(value = "企业编号")
    private String enterpriseId;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String note;

    @ApiModelProperty(value = "是否删除标识")
    boolean deleteFlag;

    @ApiModelProperty(value = "是否复制角色")
    private Short createRoles;

    @ApiModelProperty(value = "授权的用户数目")
    private Integer userCount;
}
