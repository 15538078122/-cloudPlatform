package com.hd.microsysservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.microsysservice.service.SyEnterpriseService;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.service.UserCenterFeignService;
import com.hd.microsysservice.utils.JwtUtils;
import com.hd.microsysservice.utils.LicenseCheckUtil;
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
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Autowired
    SyUserService syUserService;
    @Autowired
    LicenseCheckUtil licenseCheckUtil;

    @ApiOperation(value = "授权license")
    @RequiresPermissions(value = "license:create", note = "授权license")
    @PostMapping("/license")
    public void license(String machineCode, Long userCount,Long days,String enterpriseId, HttpServletResponse response) throws IOException {
        Response res =userCenterFeignService.downloadLicense(machineCode,userCount,days,enterpriseId);
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
            String filename = "license.lic";
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
    public RetResult licenseUpload(HttpServletRequest request,String enterpriseId,MultipartFile file) throws Exception {
        Assert.isTrue(enterpriseId!=null,String.format("企业参数%s不能为null!","enterpriseId"));
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Assert.isTrue(isMultipart, String.format("没有上传license文件!"));
        ApplicationHome ah = new ApplicationHome(getClass());
        File f = ah.getSource();
        //String licensePath =f.getParentFile().toString()+"/"+ SecurityContext.GetCurTokenInfo().getEnterpriseId() +"/license.txt";
        String enterprisePath=f.getParentFile().toString()+"/"+ enterpriseId;
        String licensePath =enterprisePath +"/license_new.txt";
        File destFile = new File(licensePath);
        if (destFile.exists()) {
            destFile.delete();
        }
        FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
        Long userCount =    licenseCheckUtil.CheckLicense(enterpriseId,"license_new.txt");

        QueryWrapper queryWrapper = new QueryWrapper() {{
            eq("enterprise_id", enterpriseId);
        }};
        queryWrapper.eq("delete_flag",0);
        int totalCount = syUserService.count(queryWrapper);
        Assert.isTrue(totalCount-1<=userCount,String.format("授权失败，已存在用户数量超限，请先删除!"));

        String licensePath2 =enterprisePath +"/license.txt";
        File destFile2 = new File(licensePath2);
        if (destFile2.exists()) {
            destFile2.delete();
        }
        destFile.renameTo(destFile2);

        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("enterprise_id",enterpriseId);
        updateWrapper.set("user_count",userCount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateStr=sdf.format(new Date());
        String expDateStr=licenseCheckUtil.getExpiredDate(enterpriseId,"license.txt");
        updateWrapper.set("expire_date",nowDateStr+" 至 "+expDateStr);
        syEnterpriseService.update(updateWrapper);

        licenseCheckUtil.checkAllLicense();
        return RetResponse.makeRsp("上传license成功");
    }
}
