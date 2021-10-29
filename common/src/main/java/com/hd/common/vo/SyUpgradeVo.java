package com.hd.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.hd.common.utils.LongToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@ApiModel("升级vo")
public class SyUpgradeVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    /**
     * 附件id
     */
    @NotNull(message = "附件id不能为空！")
    //@NotBlank(message = "附件id不能为空！")
    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long attachId;

    /**
     * 应用类型
     */

    @NotNull(message = "应用类型不能为空！")
    //@NotBlank(message = "应用类型不能为空！")
    private Integer type;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 版本名
     */
    @NotNull(message = "版本名不能为空！")
    @NotBlank(message = "版本名不能为空！")
    private String versionName;

    /**
     * 升级说明
     */
    private String desc;

    /**
     * 创建时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @NotNull(message = "企业id不能为空！")
    @NotBlank(message = "企业id不能为空！")
    private String enterpriseId;
}
