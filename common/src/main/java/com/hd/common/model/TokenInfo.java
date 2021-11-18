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
    String orgId;
    String account;
    String enterpriseId;
    String scopes;
    String uri;
    String method;
    String loginTime;
    String deviceType;
    //用户中心账号id
    String oauthId;
}
