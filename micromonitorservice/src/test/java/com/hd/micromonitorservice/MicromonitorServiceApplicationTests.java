package com.hd.micromonitorservice;

import com.baomidou.mybatisplus.annotation.DbType;
import com.hd.common.MyPage;
import com.hd.common.vo.UriCostVo;
import com.hd.micromonitorservice.entity.UriCost;
import com.hd.micromonitorservice.service.UriCostService;
import com.hd.micromonitorservice.utils.GeneralSqlUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class MicromonitorServiceApplicationTests {

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
        String[] tableNames = {"sy_monitor"}; //{"sy_dict","sy_dict_item","sy_system","sy_org","sy_user","sy_role","sy_role_perm","sy_user_role"};
        // 字段前缀
        String[] fieldPrefixes = {};
        // 基础包名
        String packageName = "com.hd.micromonitorservice";
        GeneralSqlUtil.execute(dbType, dbUrl, username, password, driver, tablePrefixes, tableNames, packageName, fieldPrefixes);
    }

    @Autowired
    UriCostService uriCostService;

    @Test
    void mongodbTest() {
    	UriCost uriCost=new UriCost(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                                    "/xxx/yy",34,0.4f,0,0);
		uriCost = uriCostService.save(uriCost);
		MyPage<UriCostVo> myPage = uriCostService.getMaxCost2Sec(1,100);
	}
}
