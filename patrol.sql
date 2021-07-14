/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1_3333
Source Server Version : 80011
Source Host           : 127.0.0.1:3333
Source Database       : patrol

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2021-07-14 11:00:24
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
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `level_code` char(3) DEFAULT NULL COMMENT '本级编号',
  `path_code` char(128) DEFAULT NULL COMMENT '树形编号',
  `type` tinyint(1) DEFAULT NULL COMMENT '0:目录；1:功能',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父id',
  `note` varchar(250) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_function
-- ----------------------------
INSERT INTO `sy_function` VALUES ('1414763942085136385', '菜单管理', '100', '100.100', '1', '1414764039753699330', null);
INSERT INTO `sy_function` VALUES ('1414764039753699330', '系统管理', '100', '100', '0', null, null);

-- ----------------------------
-- Table structure for sy_func_operator
-- ----------------------------
DROP TABLE IF EXISTS `sy_func_operator`;
CREATE TABLE `sy_func_operator` (
  `id` bigint(20) NOT NULL,
  `func_id` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_func_operator
-- ----------------------------
INSERT INTO `sy_func_operator` VALUES ('1414767899385794561', '1414763942085136385', 'list', '页面list操作');

-- ----------------------------
-- Table structure for sy_func_op_url
-- ----------------------------
DROP TABLE IF EXISTS `sy_func_op_url`;
CREATE TABLE `sy_func_op_url` (
  `id` bigint(20) NOT NULL,
  `url` varchar(250) DEFAULT NULL COMMENT 'url地址',
  `perm_code` varchar(100) DEFAULT NULL COMMENT 'controller的className.method',
  `func_op_id` bigint(20) DEFAULT NULL COMMENT '功能id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_func_op_url
-- ----------------------------
INSERT INTO `sy_func_op_url` VALUES ('1414776181965983746', 'post /authbridge', 'permission:authbr', '1414767899385794561');
INSERT INTO `sy_func_op_url` VALUES ('1414776181965983747', 'post /authbridge', 'permission:authbr', '1414767899385794561');

-- ----------------------------
-- Table structure for sy_menu
-- ----------------------------
DROP TABLE IF EXISTS `sy_menu`;
CREATE TABLE `sy_menu` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `type` tinyint(1) DEFAULT NULL COMMENT '0:目录；1:菜单；',
  `level_code` char(3) DEFAULT NULL COMMENT '本级编号',
  `path_code` char(128) DEFAULT NULL COMMENT '树形编号',
  `enabled` int(11) DEFAULT NULL COMMENT '状态,0:停用，1：启用',
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
INSERT INTO `sy_menu` VALUES ('1414465565539700737', null, 'root', '0', '100', '100', '1', '系统管理', null, null, 'fa fa-map-o', '1');
INSERT INTO `sy_menu` VALUES ('1414465743558545409', '1414465565539700737', 'root', '1', '100', '100.100', '1', '菜单管理', '/system/menu', null, 'fa fa-map-o', '1');
INSERT INTO `sy_menu` VALUES ('1414467899229474818', '1414465565539700737', 'root', '1', '300', '100.200', '1', '功能管理', '/system/function', '444', 'fa fa-map-o', '1');
INSERT INTO `sy_menu` VALUES ('1415133178829737986', null, 'root', '0', '200', null, null, '业务管理', null, null, 'fa fa-wrench', null);

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
INSERT INTO `sy_menu_btn` VALUES ('1414474794170322946', 'root', '1414465743558545409', '1', null, '添加菜单', 'btn_addMenu', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1414475159779414017', 'root', '1414465743558545409', '1', null, '编辑菜单', 'btn_editMenu', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1414475219313364993', 'root', '1414465743558545409', '1', null, '删除菜单', 'btn_delMenu', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1414475279249969154', 'root', '1414465743558545409', '1', null, '添加按钮', 'btn_addBtn', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1414475326389751809', 'root', '1414465743558545409', '1', null, '编辑按钮', 'btn_editBtn', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1414475363865858050', 'root', '1414465743558545409', '1', null, '删除按钮', 'btn_delBtn', null, null, '1');

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
  `type` tinyint(1) DEFAULT '0' COMMENT '类别{0：组织；1:部门}',
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
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `menu_btn_id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
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
INSERT INTO `sy_url_mapping` VALUES ('1414855373747392513', 'post /authbridge', 'permission:authbr', 'com.hd.microauservice.controller.AuthenticationController:authbridge', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855374015827970', 'post /auth', 'permission:auth', 'com.hd.microauservice.controller.AuthenticationController:auth', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855374326206465', 'get /url-mapping', 'urlMaping:get', 'com.hd.microauservice.controller.FunctionController:getUrlMapping', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855374640779265', 'get /function', 'func:get', 'com.hd.microauservice.controller.FunctionController:getfunction', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855374946963458', 'put /function/{funcId}', 'func:edit', 'com.hd.microauservice.controller.FunctionController:editFunc', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855375257341954', 'delete /function/{funcId}', 'func:del', 'com.hd.microauservice.controller.FunctionController:delFunc', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855375563526146', 'post /function/opr', 'funcOpr:create', 'com.hd.microauservice.controller.FunctionController:createFuncOpr', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855375873904641', 'post /function', 'func:create', 'com.hd.microauservice.controller.FunctionController:createFunc', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855376184283137', 'put /function/opr/{funcOprId}', 'funcOpr:edit', 'com.hd.microauservice.controller.FunctionController:editFuncOpr', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855376494661633', 'delete /function/opr/{funcOprId}', 'funcOpr:del', 'com.hd.microauservice.controller.FunctionController:delFuncOpr', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855376805040129', 'get /function/opr/{funcOprId}/url', 'funcOpUrl:get', 'com.hd.microauservice.controller.FunctionController:getfunOpUrl', '获取某个操作对应的Uri');
INSERT INTO `sy_url_mapping` VALUES ('1414855377115418626', 'put /function/opr/{funcOprId}/url', 'funcOpUrl:update', 'com.hd.microauservice.controller.FunctionController:updatefunOpUrl', '更新某个操作对应的Uri');
INSERT INTO `sy_url_mapping` VALUES ('1414855377329328129', 'get /enterprise/{enterCode}/menu', 'enterprise:menu', 'com.hd.microauservice.controller.MenuController:getEnterpriseMenu', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855377635512321', 'get /menu/my', 'menu:my', 'com.hd.microauservice.controller.MenuController:getMyMenu', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855377945890817', 'delete /menu/btn/{id}', 'menuBtn:del', 'com.hd.microauservice.controller.MenuController:delMenuBtn', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855378260463617', 'post /menu', 'menu:create', 'com.hd.microauservice.controller.MenuController:createMenu', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855378566647809', 'put /menu/{id}', 'menu:edit', 'com.hd.microauservice.controller.MenuController:editMenu', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855378877026305', 'put /menu/btn/{id}', 'menuBtn:edit', 'com.hd.microauservice.controller.MenuController:editMenuBtn', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855379086741506', 'delete /menu/{id}', 'menu:del', 'com.hd.microauservice.controller.MenuController:delMenu', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855379392925697', 'post /menu/btn', 'menuBtn:create', 'com.hd.microauservice.controller.MenuController:createMenuBtn', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855379703304194', 'post /user', 'user:create', 'com.hd.microauservice.controller.UserController:createUser', '');
INSERT INTO `sy_url_mapping` VALUES ('1414855380017876994', 'get /cur-user/info', 'curUser:info', 'com.hd.microauservice.controller.UserController:getCurrentUser', '');

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
  `phone` varchar(100) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机',
  `job_name` varchar(30) DEFAULT NULL COMMENT '职务',
  `modified_time` datetime DEFAULT NULL COMMENT '密码修改时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_user
-- ----------------------------
INSERT INTO `sy_user` VALUES ('1414876928980291585', null, 'root', '1', 'testuser', null, null, null, null, null, null, '1');
INSERT INTO `sy_user` VALUES ('1414877333420249090', null, 'root', '1', 'testuser', null, null, null, null, null, null, '1');
INSERT INTO `sy_user` VALUES ('1414886317858492418', null, 'root', '1', 'liwei', null, null, null, null, null, null, '0');
INSERT INTO `sy_user` VALUES ('1414886757534797825', null, 'root', '1', 'testuser', null, null, null, null, null, null, '1');
INSERT INTO `sy_user` VALUES ('1414886873280811010', null, 'root', '1', 'testuser', null, null, null, null, null, null, '1');
INSERT INTO `sy_user` VALUES ('1414886958668451841', null, 'root', '1', 'testuser', null, null, null, null, null, null, '1');
INSERT INTO `sy_user` VALUES ('1414893218583285761', null, 'root', '1', 'testuser', null, null, null, null, null, null, '0');

-- ----------------------------
-- Table structure for sy_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sy_user_role`;
CREATE TABLE `sy_user_role` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_user_role
-- ----------------------------
