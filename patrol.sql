/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1_3333
Source Server Version : 80011
Source Host           : 127.0.0.1:3333
Source Database       : patrol

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2021-07-09 16:43:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sy_attach
-- ----------------------------
DROP TABLE IF EXISTS `sy_attach`;
CREATE TABLE `sy_attach` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT '0' COMMENT '文件大小',
  `upload_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `upload_by` bigint(20) DEFAULT NULL COMMENT '上传人id',
  `file_name` varchar(200) DEFAULT NULL COMMENT '上传文件名',
  `content_type` varchar(200) DEFAULT NULL COMMENT '文件mimetype',
  `root_path` varchar(200) DEFAULT NULL COMMENT '路径起始',
  `store_path` varchar(200) DEFAULT NULL COMMENT '存储路径，包括存储文件名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_attach
-- ----------------------------

-- ----------------------------
-- Table structure for sy_dict
-- ----------------------------
DROP TABLE IF EXISTS `sy_dict`;
CREATE TABLE `sy_dict` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  `code` char(128) DEFAULT NULL COMMENT '编号',
  `sort_` int(11) DEFAULT '0' COMMENT '排序号',
  `enabled` int(11) DEFAULT '1' COMMENT '状态{1:启用,0:停用}',
  `cate` varchar(100) DEFAULT NULL COMMENT '类别',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sy_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sy_dict_item`;
CREATE TABLE `sy_dict_item` (
  `id` bigint(20) NOT NULL,
  `dict_id` bigint(20) DEFAULT NULL COMMENT '字典id',
  `enabled` int(11) DEFAULT '1' COMMENT '状态{1:启用,0:停用}',
  `sort_` int(11) DEFAULT '0' COMMENT '排序号',
  `code` char(128) DEFAULT NULL COMMENT '值',
  `name` varchar(100) DEFAULT NULL COMMENT '显示名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_dict_item
-- ----------------------------

-- ----------------------------
-- Table structure for sy_function
-- ----------------------------
DROP TABLE IF EXISTS `sy_function`;
CREATE TABLE `sy_function` (
  `id` bigint(20) NOT NULL,
  `code` char(128) DEFAULT NULL COMMENT '业务编号',
  `level_code` char(3) DEFAULT NULL COMMENT '本级编号',
  `path_code` char(128) DEFAULT NULL COMMENT '树形编号',
  `type_flag` int(11) DEFAULT NULL COMMENT '0:模块，1：功能',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父id',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `enabled` int(11) DEFAULT '1' COMMENT '可用状态，1：启用，0：停用',
  `note` varchar(250) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_function
-- ----------------------------

-- ----------------------------
-- Table structure for sy_func_operator
-- ----------------------------
DROP TABLE IF EXISTS `sy_func_operator`;
CREATE TABLE `sy_func_operator` (
  `id` bigint(20) DEFAULT NULL,
  `func_id` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`func_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_func_operator
-- ----------------------------

-- ----------------------------
-- Table structure for sy_func_op_url
-- ----------------------------
DROP TABLE IF EXISTS `sy_func_op_url`;
CREATE TABLE `sy_func_op_url` (
  `id` bigint(20) NOT NULL,
  `url` varchar(250) DEFAULT NULL COMMENT 'url地址',
  `method` varchar(12) DEFAULT NULL COMMENT 'http方法',
  `perm_code` varchar(100) DEFAULT NULL COMMENT 'controller的className.method',
  `func_op_id` bigint(20) DEFAULT NULL COMMENT '功能id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_func_op_url
-- ----------------------------

-- ----------------------------
-- Table structure for sy_menu
-- ----------------------------
DROP TABLE IF EXISTS `sy_menu`;
CREATE TABLE `sy_menu` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `level_code` char(3) DEFAULT NULL COMMENT '本级编号',
  `path_code` char(128) DEFAULT NULL COMMENT '树形编号',
  `enabled` int(11) DEFAULT '1' COMMENT '状态,0:停用，1：启用',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `url` varchar(250) DEFAULT NULL COMMENT '相关url',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  `icon_class` varchar(100) DEFAULT NULL COMMENT '图标样式',
  `is_visible` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表';

-- ----------------------------
-- Records of sy_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sy_menu_btn
-- ----------------------------
DROP TABLE IF EXISTS `sy_menu_btn`;
CREATE TABLE `sy_menu_btn` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL COMMENT '关联菜单id',
  `enabled` int(11) DEFAULT '1' COMMENT '状态{1:启用,0:停用}',
  `func_op_id` bigint(20) DEFAULT NULL COMMENT '关联操作id',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `html_id` varchar(50) DEFAULT NULL,
  `jshandler` varchar(200) DEFAULT NULL COMMENT 'click handler',
  `icon_class` varchar(100) DEFAULT NULL COMMENT '图标样式',
  `is_visible` tinyint(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_menu_btn
-- ----------------------------

-- ----------------------------
-- Table structure for sy_org
-- ----------------------------
DROP TABLE IF EXISTS `sy_org`;
CREATE TABLE `sy_org` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
  `enterprise_id` varchar(20) NOT NULL,
  `level_code` char(3) DEFAULT NULL COMMENT '本级编号',
  `path_code` char(128) DEFAULT NULL COMMENT '树形编号',
  `cate` int(11) DEFAULT '0' COMMENT '组织类别{0：企业，局，10：段，处，20：车间，30：工区}',
  `enabled` int(11) DEFAULT '1' COMMENT '状态{0：停用，1：启用}',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(200) DEFAULT NULL COMMENT '简称',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  `icon_class` varchar(100) DEFAULT NULL COMMENT '图标样式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_org
-- ----------------------------

-- ----------------------------
-- Table structure for sy_role
-- ----------------------------
DROP TABLE IF EXISTS `sy_role`;
CREATE TABLE `sy_role` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `enabled` int(11) DEFAULT '1' COMMENT '状态{1：启用，0：停用}',
  `sort_` int(11) DEFAULT '0' COMMENT '排序号',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_role
-- ----------------------------

-- ----------------------------
-- Table structure for sy_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `sy_role_perm`;
CREATE TABLE `sy_role_perm` (
  `id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) NOT NULL,
  `menu_btn_id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`role_id`,`menu_btn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_role_perm
-- ----------------------------

-- ----------------------------
-- Table structure for sy_system
-- ----------------------------
DROP TABLE IF EXISTS `sy_system`;
CREATE TABLE `sy_system` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '企业编号(s00000：特殊企业，系统配置员使用关联菜单；s00001：特殊企业，企业管理员使用关联菜单)',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `note` varchar(250) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_system
-- ----------------------------

-- ----------------------------
-- Table structure for sy_url_mapping
-- ----------------------------
DROP TABLE IF EXISTS `sy_url_mapping`;
CREATE TABLE `sy_url_mapping` (
  `id` bigint(20) NOT NULL,
  `url` varchar(250) DEFAULT NULL COMMENT 'url地址',
  `perm_code` varchar(100) DEFAULT NULL,
  `handler` varchar(200) DEFAULT NULL COMMENT 'controller的className.method',
  `notes` varchar(300) DEFAULT NULL COMMENT '注释',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_url_mapping
-- ----------------------------
INSERT INTO `sy_url_mapping` VALUES ('1413382235780026370', 'post /auth', 'permission:auth', 'com.hd.microauservice.controller.AuthenticationController:auth', null);
INSERT INTO `sy_url_mapping` VALUES ('1413382236056850433', 'post /authbridge', 'permission:authbr', 'com.hd.microauservice.controller.AuthenticationController:authbridge', null);
INSERT INTO `sy_url_mapping` VALUES ('1413382236363034625', 'get /test1', 'micro:test1', 'com.hd.microauservice.controller.MicroServiceController:test', null);
INSERT INTO `sy_url_mapping` VALUES ('1413382236677607426', 'get /test2', 'micro:test2', 'com.hd.microauservice.controller.MicroServiceController:test2', null);

-- ----------------------------
-- Table structure for sy_user
-- ----------------------------
DROP TABLE IF EXISTS `sy_user`;
CREATE TABLE `sy_user` (
  `id` bigint(20) NOT NULL,
  `org_id` bigint(20) DEFAULT NULL COMMENT '所属部门',
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '1:启用,0:停用',
  `account` varchar(100) NOT NULL COMMENT '登录账户',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `password_hash` varchar(200) DEFAULT NULL COMMENT '密码hash值',
  `salt` varchar(100) DEFAULT NULL COMMENT 'hash key',
  `phone` varchar(100) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机',
  `user_type` smallint(6) DEFAULT '0' COMMENT '0：系统配置员；1：企业管理员；10：企业员工；数据字典',
  `job_` varchar(30) DEFAULT NULL COMMENT '职务',
  `modified_time` datetime DEFAULT NULL COMMENT '密码修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_user
-- ----------------------------

-- ----------------------------
-- Table structure for sy_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sy_user_role`;
CREATE TABLE `sy_user_role` (
  `id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_user_role
-- ----------------------------
