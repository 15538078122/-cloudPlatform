package com.hd.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.xml.internal.ws.developer.Serialization;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Serialization
@ApiModel("统一返回json格式")
public class RetResult<T> {
    @ApiModelProperty("响应码")
    private int code;
    @ApiModelProperty("响应的消息")
    private String msg;
    @ApiModelProperty("响应的数据，根据不同的uri决定")
    private T data;
}


