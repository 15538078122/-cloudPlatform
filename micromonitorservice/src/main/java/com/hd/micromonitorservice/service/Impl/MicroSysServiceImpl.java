package com.hd.micromonitorservice.service.Impl;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.vo.SyUrlMappingVo;
import com.hd.micromonitorservice.service.MicroSysService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: liwei
 * @Description:
 */
@Component
public class MicroSysServiceImpl implements MicroSysService {
    @Override
    public RetResult addUrlMapping(@RequestBody SyUrlMappingVo syUrlMappingVo) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
