/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1_3333
Source Server Version : 80011
Source Host           : 127.0.0.1:3333
Source Database       : patrol

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2021-10-15 14:44:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL,
  `enterprise` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `delete_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('8888888888888888888', 'root', 'root', '$2a$10$rzRIFc9twR/hZVbTHl4fm.zxIGb.9jC6VQ1IVSLufpj0DZMq5pQBm', '2021-07-19 15:01:56', '0');

-- ----------------------------
-- Table structure for sys_upgrade
-- ----------------------------
DROP TABLE IF EXISTS `sys_upgrade`;
CREATE TABLE `sys_upgrade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attach_id` bigint(20) NOT NULL COMMENT '附件id',
  `type` tinyint(4) NOT NULL COMMENT '应用类型 ',
  `version` int(11) NOT NULL COMMENT '版本号',
  `version_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '版本名',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '升级说明',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=240 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_upgrade
-- ----------------------------

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
  `content_type` varchar(50) DEFAULT NULL COMMENT '文件mimetype',
  `file_new_name` varchar(200) DEFAULT NULL COMMENT '路径',
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_unique` (`enterprise_id`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_dict
-- ----------------------------
INSERT INTO `sy_dict` VALUES ('1448101688753131521', 'root', 'admin_role', '0', '1', null, '管理员角色列表', null);
INSERT INTO `sy_dict` VALUES ('1448101885646344193', 'root', 'shy_role', '0', '1', null, '审核员角色列表', null);

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_unique` (`dict_id`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_dict_item
-- ----------------------------
INSERT INTO `sy_dict_item` VALUES ('1448101748224167938', '1448101688753131521', '1', '1', '管理员', '管理员');
INSERT INTO `sy_dict_item` VALUES ('1448102005674741761', '1448101885646344193', '1', '1', '计划员', '计划员');

-- ----------------------------
-- Table structure for sy_enterprise
-- ----------------------------
DROP TABLE IF EXISTS `sy_enterprise`;
CREATE TABLE `sy_enterprise` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '企业编号(s00000：特殊企业，系统配置员使用关联菜单；s00001：特殊企业，企业管理员使用关联菜单)',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `note` varchar(250) DEFAULT NULL COMMENT '描述',
  `delete_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `enter_unique` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_enterprise
-- ----------------------------
INSERT INTO `sy_enterprise` VALUES ('8888888888888888888', 'root', 'root企业', 'root企业', '0');

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
INSERT INTO `sy_function` VALUES ('1430722653278441474', '所有功能', '999', '999.999', '1', '1431193490385276930', null);
INSERT INTO `sy_function` VALUES ('1431193490385276930', 'root专用', '999', '999', '0', null, null);
INSERT INTO `sy_function` VALUES ('1438770712080158721', '服务监视', '100', '100', '0', null, '服务监视');
INSERT INTO `sy_function` VALUES ('1438771927883059201', '系统管理', '200', '200', '0', null, '系统管理');
INSERT INTO `sy_function` VALUES ('1438772125371863042', '配置管理', '210', '210', '0', null, '配置管理');
INSERT INTO `sy_function` VALUES ('1438772388421832706', '日常维护', '400', '400', '0', null, '日常维护');
INSERT INTO `sy_function` VALUES ('1438772452099756033', '巡检任务', '500', '500', '0', null, '巡检任务');
INSERT INTO `sy_function` VALUES ('1438772561843720194', '执行结果反馈', '600', '600', '0', null, '执行结果反馈');
INSERT INTO `sy_function` VALUES ('1438772639585144834', '监控通知', '700', '700', '0', null, '监控通知');
INSERT INTO `sy_function` VALUES ('1438772702118023169', '统计分析', '800', '800', '0', null, '统计分析');
INSERT INTO `sy_function` VALUES ('1438773045216284674', '服务在线状态', '100', '100.100', '1', '1438770712080158721', '服务监视-服务在线状态');
INSERT INTO `sy_function` VALUES ('1438773235247616001', '请求访问记录', '200', '100.200', '1', '1438770712080158721', '服务监视-请求访问记录');
INSERT INTO `sy_function` VALUES ('1438773441963888642', '请求耗时统计', '300', '100.300', '1', '1438770712080158721', '服务监视-请求耗时统计');
INSERT INTO `sy_function` VALUES ('1438773664870174722', '角色管理', '100', '200.100', '1', '1438771927883059201', '系统管理-角色管理');
INSERT INTO `sy_function` VALUES ('1438773817010163714', '部门管理', '200', '200.200', '1', '1438771927883059201', '系统管理-部门管理');
INSERT INTO `sy_function` VALUES ('1438773989681270785', '人员管理', '300', '200.300', '1', '1438771927883059201', '系统管理-功能管理');
INSERT INTO `sy_function` VALUES ('1438774116231811074', '字典管理', '400', '200.400', '1', '1438771927883059201', '系统管理-功能管理');
INSERT INTO `sy_function` VALUES ('1438774247517720578', '菜单管理', '100', '210.100', '1', '1438772125371863042', '配置管理-菜单管理');
INSERT INTO `sy_function` VALUES ('1438774372428288002', '功能管理', '200', '210.200', '1', '1438772125371863042', '配置管理-功能管理');
INSERT INTO `sy_function` VALUES ('1438774490938347521', '企业管理', '300', '210.300', '1', '1438772125371863042', '配置管理-功能管理');
INSERT INTO `sy_function` VALUES ('1438774653190803457', '巡检点管理', '100', '400.100', '1', '1438772388421832706', '日常维护-巡检点管理');
INSERT INTO `sy_function` VALUES ('1438774796287873026', '巡检项管理', '200', '400.200', '1', '1438772388421832706', '日常维护-巡检项管理');
INSERT INTO `sy_function` VALUES ('1438774946062274562', '工单模板', '300', '400.300', '1', '1438772388421832706', '日常维护-工单模板');
INSERT INTO `sy_function` VALUES ('1438775127260401666', '巡检计划', '100', '500.100', '1', '1438772452099756033', '巡检任务-巡检计划');
INSERT INTO `sy_function` VALUES ('1438775277324210178', '创建工单', '200', '500.200', '1', '1438772452099756033', '巡检任务-创建工单');
INSERT INTO `sy_function` VALUES ('1438775684129755137', '巡检单管理', '100', '600.100', '1', '1438772561843720194', '执行结果反馈-巡检单管理');
INSERT INTO `sy_function` VALUES ('1438775795312365569', '工单管理', '200', '600.200', '1', '1438772561843720194', '执行结果反馈-工单管理');
INSERT INTO `sy_function` VALUES ('1438776172413849601', '隐患报告', '300', '600.300', '1', '1438772561843720194', '执行结果反馈-隐患报告');
INSERT INTO `sy_function` VALUES ('1438776384071012354', '报警信息', '400', '600.400', '1', '1438772561843720194', '执行结果反馈-报警信息');
INSERT INTO `sy_function` VALUES ('1438776528707391490', '数据采集', '500', '600.500', '1', '1438772561843720194', '执行结果反馈-数据采集');
INSERT INTO `sy_function` VALUES ('1438776652892344322', '通知管理', '100', '700.100', '1', '1438772639585144834', '监控通知-通知管理');
INSERT INTO `sy_function` VALUES ('1438776793045012481', '统计分析首页', '100', '800.100', '1', '1438772702118023169', '统计分析-统计分析首页');
INSERT INTO `sy_function` VALUES ('1438777549928140801', '巡检单执行效率', '200', '800.200', '1', '1438772702118023169', '统计分析-巡检单执行效率');
INSERT INTO `sy_function` VALUES ('1438777669012819969', '工单执行效率', '300', '800.300', '1', '1438772702118023169', '统计分析-工单执行效率');
INSERT INTO `sy_function` VALUES ('1438778081535201281', '巡检点漏检统计', '400', '800.400', '1', '1438772702118023169', '统计分析-巡检点漏检统计');
INSERT INTO `sy_function` VALUES ('1438778187873390593', '隐患统计', '500', '800.500', '1', '1438772702118023169', '统计分析-隐患统计');
INSERT INTO `sy_function` VALUES ('1438778306995818497', '数据采集统计', '600', '800.600', '1', '1438772702118023169', '统计分析-数据采集统计');

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
INSERT INTO `sy_func_operator` VALUES ('1438780044242980866', '1438773664870174722', '添加', '添加角色');
INSERT INTO `sy_func_operator` VALUES ('1438782766954450945', '1438773664870174722', '编辑', '编辑角色');
INSERT INTO `sy_func_operator` VALUES ('1438782888853508098', '1438773664870174722', '删除', '删除角色');
INSERT INTO `sy_func_operator` VALUES ('1438783082198339586', '1438773817010163714', '添加', '添加部门');
INSERT INTO `sy_func_operator` VALUES ('1438783208878903297', '1438773817010163714', '编辑', '编辑部门');
INSERT INTO `sy_func_operator` VALUES ('1438783393197592578', '1438773817010163714', '删除', '删除部门');
INSERT INTO `sy_func_operator` VALUES ('1438783471136149506', '1438773989681270785', '添加', '添加人员');
INSERT INTO `sy_func_operator` VALUES ('1438783580674592770', '1438773989681270785', '编辑', '编辑人员');
INSERT INTO `sy_func_operator` VALUES ('1438783712459624449', '1438773989681270785', '删除', '删除人员');
INSERT INTO `sy_func_operator` VALUES ('1438783862942863362', '1438774116231811074', '添加字典', '添加字典');
INSERT INTO `sy_func_operator` VALUES ('1438783968639324161', '1438774116231811074', '编辑字典', '编辑字典');
INSERT INTO `sy_func_operator` VALUES ('1438784055213953025', '1438774116231811074', '删除字典', '删除字典');
INSERT INTO `sy_func_operator` VALUES ('1438808460287676417', '1438773664870174722', '配置权限', '配置权限');

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
  `enabled` tinyint(1) DEFAULT '1' COMMENT '状态,0:停用，1：启用',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `url` varchar(250) DEFAULT NULL COMMENT '相关url',
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  `icon_class` varchar(100) DEFAULT NULL COMMENT '图标样式',
  `is_visible` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `path_code_unique` (`path_code`,`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每项具备url的菜单都有一个隐含的menu_btn，代表查看本页面，使sy_role_perm表统一关联到sy_menu_btn表';

-- ----------------------------
-- Records of sy_menu
-- ----------------------------
INSERT INTO `sy_menu` VALUES ('1438740455029608449', null, 'root', '0', '200', '200', '1', '系统管理', null, '系统管理', 'fa fa-map-o', '1');
INSERT INTO `sy_menu` VALUES ('1438741597226668034', null, 'root', '0', '100', '100', '1', '服务监视', '', '服务监视', 'fa fa-heart', '1');
INSERT INTO `sy_menu` VALUES ('1438742446384484354', null, 'root', '0', '300', '300', '1', '日常维护', null, '日常维护', 'fa fa-wrench', '1');
INSERT INTO `sy_menu` VALUES ('1438742953027047425', null, 'root', '0', '400', '400', '1', '巡检任务', null, '巡检任务', 'fa fa-blind', '1');
INSERT INTO `sy_menu` VALUES ('1438751878354964482', '1438741597226668034', 'root', '1', '100', '100.100', '1', '服务在线状态', '/system/heartbeat', '服务监视-服务在线状态', 'fa fa-heartbeat', '1');
INSERT INTO `sy_menu` VALUES ('1438753163363880961', '1438741597226668034', 'root', '1', '200', '100.200', '1', '请求访问记录', '/system/lastreq', '服务监视-请求访问状态', 'fa fa-list-ol', '1');
INSERT INTO `sy_menu` VALUES ('1438753502137815042', '1438741597226668034', 'root', '1', '300', '100.300', '1', '请求耗时统计', '/system/lastreqavg', '服务监视-请求耗时统计', 'fa fa-clock-o', '1');
INSERT INTO `sy_menu` VALUES ('1438755293973843970', '1438740455029608449', 'root', '1', '100', '200.100', '1', '角色管理', '/system/role', '系统管理-角色管理', 'fa fa-child', '1');
INSERT INTO `sy_menu` VALUES ('1438755558001086465', '1438740455029608449', 'root', '1', '200', '200.200', '1', '部门管理', '/system/dept', '系统管理-部门管理', 'fa fa-group (alias)', '1');
INSERT INTO `sy_menu` VALUES ('1438755763178049538', '1438740455029608449', 'root', '1', '300', '200.300', '1', '人员管理', '/system/user', '系统管理-人员管理', 'fa fa-user', '1');
INSERT INTO `sy_menu` VALUES ('1438756009941536769', '1438740455029608449', 'root', '1', '400', '200.400', '1', '字典管理', '/system/dict', '系统管理-字典管理', 'fa fa-folder-open-o', '1');
INSERT INTO `sy_menu` VALUES ('1438757050464473089', '1438742446384484354', 'root', '1', '100', '300.100', '1', '巡检点管理', '/business/checkPoint', '日常维护-巡检点管理', 'fa fa-dot-circle-o', '1');
INSERT INTO `sy_menu` VALUES ('1438757420565663745', '1438742446384484354', 'root', '1', '200', '300.200', '1', '巡检项管理', '/business/checkItem', '日常维护-巡检项管理', 'fa fa-check-square', '1');
INSERT INTO `sy_menu` VALUES ('1438758102119092225', '1438742446384484354', 'root', '1', '300', '300.300', '1', '工单模板', '/business/repairTemplate', '日常维护-工单模板', 'fa fa-leanpub', '1');
INSERT INTO `sy_menu` VALUES ('1438758501395861506', '1438742953027047425', 'root', '1', '100', '400.100', '1', '巡检计划', '/business/inspectionPlan', '巡检任务-巡检计划', 'fa fa-binoculars', '1');
INSERT INTO `sy_menu` VALUES ('1438758814479683586', '1438742953027047425', 'root', '1', '200', '400.200', '1', '创建工单', '/business/createOrder', '巡检任务-创建工单', 'fa fa-calendar-plus-o', '1');
INSERT INTO `sy_menu` VALUES ('1438759645761376258', null, 'root', '0', '500', '500', '1', '执行结果反馈', null, '执行结果反馈', 'fa fa-flag', '1');
INSERT INTO `sy_menu` VALUES ('1438759927752822786', '1438759645761376258', 'root', '1', '100', '500.100', '1', '巡检单管理', '/executeBack/inspectionList', '执行结果反馈-巡检单管理', 'fa fa-calendar-check-o', '1');
INSERT INTO `sy_menu` VALUES ('1438760304812363777', '1438759645761376258', 'root', '1', '200', '500.200', '1', '工单管理', '/executeBack/workList', '执行结果反馈-工单管理', 'fa fa-newspaper-o', '1');
INSERT INTO `sy_menu` VALUES ('1438760635050889218', '1438759645761376258', 'root', '1', '300', '500.300', '1', '隐患报告', '/executeBack/troubleReport', '执行结果反馈-隐患报告', 'fa fa-exclamation-triangle', '1');
INSERT INTO `sy_menu` VALUES ('1438761114493390849', '1438759645761376258', 'root', '1', '400', '500.400', '1', '报警信息', '/executeBack/callPolice', '执行结果反馈-报警信息', 'fa fa-comments-o', '1');
INSERT INTO `sy_menu` VALUES ('1438761365371490306', '1438759645761376258', 'root', '1', '500', '500.500', '1', '数据采集', '/executeBack/collectData', '执行结果反馈-数据采集', 'fa fa-cloud-upload', '1');
INSERT INTO `sy_menu` VALUES ('1438762552967696385', null, 'root', '0', '600', '600', '1', '监控通知', null, '监控通知', 'fa fa-recycle', '1');
INSERT INTO `sy_menu` VALUES ('1438762747356909570', '1438762552967696385', 'root', '1', '100', '600.100', '1', '通知管理', '/executeBack/infoManagement', '监控通知-通知管理', 'fa fa-volume-up', '1');
INSERT INTO `sy_menu` VALUES ('1438763102379577346', null, 'root', '0', '700', '700', '1', '统计分析', null, '统计分析', 'fa fa-bar-chart', '1');
INSERT INTO `sy_menu` VALUES ('1438763393913065474', '1438763102379577346', 'root', '1', '100', '700.100', '1', '统计分析首页', '/business/mainAnaly', '统计分析-统计分析首页', 'fa fa-tv (alias)', '1');
INSERT INTO `sy_menu` VALUES ('1438763750336630785', '1438763102379577346', 'root', '1', '200', '700.200', '1', '巡检单执行效率', '/business/inspectAnaly', '统计分析-巡检单执行效率', 'fa fa-line-chart', '1');
INSERT INTO `sy_menu` VALUES ('1438764022412742658', '1438763102379577346', 'root', '1', '300', '700.300', '1', '工单执行效率', '/business/orderAnaly', '统计分析-工单执行效率', 'fa fa-pie-chart', '1');
INSERT INTO `sy_menu` VALUES ('1438764295264800770', '1438763102379577346', 'root', '1', '400', '700.400', '1', '巡检点漏检统计', '/business/testerAnaly', '统计分析-巡检点漏检统计', 'fa fa-bullhorn', '1');
INSERT INTO `sy_menu` VALUES ('1438764624744157185', '1438763102379577346', 'root', '1', '500', '700.500', '1', '隐患统计', '/business/troubleAnaly', '统计分析-隐患统计', 'fa fa-gavel', '1');
INSERT INTO `sy_menu` VALUES ('1438764933709172737', '1438763102379577346', 'root', '1', '600', '700.600', '1', '数据采集统计', '/business/customAnaly', '统计分析-数据采集统计', 'fa fa-hand-pointer-o', '1');
INSERT INTO `sy_menu` VALUES ('1438767963997343746', null, 'root', '0', '210', '210', '1', '配置管理', null, '配置管理', 'fa fa-cog', '1');
INSERT INTO `sy_menu` VALUES ('1438768991840899074', '1438767963997343746', 'root', '1', '100', '210.100', '1', '菜单管理', '/system/menu', '配置管理', 'fa fa-folder-open-o', '1');
INSERT INTO `sy_menu` VALUES ('1438769391335772162', '1438767963997343746', 'root', '1', '200', '210.200', '1', '功能管理', '/system/function', '配置管理-功能管理', 'fa fa-cube', '1');
INSERT INTO `sy_menu` VALUES ('1438769751471296513', '1438767963997343746', 'root', '1', '300', '210.300', '1', '企业管理', '/system/company', '配置管理-企业管理', 'fa fa-bank (alias)', '1');

-- ----------------------------
-- Table structure for sy_menu_btn
-- ----------------------------
DROP TABLE IF EXISTS `sy_menu_btn`;
CREATE TABLE `sy_menu_btn` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL COMMENT '关联菜单id',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '状态{1:启用,0:停用}',
  `func_op_id` bigint(20) DEFAULT NULL COMMENT '关联操作id',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `html_id` varchar(50) DEFAULT NULL,
  `jshandler` varchar(200) DEFAULT NULL COMMENT 'click handler',
  `icon_class` varchar(100) DEFAULT NULL COMMENT '图标样式',
  `is_visible` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_menu_btn
-- ----------------------------
INSERT INTO `sy_menu_btn` VALUES ('1438751878354964483', 'root', '1438751878354964482', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438753163372269569', 'root', '1438753163363880961', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438753502146203650', 'root', '1438753502137815042', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438755293982232578', 'root', '1438755293973843970', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438755558013669378', 'root', '1438755558001086465', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438755763178049539', 'root', '1438755763178049538', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438756009949925378', 'root', '1438756009941536769', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438757050464473090', 'root', '1438757050464473089', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438757420565663746', 'root', '1438757420565663745', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438758102127480833', 'root', '1438758102119092225', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438758501404250113', 'root', '1438758501395861506', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438758814488072194', 'root', '1438758814479683586', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438759927761211394', 'root', '1438759927752822786', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438760304820752385', 'root', '1438760304812363777', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438760635059277826', 'root', '1438760635050889218', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438761114501779458', 'root', '1438761114493390849', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438761365379878913', 'root', '1438761365371490306', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438762747365298177', 'root', '1438762747356909570', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438763393913065475', 'root', '1438763393913065474', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438763750340825090', 'root', '1438763750336630785', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438764022450491394', 'root', '1438764022412742658', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438764295285772289', 'root', '1438764295264800770', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438764624748351489', 'root', '1438764624744157185', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438764933709172738', 'root', '1438764933709172737', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438768991849287681', 'root', '1438768991840899074', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438769391352549377', 'root', '1438769391335772162', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438769751479685122', 'root', '1438769751471296513', '1', null, 'list', null, null, null, '0');
INSERT INTO `sy_menu_btn` VALUES ('1438781802478440449', 'root', '1438755293973843970', '1', '1438780044242980866', '添加角色', 'add_role', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438784984118071298', 'root', '1438755293973843970', '1', '1438782766954450945', '编辑角色', 'edit_role', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438785569189924865', 'root', '1438755293973843970', '1', '1438782888853508098', '删除角色', 'del_role', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438786049622282241', 'root', '1438755558001086465', '1', '1438783082198339586', '添加部门', 'add_dept', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438786136536649729', 'root', '1438755558001086465', '1', '1438783208878903297', '编辑部门', 'edit_dept', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438786245383032833', 'root', '1438755558001086465', '1', '1438783393197592578', '删除部门', 'del_dept', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438786385015607298', 'root', '1438755763178049538', '1', '1438783471136149506', '添加人员', 'add_user', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438786483086823425', 'root', '1438755763178049538', '1', '1438783580674592770', '编辑人员', 'edit_user', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438786648694722561', 'root', '1438755763178049538', '1', '1438783712459624449', '删除人员', 'del_user', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438807857008349185', 'root', '1438756009941536769', '1', '1438783862942863362', '新增字典', 'add_dict', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438807947689201666', 'root', '1438756009941536769', '1', '1438783968639324161', '编辑字典', 'edit_dict', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438808049594011650', 'root', '1438756009941536769', '1', '1438784055213953025', '删除字典', 'del_dict', null, null, '1');
INSERT INTO `sy_menu_btn` VALUES ('1438808808112918530', 'root', '1438755293973843970', '1', '1438808460287676417', '配置权限', 'per_role', null, null, '1');

-- ----------------------------
-- Table structure for sy_monitor
-- ----------------------------
DROP TABLE IF EXISTS `sy_monitor`;
CREATE TABLE `sy_monitor` (
  `id` bigint(20) NOT NULL,
  `service_name` varchar(100) NOT NULL,
  `heartbeat_tm` datetime NOT NULL,
  `show_name` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_monitor
-- ----------------------------
INSERT INTO `sy_monitor` VALUES ('1', 'microsys', '2021-10-09 09:46:30', '系统基础微服务', null);
INSERT INTO `sy_monitor` VALUES ('2', 'gateway', '2021-10-09 09:46:30', '网关服务', null);
INSERT INTO `sy_monitor` VALUES ('3', 'auserver', '2021-10-09 09:46:30', '认证服务', null);
INSERT INTO `sy_monitor` VALUES ('4', 'micromonitor', '2021-10-09 09:46:30', '监视服务', null);
INSERT INTO `sy_monitor` VALUES ('5', 'basicconfig', '2021-10-09 11:06:04', '业务基础微服务', null);
INSERT INTO `sy_monitor` VALUES ('6', 'execute', '2021-10-09 11:06:37', '业务执行微服务', null);
INSERT INTO `sy_monitor` VALUES ('7', 'reportnews', '2021-10-09 11:07:05', '业务报告微服务', null);
INSERT INTO `sy_monitor` VALUES ('8', 'task', '2021-09-08 17:50:47', '定时任务微服务', null);
INSERT INTO `sy_monitor` VALUES ('1448830614307606530', 'gateway', '2021-10-15 10:33:30', '网关服务', 'http://192.168.3.50:20001');
INSERT INTO `sy_monitor` VALUES ('1448830614307606531', 'auserver', '2021-10-15 10:33:30', '认证服务', 'http://192.168.3.50:10001');
INSERT INTO `sy_monitor` VALUES ('1448830614307606532', 'microsys', '2021-10-15 10:33:30', '系统基础微服务', 'http://192.168.3.50:20002');
INSERT INTO `sy_monitor` VALUES ('1448830616480256002', 'micromonitor', '2021-10-15 10:33:30', '监视服务', 'http://192.168.3.50:20003');

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
  `delete_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `path_code_unique` (`enterprise_id`,`path_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_org
-- ----------------------------
INSERT INTO `sy_org` VALUES ('8888888888888888888', null, 'root', '100', '100', '0', '1', '超级公司', '超级', null, null, '0');

-- ----------------------------
-- Table structure for sy_role
-- ----------------------------
DROP TABLE IF EXISTS `sy_role`;
CREATE TABLE `sy_role` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `enabled` int(11) DEFAULT '1' COMMENT '状态{1：启用，0：停用}',
  `sort_num` int(11) DEFAULT '0' COMMENT '排序号',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `data_privilege` tinyint(1) DEFAULT NULL,
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_role
-- ----------------------------
INSERT INTO `sy_role` VALUES ('1438815835165954050', 'root', '1', '2', '管理员', '0', null);
INSERT INTO `sy_role` VALUES ('8888888888888888888', 'root', '1', '1', '超级root', '0', null);

-- ----------------------------
-- Table structure for sy_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `sy_role_perm`;
CREATE TABLE `sy_role_perm` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `menu_btn_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_role_perm
-- ----------------------------
INSERT INTO `sy_role_perm` VALUES ('1438815450078515202', '8888888888888888888', '1438751878354964483');
INSERT INTO `sy_role_perm` VALUES ('1438815450078515203', '8888888888888888888', '1438753163372269569');
INSERT INTO `sy_role_perm` VALUES ('1438815450078515204', '8888888888888888888', '1438753502146203650');
INSERT INTO `sy_role_perm` VALUES ('1438815450078515205', '8888888888888888888', '1438755293982232578');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903810', '8888888888888888888', '1438781802478440449');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903811', '8888888888888888888', '1438784984118071298');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903812', '8888888888888888888', '1438785569189924865');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903813', '8888888888888888888', '1438808808112918530');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903814', '8888888888888888888', '1438755558013669378');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903815', '8888888888888888888', '1438786049622282241');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903816', '8888888888888888888', '1438786136536649729');
INSERT INTO `sy_role_perm` VALUES ('1438815450086903817', '8888888888888888888', '1438786245383032833');
INSERT INTO `sy_role_perm` VALUES ('1438815450095292418', '8888888888888888888', '1438755763178049539');
INSERT INTO `sy_role_perm` VALUES ('1438815450095292419', '8888888888888888888', '1438786385015607298');
INSERT INTO `sy_role_perm` VALUES ('1438815450095292420', '8888888888888888888', '1438786483086823425');
INSERT INTO `sy_role_perm` VALUES ('1438815450095292421', '8888888888888888888', '1438786648694722561');
INSERT INTO `sy_role_perm` VALUES ('1438815450095292422', '8888888888888888888', '1438756009949925378');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486721', '8888888888888888888', '1438807857008349185');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486722', '8888888888888888888', '1438807947689201666');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486723', '8888888888888888888', '1438808049594011650');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486724', '8888888888888888888', '1438768991849287681');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486725', '8888888888888888888', '1438769391352549377');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486726', '8888888888888888888', '1438769751479685122');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486727', '8888888888888888888', '1438757050464473090');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486728', '8888888888888888888', '1438757420565663746');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486729', '8888888888888888888', '1438758102127480833');
INSERT INTO `sy_role_perm` VALUES ('1438815450099486730', '8888888888888888888', '1438758501404250113');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875329', '8888888888888888888', '1438758814488072194');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875330', '8888888888888888888', '1438759927761211394');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875331', '8888888888888888888', '1438760304820752385');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875332', '8888888888888888888', '1438760635059277826');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875333', '8888888888888888888', '1438761114501779458');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875334', '8888888888888888888', '1438761365379878913');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875335', '8888888888888888888', '1438762747365298177');
INSERT INTO `sy_role_perm` VALUES ('1438815450107875336', '8888888888888888888', '1438763393913065475');
INSERT INTO `sy_role_perm` VALUES ('1438815450116263937', '8888888888888888888', '1438763750340825090');
INSERT INTO `sy_role_perm` VALUES ('1438815450116263938', '8888888888888888888', '1438764022450491394');
INSERT INTO `sy_role_perm` VALUES ('1438815450116263939', '8888888888888888888', '1438764295285772289');
INSERT INTO `sy_role_perm` VALUES ('1438815450116263940', '8888888888888888888', '1438764624748351489');
INSERT INTO `sy_role_perm` VALUES ('1438815450116263941', '8888888888888888888', '1438764933709172738');
INSERT INTO `sy_role_perm` VALUES ('1438815921023356930', '1438815835165954050', '1438755293982232578');
INSERT INTO `sy_role_perm` VALUES ('1438815921023356931', '1438815835165954050', '1438781802478440449');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745538', '1438815835165954050', '1438784984118071298');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745539', '1438815835165954050', '1438785569189924865');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745540', '1438815835165954050', '1438808808112918530');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745541', '1438815835165954050', '1438755558013669378');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745542', '1438815835165954050', '1438786049622282241');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745543', '1438815835165954050', '1438786136536649729');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745544', '1438815835165954050', '1438786245383032833');
INSERT INTO `sy_role_perm` VALUES ('1438815921031745545', '1438815835165954050', '1438755763178049539');
INSERT INTO `sy_role_perm` VALUES ('1438815921040134145', '1438815835165954050', '1438786385015607298');
INSERT INTO `sy_role_perm` VALUES ('1438815921040134146', '1438815835165954050', '1438786483086823425');
INSERT INTO `sy_role_perm` VALUES ('1438815921040134147', '1438815835165954050', '1438786648694722561');
INSERT INTO `sy_role_perm` VALUES ('1438815921040134148', '1438815835165954050', '1438756009949925378');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328450', '1438815835165954050', '1438807857008349185');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328451', '1438815835165954050', '1438807947689201666');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328452', '1438815835165954050', '1438808049594011650');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328453', '1438815835165954050', '1438757050464473090');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328454', '1438815835165954050', '1438757420565663746');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328455', '1438815835165954050', '1438758102127480833');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328456', '1438815835165954050', '1438758501404250113');
INSERT INTO `sy_role_perm` VALUES ('1438815921044328457', '1438815835165954050', '1438758814488072194');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717057', '1438815835165954050', '1438759927761211394');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717058', '1438815835165954050', '1438760304820752385');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717059', '1438815835165954050', '1438760635059277826');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717060', '1438815835165954050', '1438761114501779458');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717061', '1438815835165954050', '1438761365379878913');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717062', '1438815835165954050', '1438762747365298177');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717063', '1438815835165954050', '1438763393913065475');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717064', '1438815835165954050', '1438763750340825090');
INSERT INTO `sy_role_perm` VALUES ('1438815921052717065', '1438815835165954050', '1438764022450491394');
INSERT INTO `sy_role_perm` VALUES ('1438815921061105665', '1438815835165954050', '1438764295285772289');
INSERT INTO `sy_role_perm` VALUES ('1438815921061105666', '1438815835165954050', '1438764624748351489');
INSERT INTO `sy_role_perm` VALUES ('1438815921061105667', '1438815835165954050', '1438764933709172738');

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
  `create_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL COMMENT '密码修改时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `id_center` bigint(20) DEFAULT NULL COMMENT '认证中心对应的用户id',
  `head_pic` bigint(20) DEFAULT NULL,
  `type_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_user
-- ----------------------------
INSERT INTO `sy_user` VALUES ('8888888888888888888', '8888888888888888888', 'root', '1', 'root', '超级root', null, '333333', '', '', null, '2021-09-28 20:04:10', '0', '8888888888888888888', '233233232', '0');

-- ----------------------------
-- Table structure for sy_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sy_user_role`;
CREATE TABLE `sy_user_role` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sy_user_role
-- ----------------------------
INSERT INTO `sy_user_role` VALUES ('1442788860538261505', '8888888888888888888', '8888888888888888888');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=261 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
