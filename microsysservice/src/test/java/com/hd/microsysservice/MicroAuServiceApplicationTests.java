package com.hd.microsysservice;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.utils.RSAEncrypt;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.hd.microsysservice.mapper.SyMaintainMapper;
import com.hd.microsysservice.service.SyEnterpriseService;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.utils.GeneralSqlUtil;
import com.hd.microsysservice.utils.JwtUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class MicroAuServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void generalEntity() {
        DbType dbType = DbType.MYSQL;
        String dbUrl = "jdbc:mysql://127.0.0.1:3333/patrol?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        // 表前缀，生成的实体类，不含前缀
        String[] tablePrefixes = {};
        // 表名，为空，生成所有的表
        String[] tableNames = {"sys_upgrade"}; //{"sy_dict","sy_dict_item","sy_system","sy_org","sy_user","sy_role","sy_role_perm","sy_user_role"};
        // 字段前缀
        String[] fieldPrefixes = {};
        // 基础包名
        String packageName = "com.hd.microsysservice";
        GeneralSqlUtil.execute(dbType, dbUrl, username, password, driver, tablePrefixes, tableNames, packageName, fieldPrefixes);
    }

    @Autowired
    SyUserService syUserService;

    @Test
    void logicDel() throws Exception {
        SyUserVo syUserEntity = new SyUserVo() {{
            setAccount("testuser");
            setPasswordMd5("1234");
            setEnterpriseId("abc");
        }};
        syUserService.createUser(syUserEntity);

        QueryWrapper queryWrapper = new QueryWrapper() {{
            eq("account", "testuser");
        }};
        syUserService.remove(queryWrapper);

        syUserService.list();
    }

    @Autowired
    SyMaintainMapper syMaintainMapper;
    @Autowired
    SyEnterpriseService syEnterpriseService;

    @Test
    void physicallyDeleteEnterprise() {
        QueryWrapper queryWrapper = new QueryWrapper() {{
            ne("enterprise_id", "root");
            //ne("enterprise_id","huadaokeji");
        }};
		List<SyEnterpriseEntity> syEnterpriseEntities  = syEnterpriseService.list(queryWrapper);
		for(SyEnterpriseEntity syEnterpriseEntity:syEnterpriseEntities){
			syMaintainMapper.deleteEnterprisePhysically(syEnterpriseEntity.getEnterpriseId());
		}
    }

    @Autowired
    JwtUtils jwtUtils;

    @Test
    void genRsaCipher() throws Exception {
        String USER_OP_IDENTIFICATION = "SFSDF-332-DFDGSG-323-EG-SDG$rfy*kjg";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
        String userOpIdentificationEncode = Base64.encode(cipherData);
        userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
        System.out.println(userOpIdentificationEncode);
    }

    @Test
    void PathMatcher() {
        PathMatcher pathMatcher = new AntPathMatcher();
        String templateUri = "/usr/{accout}";
        templateUri = templateUri.replaceAll("\\{[^}]*\\}", "*");
        templateUri = "/usr/{accout}/sfsf/{sfsd}";
        templateUri = templateUri.replaceAll("\\{[^}]*\\}", "*");

        boolean match = pathMatcher.match("/user/*", "/user/ere/root");
        match = pathMatcher.match("/user/*/list", "/user/root/list");
        match = pathMatcher.match("/user/root", "/user/root");
        match = pathMatcher.match("/user/**", "/user/root/tyyt");
    }

}
