package com.hd.microsysservice.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;


//@ConditionalOnClass(name = "xxddd")
@Component
public class JwtUtils {

    private  String publicKey = null;
    public   RSAPublicKey rsaPublicKey  ;

    @PostConstruct
    public  void  init(){
        Resource resource = new ClassPathResource("public.txt");
        try {
            publicKey = inputStream2String(resource.getInputStream());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        //String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbIndyaXRlMSJdLCJleHAiOjE2MjI2MTA3MzQsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMzc5NWM4ZGMtMzliYi00MDY5LTk5NjItM2EwMzI1MjdlYzZkIiwiY2xpZW50X2lkIjoiY2xpZW50IiwidXNlcm5hbWUiOiJhZG1pbiJ9.XgAtIJdzW0pqoPeCBxKua8GVvnxqbKaNh0lsRzPDgkNWVkea8zOeUlaHTW9qQ0uTCTS9ch0ogmygtIcalHizJz_4JI25hh1Q_7Cv4vEqOfXmNM_pxAlMa8-GTJxiYVrO_ttY_o2neaNy-CUeyrKNJKbyydhJIY7KvEpLEBbsB6ij5YNjnUdv5_D4GvCKmyDq086UMkNzVl7cyOIuUnq7BEm4DYtFa2mWrdJRN3BIMG2Gn1x3mrVWuMVDgi8O9_UG7lQMeWBa6m4nCOusgcITJWo90yx6YA3g_5x60AoMydw8xgwc_uXzA9WUVPtH3gPf6BL03Ck5puh69-edXJRcsQ";
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

}

