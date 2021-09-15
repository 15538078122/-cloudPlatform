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

    @Cacheable(value = "account", key = "'centerUserId:'+#centerUserId", unless = "#result == null")
    public Long getUserIdByCenterUserIdFromCach(Long centerUserId) {
        Long userId = syUserMapperCommon.getUserIdByCenterUserId(centerUserId);
        return userId;
    }

}
