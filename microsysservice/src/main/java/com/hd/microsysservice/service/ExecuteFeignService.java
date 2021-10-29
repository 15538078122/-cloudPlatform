package com.hd.microsysservice.service;

import com.hd.common.RetResult;
import com.hd.microsysservice.service.Impl.ExecuteFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "execute",fallback = ExecuteFeignServiceImpl.class)
public interface ExecuteFeignService {
    @GetMapping("/api.ps/v1/patrolSheet/undone/{userId}")
    RetResult undone(@RequestParam("userId") Long userId);
}
