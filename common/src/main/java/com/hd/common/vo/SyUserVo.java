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
    private String account;

    /**
     * 名称
     */
    private String name;

    /**
     * 密码hash值
     */
    private String passwordMd5;

    /**
     * 电话
     */
    private String phone;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String jobName;

    /**
     * 密码修改时间
     */
    private Date modifiedTime;

    @ApiModelProperty(value = "删除标志")
    private boolean deleteFlag;
}
