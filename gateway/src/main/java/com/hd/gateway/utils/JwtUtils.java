package com.hd.gateway.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hd.common.model.TokenInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;


//@ConditionalOnClass(name = "xxddd")
@Component
public class JwtUtils {

    private  String publicKey = null;
    RsaVerifier  rsaVerifier  ;
    RSAPublicKey rsaPublicKey  ;

    @PostConstruct
    public  void  init(){
        Resource resource = new ClassPathResource("public.txt");
        try {
            publicKey = inputStream2String(resource.getInputStream());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        //String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIndyaXRlMSJdLCJleHAiOjE2MjI2MTA3MzQsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMzc5NWM4ZGMtMzliYi00MDY5LTk5NjItM2EwMzI1MjdlYzZkIiwiY2xpZW50X2lkIjoiY2xpZW50IiwidXNlcm5hbWUiOiJhZG1pbiJ9.XgAtIJdzW0pqoPeCBxKua8GVvnxqbKaNh0lsRzPDgkNWVkea8zOeUlaHTW9qQ0uTCTS9ch0ogmygtIcalHizJz_4JI25hh1Q_7Cv4vEqOfXmNM_pxAlMa8-GTJxiYVrO_ttY_o2neaNy-CUeyrKNJKbyydhJIY7KvEpLEBbsB6ij5YNjnUdv5_D4GvCKmyDq086UMkNzVl7cyOIuUnq7BEm4DYtFa2mWrdJRN3BIMG2Gn1x3mrVWuMVDgi8O9_UG7lQMeWBa6m4nCOusgcITJWo90yx6YA3g_5x60AoMydw8xgwc_uXzA9WUVPtH3gPf6BL03Ck5puh69-edXJRcsQ";
        rsaVerifier = new RsaVerifier(publicKey);
        rsaPublicKey = RsaKeyHelper.parsePublicKey(publicKey.trim());
    }

    private    String   inputStream2String(InputStream is)   throws   Exception{
        ByteArrayOutputStream baos   =   new ByteArrayOutputStream();
        int   i=-1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return   baos.toString();
    }
    public TokenInfo decodeToken(String token) throws Exception {

        Jwt jwt = JwtHelper.decodeAndVerify(token, rsaVerifier);
        String claimsStr = jwt.getClaims();
        JSONObject  jsonObj = (JSONObject)JSONObject.parse(claimsStr);
        String username = jsonObj.getString("user_name");
        String loginTime = jsonObj.getString("login_time");
        String enterpriseId = jsonObj.getString("enterprise_id");
        String id = jsonObj.getString("id");

        String clientId = jsonObj.getString("client_id");
        JSONArray scopeArray = jsonObj.getJSONArray("scope");
        String []scopes=new String[scopeArray.size()];
        scopeArray.toArray(scopes);
        String exp = jsonObj.getString("exp");
        //List<String> authorities=new ArrayList<>();
        String scope="";
        for (int i=0;i<scopes.length;i++) {
            scope+=scopes[i];
            if(i<scopes.length-1){
                scope+=",";
            }
        }
        TokenInfo tokenInfo=new TokenInfo(id,username,enterpriseId,scope,null,null,loginTime);
        return  tokenInfo;

    }

}

