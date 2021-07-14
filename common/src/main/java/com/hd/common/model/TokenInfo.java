package com.hd.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: liwei
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    String id;
    String account;
    String companyCode;
    String scopes;
    String uri;
    String method;
    String loginTime;
}
