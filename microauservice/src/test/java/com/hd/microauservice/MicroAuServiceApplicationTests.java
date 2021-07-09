package com.hd.microauservice;

import com.baomidou.mybatisplus.annotation.DbType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.hd.microauservice.utils.GeneralSqlUtil;

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
		String [] tablePrefixes = {};
		// 表名，为空，生成所有的表
		String [] tableNames = {"sy_menu"};
		// 字段前缀
		String [] fieldPrefixes = {};
		// 基础包名
		String packageName = "com.hd.microauservice";
		GeneralSqlUtil.execute(dbType, dbUrl, username, password, driver, tablePrefixes, tableNames, packageName, fieldPrefixes);
	}
}
