package com.hd.microsysservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hd.common.RetCode;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.utils.FileUtil;
import com.hd.common.utils.LicenseUtil;
import com.hd.common.utils.RSASignature;
import com.hd.common.vo.SyAttachVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.hd.microsysservice.service.SyAttachService;
import com.hd.microsysservice.service.SyEnterpriseService;
import com.hd.microsysservice.service.UserCenterFeignService;
import com.hd.microsysservice.utils.JwtUtils;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liwei
 */
@Api(tags = "license授权Controller")
@RestController
@Slf4j
public class LicenseController {

    @Autowired
    SyEnterpriseService syEnterpriseService;
    @Autowired
    UserCenterFeignService userCenterFeignService;
    @Autowired
    JwtUtils jwtUtils;

    @ApiOperation(value = "授权license")
    @RequiresPermissions(value = "license:create", note = "授权license")
    @PostMapping("/license")
    public void license(String machineCode, Long userCount,Long days, HttpServletResponse response) throws IOException {
        Response res =userCenterFeignService.downloadLicense(machineCode,userCount,days);
        Response.Body body = res.body();
        InputStream inputStream= body.asInputStream();
        byte[] sign = new byte[inputStream.available()];
        inputStream.read(sign);
        try(ServletOutputStream output = response.getOutputStream())
        {
            //初始化response.
            response.reset();
            response.setBufferSize(20480);
            //response.setHeader("Content-type", "application/octet-stream;charset=UTF-8");
            String disposition = "attachment";
            String filename = "license.txt";
            response.setHeader("Content-Disposition", disposition + ";filename=" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("ETag", URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setContentType("application/octet-stream;charset=UTF-8");
            Integer length=sign.length;
            response.setHeader("Content-Range", "bytes " + 0 + "-" + (length-1) + "/" + length);
            response.setHeader("Content-length", String.valueOf(length));
            response.setStatus(HttpServletResponse.SC_OK);
            output.write(sign);
            output.flush();
            response.flushBuffer();
        }
    }
    @ApiOperation(value = "上传license文件")
    @RequiresPermissions(value = "license:upload", note = "上传license文件")
    @PostMapping("/license/upload")
    public RetResult licenseUpload(HttpServletRequest request,MultipartFile file) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Assert.isTrue(isMultipart, String.format("没有上传license文件!"));
        ApplicationHome ah = new ApplicationHome(getClass());
        File f = ah.getSource();
        String licensePath =f.getParentFile().toString()+"/"+ SecurityContext.GetCurTokenInfo().getEnterpriseId() +"/license.txt";
        File destFile = new File(licensePath);
        if (destFile.exists()) {
            destFile.delete();
        }
        FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
        //读取授权
        String sign= FileUtil.readTxtFile(licensePath);
        //log.info(licensePath);
        String expDateStr=sign.substring(0,10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expDt=sdf.parse(expDateStr);
        Long userCount=Long.parseLong(sign.substring(10,15));
        String machineCode = LicenseUtil.getMachineCode();
        Boolean check = RSASignature.doCheck( machineCode+userCount+expDateStr,sign.substring(15,sign.length()),jwtUtils.rsaPublicKeyForManufacturer);
        //Assert.isTrue(check,String.format("未授权,请联系厂家授权,机器码%s",machineCode));
        //if(check){
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("enterprise_id",SecurityContext.GetCurTokenInfo().getEnterpriseId());
            updateWrapper.set("user_count",userCount);
            updateWrapper.set("Integer expire_date;",expDateStr);
            syEnterpriseService.update(updateWrapper);
        //}else {

       // }

        return RetResponse.makeRsp("上传license成功");
    }
}
