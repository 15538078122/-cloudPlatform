package com.hd.microsysservice.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.utils.FileUtil;
import com.hd.common.utils.MachineCodeUtil;
import com.hd.common.utils.RSASignature;
import com.hd.microsysservice.conf.GeneralConfig;
import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.hd.microsysservice.service.SyEnterpriseService;
import com.hd.microsysservice.service.SyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Component
public class LicenseCheckUtil {
    @Autowired
    SyUserService syUserService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    SyEnterpriseService syEnterpriseService;
    public String getExpiredDate(String enterpriseId,String licName){
        ApplicationHome ah = new ApplicationHome(getClass());
        File file = ah.getSource();
        String licensePath = file.getParentFile().toString() + "/" + enterpriseId + "/"+licName;
        String sign = FileUtil.readTxtFile(licensePath);
        //log.info(licensePath);
        if (sign.isEmpty()) {
            licensePath = file.getParentFile().toString() + "/../" + enterpriseId + "/"+licName;
            sign = FileUtil.readTxtFile(licensePath);
        }
        String expDateStr = sign.substring(0, 10);
        return expDateStr;
    }

    public Long CheckLicense(String enterpriseId,String licName) throws Exception {
        //判断授权有效性
        ApplicationHome ah = new ApplicationHome(getClass());
        File file = ah.getSource();
        String licensePath = file.getParentFile().toString() + "/" + enterpriseId + "/"+licName;
        String sign = FileUtil.readTxtFile(licensePath);
        //log.info(licensePath);
        if (sign.isEmpty()) {
            licensePath = file.getParentFile().toString() + "/../" + enterpriseId + "/"+licName;
            sign = FileUtil.readTxtFile(licensePath);
        }
        String machineCode = MachineCodeUtil.getMachineCode();
        Assert.isTrue(sign.length() > 50, String.format("未授权,请联系厂家授权!"));
        String expDateStr = sign.substring(0, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expDt = sdf.parse(expDateStr);
        Long userCount = Long.parseLong(sign.substring(10, 15));

        Assert.isTrue(expDt.getTime() >= System.currentTimeMillis(), String.format("授权已过期,请联系厂家授权!"));

        Boolean check = RSASignature.doCheck(machineCode + userCount + expDateStr + enterpriseId, sign.substring(15, sign.length()), jwtUtils.rsaPublicKeyForManufacturer);
        //String sign = RSASignature.sign((RSAPrivateKey) tokenConfig.rsaPrivateKey, machineCode+userCount);
        Assert.isTrue(check, String.format("未授权,请联系厂家授权!"));

        return userCount;
    }

    public void JudgeLicenseForCreateUser(String enterpriseId) throws Exception {
        Long userCount = CheckLicense(enterpriseId,"license.txt");
        QueryWrapper queryWrapper = new QueryWrapper() {{
            eq("enterprise_id", enterpriseId);
        }};
        queryWrapper.eq("delete_flag", 0);
        int totalCount = syUserService.count(queryWrapper);
        Assert.isTrue(totalCount - 1 < userCount, String.format("授权数量超限,请联系厂家授权!"));
    }

    public void checkAllLicense() {
        List<SyEnterpriseEntity> syEnterpriseEntities = syEnterpriseService.listByMap(new HashMap<String, Object>() {{
            put("delete_flag", 0);
        }});
        for (SyEnterpriseEntity syEnterpriseEntity : syEnterpriseEntities) {
            try {
                Long userCount = CheckLicense(syEnterpriseEntity.getEnterpriseId(),"license.txt");
                //判断用户数目
                QueryWrapper queryWrapper = new QueryWrapper() {{
                    eq("enterprise_id", syEnterpriseEntity.getEnterpriseId());
                }};
                queryWrapper.eq("delete_flag", 0);
                int totalCount = syUserService.count(queryWrapper);
                Assert.isTrue(totalCount-1 <= userCount, String.format("授权数量超限,请联系厂家授权!"));
                GeneralConfig.getLicenseStatus().put(syEnterpriseEntity.getEnterpriseId(), true);
            } catch (Exception ex) {
                //授权过期
                GeneralConfig.getLicenseStatus().put(syEnterpriseEntity.getEnterpriseId(), false);
            }
        }
    }

    public void checkIfLicenseExpired(String enterpriseId) {
        //判断企业是否授权过期
        Boolean licenseStatus = GeneralConfig.getLicenseStatus().get(enterpriseId);
        Assert.isTrue(licenseStatus == null || licenseStatus, "授权过期,请联系厂家授权!");
    }

}
