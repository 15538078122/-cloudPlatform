package com.hd.micromonitorservice.service;

import com.hd.common.RetResult;
import com.hd.common.vo.SyUrlMappingVo;
import com.hd.micromonitorservice.service.Impl.MicroSysServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "microsys",fallback = MicroSysServiceImpl.class)
public interface MicroSysService {
    @PostMapping("/sys/url-mapping")
    RetResult addUrlMapping(@RequestBody SyUrlMappingVo syUrlMappingVo);
}