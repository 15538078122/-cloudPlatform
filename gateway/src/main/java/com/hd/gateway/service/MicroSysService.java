package com.hd.gateway.service;

import com.hd.common.RetResult;
import com.hd.common.model.TokenInfo;
import com.hd.gateway.service.Impl.MicroSysServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "microsys",fallback = MicroSysServiceImpl.class)
public interface MicroSysService {
    @PostMapping("/sys/authbridge")
    RetResult authbridge(@RequestBody TokenInfo tokenInfo);
}