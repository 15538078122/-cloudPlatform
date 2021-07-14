package com.hd.microauservice;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.vo.SyUserVo;
import com.hd.microauservice.entity.SyUserEntity;
import com.hd.microauservice.service.SyUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hd.microauservice.utils.GeneralSqlUtil;

import java.sql.Wrapper;

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
		String [] tableNames = {"sy_system","sy_org","sy_user","sy_role","sy_role_perm","sy_user_role"};
		// 字段前缀
		String [] fieldPrefixes = {};
		// 基础包名
		String packageName = "com.hd.microauservice";
		GeneralSqlUtil.execute(dbType, dbUrl, username, password, driver, tablePrefixes, tableNames, packageName, fieldPrefixes);
	}

	@Autowired
	SyUserService syUserService;

	@Test
	void  logicDel() throws Exception {
		SyUserVo syUserEntity=new SyUserVo(){{
			setAccount("testuser");
			setPasswordMd5("1234");
			setEnterpriseId("abc");
		}};
		syUserService.createUser(syUserEntity);

		QueryWrapper queryWrapper=new QueryWrapper(){{
			eq("account","testuser");
		}};
		syUserService.remove(queryWrapper);

		syUserService.list();
	}
}
