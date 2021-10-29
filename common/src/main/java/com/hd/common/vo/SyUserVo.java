package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hd.common.utils.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("用户")
public class SyUserVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    /**
     * 所属部门
     */
    @JSONField(serializeUsing = LongToStringSerializer.class)
    @ApiModelProperty(value = "所属部门id")
    private Long orgId;

    /**
     * 租户id
     */
    private String enterpriseId;

    /**
     * 1:启用,0:停用
     */
    private Boolean enabled;

    /**
     * 登录账户
     */
    @NotNull(message = "名称不能为空！")
    @NotBlank(message = "名称不能为空！")
    private String account;

    /**
     * 名称
     */
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    private String name;

    /**
     * 密码hash值
     */
    private String passwordMd5;

    /**
     * 密码hash值
     */
    private String passwordMd5Old;
    /**
     * 电话
     */
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    private String phone;

    /**
     * 手机
     */
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    private String mobile;

    /**
     * 职务
     */
    @JSONField(serialzeFeatures={SerializerFeature.WriteNullStringAsEmpty})
    @ApiModelProperty(value = "职务")
    private String jobName;

    /**
     * 修改时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "删除标志")
    private boolean deleteFlag;

    @ApiModelProperty(value = "类型标识")
    private Long typeFlag;

    @ApiModelProperty(value = "用户头像")
    private Long headPic;

    @JSONField(serialzeFeatures={SerializerFeature.WriteNullListAsEmpty})
    @ApiModelProperty(value = "用户角色,不更新时，此字段为null")
    private List<SyRoleVo> syRoleVos;

    @ApiModelProperty(value = "用户角色标志:0:管理员;1:普通用户")
    private Short userFlag=1;
}
