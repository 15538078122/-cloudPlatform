package com.hd.microsysservice.service;

import com.hd.common.RetResult;
import com.hd.microsysservice.service.Impl.UserCenterServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "auserver",fallback = UserCenterServiceImpl.class)
public interface UserCenterFeignService {
    @PostMapping("/account")
    RetResult add(@RequestParam("account") String account, @RequestParam("enterprise") String enterprise, @RequestParam("password") String password);
    @DeleteMapping("/account/{id}")
    RetResult remove(@RequestParam("id") Long id);
    @PutMapping("/account")
    RetResult changepwd(@RequestParam ("account")  String account,@RequestParam("enterprise") String enterprise,@RequestParam("password") String password,@RequestParam("passwordOld") String passwordOld);
    @PutMapping("/account/resetpwd")
    RetResult resetpwd(@RequestParam ("account")  String account,@RequestParam("enterprise") String enterprise);
}
