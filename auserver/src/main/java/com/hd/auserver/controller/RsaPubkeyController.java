package com.hd.auserver.controller;

import com.hd.auserver.config.GenRsaFileTask;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liwei
 * @Description:
 */
@RestController
public class RsaPubkeyController {


    @GetMapping("/rsapubkey")
    public RetResult rsapubkey() {
        return RetResponse.makeRsp(GenRsaFileTask.rsaPublicKey);
    }
}
