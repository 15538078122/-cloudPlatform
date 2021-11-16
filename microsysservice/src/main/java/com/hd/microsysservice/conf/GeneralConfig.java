package com.hd.microsysservice.conf;

import java.util.HashMap;

/**
 * @Author: liwei
 * @Description:
 */
public class GeneralConfig {
    /** 根企业code */
    public static final String ROOT_ENTERPRISE_ID = "root";
    /** 企业默认管理员 */
    public static final String ENTERPRISE_ADMIN = "admin";
    private static HashMap<String,Boolean> licenseStatus =new HashMap<String,Boolean>();
    public static HashMap<String,Boolean> getLicenseStatus(){
        return licenseStatus;
    }
}
