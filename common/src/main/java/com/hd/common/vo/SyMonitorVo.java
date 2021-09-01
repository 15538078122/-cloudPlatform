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
@ApiModel("服务监视")
public class SyMonitorVo implements Serializable {

    @JSONField(serializeUsing = LongToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "服务显示名称")
    private String showName;

    @ApiModelProperty(value = "服务最后一次心跳")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date heartbeatTm;

    @ApiModelProperty(value = "服务是否在线")
    private Boolean onLine;
}
