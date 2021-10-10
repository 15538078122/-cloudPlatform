package com.hd.microsysservice.utils;

import com.hd.microsysservice.mapper.SyUserMapperCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @Author: liwei
 * @Description:
 */
@Component
public class UserCommonUtil {

    @Autowired
    SyUserMapperCommon syUserMapperCommon;

    //    @Cacheable(value = "centerId2userId", key = "'centerUserId:'+#centerUserId", unless = "#result == null")
    @Cacheable(value = "centerId2userId", key = "#centerUserId", unless = "#result == null")
    public String getUserIdByCenterUserIdFromCach(Long centerUserId) {
        String userId = syUserMapperCommon.getUserIdByCenterUserId(centerUserId);
        return userId;
    }

}
