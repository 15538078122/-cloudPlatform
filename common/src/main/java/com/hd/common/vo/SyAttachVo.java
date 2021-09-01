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
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("文档")
public class SyAttachVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    @ApiModelProperty("enterprise_id")
    private String enterpriseId;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    @ApiModelProperty(value = "上传人id")
    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long uploadBy;

    @ApiModelProperty(value = "上传人name")
    private String uploadByName;

    @NotNull(message = "上传文件名不能为空！")
    @NotBlank(message = "上传文件名不能为空！")
    private String fileName;

    @ApiModelProperty(value = "文件mimetype")
    private String contentType;

    @ApiModelProperty(value = "文件存储路径")
    private String fileNewName;
}
