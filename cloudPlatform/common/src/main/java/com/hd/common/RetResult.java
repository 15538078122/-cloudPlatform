package com.hd.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.xml.internal.ws.developer.Serialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@Serialization
public class RetResult<T> {
    private int code;
    private String msg;
    private T data;
}


