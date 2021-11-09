package com.hd.micromonitorservice.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@Document(collection="operators")
@AllArgsConstructor
//@NoArgsConstructor
@ApiModel("用户操作")
public class Operator {
    String tm;
    String enterId;
    String operModul;
    String operType;
    String operDesc;
    String account;
    String requestURI;
    String params;
    String err;
    String remoteHost;
}
