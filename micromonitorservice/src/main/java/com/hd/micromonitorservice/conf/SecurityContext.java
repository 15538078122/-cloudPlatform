package com.hd.micromonitorservice.conf;

import com.hd.common.model.TokenInfo;

/**
 * @Author: liwei
 * @Description:
 */
public class SecurityContext {
    private static final ThreadLocal<TokenInfo> currentTokenInfo = new ThreadLocal<>();
    public  static TokenInfo GetCurTokenInfo(){
        return  currentTokenInfo.get();
    }
    public  static void SetCurTokenInfo(TokenInfo tokenInfo){
        currentTokenInfo.set(tokenInfo);
    }
}
