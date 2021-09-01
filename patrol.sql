-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: patrol
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL,
  `enterprise` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `ENTER_AC_UNIQUE` (`enterprise`,`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1417016841053802498,'root','root','$2a$10$H88Sh8.yRbr/gU6pdi.qM.7FOdQCarXWsSVLAjbskdsI5Avj6cZ1.','2021-07-19 15:01:56'),(1430090755698987010,'root','liwei2','$2a$10$YCjueml3y1VfaIgsD1VyR.2s5vMM4vYnm0Ri7ykxLicStIGX.COIa','2021-08-24 16:53:01'),(1430090852734210049,'root','liwei8','$2a$10$tMOWtG415qEiez82ZFcnBOAdBGGVebv8xUoArnM0UjxkN1qI9TB0.','2021-08-24 16:53:24'),(1430091364066004993,'root','lijianfang','$2a$10$jvKORwKY4.ajX1GLF72JlOLgjz3usPvmI5o9YWj0pXdgg1zpWPn0q','2021-08-24 16:55:26');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_attach`
--

DROP TABLE IF EXISTS `sy_attach`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_attach`
--

LOCK TABLES `sy_attach` WRITE;
/*!40000 ALTER TABLE `sy_attach` DISABLE KEYS */;
INSERT INTO `sy_attach` VALUES (1432617109019693057,'root',555,'2021-08-31 16:11:50',1416950954728886274,'2323.txt',NULL,'202108/202108311611500192323.txt'),(1432619712877170690,'root',555,'2021-08-31 16:22:08',1416950954728886274,'2323.txt',NULL,'202108/202108311622081012323.txt');
/*!40000 ALTER TABLE `sy_attach` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_dict`
--

DROP TABLE IF EXISTS `sy_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_dict`
--

LOCK TABLES `sy_dict` WRITE;
/*!40000 ALTER TABLE `sy_dict` DISABLE KEYS */;
INSERT INTO `sy_dict` VALUES (1421023459328397313,'root','xyz',0,1,NULL,'类型',NULL),(1430442619850461185,'huadao','3444',0,1,NULL,'123',NULL),(1432218494203858946,'root','ew',0,1,NULL,'we',NULL);
/*!40000 ALTER TABLE `sy_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_dict_item`
--

DROP TABLE IF EXISTS `sy_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_dict_item`
--

LOCK TABLES `sy_dict_item` WRITE;
/*!40000 ALTER TABLE `sy_dict_item` DISABLE KEYS */;
INSERT INTO `sy_dict_item` VALUES (1421024891112787970,1421023459328397313,1,0,'xyz33','类型'),(1432218319423016962,1421023459328397313,1,23,'23','微服务'),(1432218367036755969,1421023459328397313,1,33,'332','23人');
/*!40000 ALTER TABLE `sy_dict_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_enterprise`
--

DROP TABLE IF EXISTS `sy_enterprise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sy_enterprise` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '企业编号(s00000：特殊企业，系统配置员使用关联菜单；s00001：特殊企业，企业管理员使用关联菜单)',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `note` varchar(250) DEFAULT NULL COMMENT '描述',
  `delete_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `enter_unique` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_enterprise`
--

LOCK TABLES `sy_enterprise` WRITE;
/*!40000 ALTER TABLE `sy_enterprise` DISABLE KEYS */;
INSERT INTO `sy_enterprise` VALUES (232323,'root','root企业','root企业',0),(1431131878307663873,'huadaoxxx','华道xxx系统',NULL,1),(1432186217730150401,'huadaoxxx1','阿萨说',NULL,1);
/*!40000 ALTER TABLE `sy_enterprise` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_func_op_url`
--

DROP TABLE IF EXISTS `sy_func_op_url`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sy_func_op_url` (
  `id` bigint(20) NOT NULL,
  `url` varchar(250) DEFAULT NULL COMMENT 'url地址',
  `perm_code` varchar(100) DEFAULT NULL COMMENT 'controller的className.method',
  `func_op_id` bigint(20) DEFAULT NULL COMMENT '功能id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_func_op_url`
--

LOCK TABLES `sy_func_op_url` WRITE;
/*!40000 ALTER TABLE `sy_func_op_url` DISABLE KEYS */;
INSERT INTO `sy_func_op_url` VALUES (1431073466517557250,'get /cur-user/info','curUser:info',1430722738691248130),(1431073466525945858,'post /dictionary','dictionary:create',1430722738691248130),(1431073466525945859,'delete /dictionary/{id}','dictionary:delete',1430722738691248130),(1431073466525945860,'put /dictionary','dictionary:edit',1430722738691248130),(1431073466530140162,'get /dictionary','dictionary:list',1430722738691248130),(1431073466530140163,'post /dictionary/item','dictItem:create',1430722738691248130),(1431073466530140164,'delete /dictionary/item/{id}','dictItem:delete',1430722738691248130),(1431073466530140165,'put /dictionary/item','dictItem:edit',1430722738691248130),(1431073466530140166,'get /dictionary/{id}/item','dictItem:get',1430722738691248130),(1431073466530140167,'post /enterprise','enterprise:create',1430722738691248130),(1431073466530140168,'delete /enterprise/{id}','enterprise:delete',1430722738691248130),(1431073466538528769,'put /enterprise/{id}','enterprise:edit',1430722738691248130),(1431073466538528770,'get /enterprise','enterprise:list',1430722738691248130),(1431073466538528771,'get /menu','enterprise:menu',1430722738691248130),(1431073466538528772,'post /function','func:create',1430722738691248130),(1431073466538528773,'delete /function/{funcId}','func:del',1430722738691248130),(1431073466538528774,'put /function/{funcId}','func:edit',1430722738691248130),(1431073466538528775,'get /function/{funcId}','func:get',1430722738691248130),(1431073466538528776,'get /function','func:get',1430722738691248130),(1431073466546917377,'post /function/opr','funcOpr:create',1430722738691248130),(1431073466546917378,'delete /function/opr/{funcOprId}','funcOpr:del',1430722738691248130),(1431073466546917379,'put /function/opr/{funcOprId}','funcOpr:edit',1430722738691248130),(1431073466546917380,'get /function/opr/{funcOprId}/url','funcOpUrl:get',1430722738691248130),(1431073466546917381,'put /function/opr/{funcOprId}/url','funcOpUrl:update',1430722738691248130),(1431073466546917382,'post /menu','menu:create',1430722738691248130),(1431073466546917383,'delete /menu/{id}','menu:del',1430722738691248130),(1431073466546917384,'put /menu/{id}','menu:edit',1430722738691248130),(1431073466546917385,'get /menu/{id}','menu:get',1430722738691248130),(1431073466546917386,'get /menu/my','menu:my',1430722738691248130),(1431073466546917387,'get /menu/myenterprise','menu:myenterprise',1430722738691248130),(1431073466546917388,'post /menu/btn','menuBtn:create',1430722738691248130),(1431073466546917389,'delete /menu/btn/{id}','menuBtn:del',1430722738691248130),(1431073466546917390,'put /menu/btn/{id}','menuBtn:edit',1430722738691248130),(1431073466546917391,'post /org','org:create',1430722738691248130),(1431073466546917392,'delete /org/{id}','org:delete',1430722738691248130),(1431073466555305986,'put /org/{id}','org:edit',1430722738691248130),(1431073466555305987,'get /org/my/tree','org:tree',1430722738691248130),(1431073466555305988,'get /org/tree','org:tree',1430722738691248130),(1431073466555305989,'get /org/{orgId}/user','orgUser:list',1430722738691248130),(1431073466555305990,'get /root/org/{orgId}/user','orgUser:list',1430722738691248130),(1431073466555305991,'post /auth','permission:auth',1430722738691248130),(1431073466555305992,'post /authbridge','permission:authbr',1430722738691248130),(1431073466555305993,'post /role','role:create',1430722738691248130),(1431073466555305994,'delete /role/{id}','role:delete',1430722738691248130),(1431073466555305995,'put /role/{id}','role:edit',1430722738691248130),(1431073466555305996,'get /role','role:list',1430722738691248130),(1431073466555305997,'get /url-mapping','urlMaping:get',1430722738691248130),(1431073466555305998,'post /user','user:create',1430722738691248130),(1431073466555305999,'delete /user/{id}','user:delete',1430722738691248130),(1431073466555306000,'put /user/{account}','user:edit',1430722738691248130),(1431073466555306001,'get /user/{account}','user:get',1430722738691248130);
/*!40000 ALTER TABLE `sy_func_op_url` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_func_operator`
--

DROP TABLE IF EXISTS `sy_func_operator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sy_func_operator` (
  `id` bigint(20) NOT NULL,
  `func_id` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_func_operator`
--

LOCK TABLES `sy_func_operator` WRITE;
/*!40000 ALTER TABLE `sy_func_operator` DISABLE KEYS */;
INSERT INTO `sy_func_operator` VALUES (1430722738691248130,1430722653278441474,'all',NULL);
/*!40000 ALTER TABLE `sy_func_operator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_function`
--

DROP TABLE IF EXISTS `sy_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_function`
--

LOCK TABLES `sy_function` WRITE;
/*!40000 ALTER TABLE `sy_function` DISABLE KEYS */;
INSERT INTO `sy_function` VALUES (1414764039753699330,'巡更巡检及工单派遣系统','333','333',0,NULL,NULL),(1430722653278441474,'所有功能','100','999.100',1,1431193490385276930,NULL),(1431193490385276930,'root目录','999','999',0,NULL,NULL);
/*!40000 ALTER TABLE `sy_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_menu`
--

DROP TABLE IF EXISTS `sy_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_menu`
--

LOCK TABLES `sy_menu` WRITE;
/*!40000 ALTER TABLE `sy_menu` DISABLE KEYS */;
INSERT INTO `sy_menu` VALUES (1414465565539700737,NULL,'root',0,'100','100',1,'系统管理',NULL,NULL,'fa fa-map-o',1),(1415150615566487554,NULL,'root',0,'200','200',1,'日常维护','',NULL,'fa fa-wrench',1),(1415150911453663233,1415150615566487554,'root',1,'100','200.100',1,'巡查点管理','/business/checkPoint','日常维护巡检点','fa fa-binoculars',1),(1415153731317796866,1415150615566487554,'root',1,'200','200.200',1,'常用巡检项','/business/checkItem','巡检项','fa fa-calendar-check-o',1),(1415154221652905985,1415150615566487554,'root',1,'300','200.300',1,'工单模板','/business/repairTemplate','工单模板','fa fa-gavel',1),(1429753843721441282,1414465565539700737,'root',1,'100','100.100',1,'菜单管理','/system/menu','菜单管理','fa fa-map-o',1),(1429755402467414018,1414465565539700737,'root',1,'200','100.200',1,'功能管理','/system/function','功能管理','fa fa-database',1),(1429755679807377409,1414465565539700737,'root',1,'300','100.300',1,'角色管理','/system/role','角色管理','fa fa-graduation-cap',1),(1429756375554330625,1414465565539700737,'root',1,'400','100.400',1,'企业管理','/system/company','企业管理','fa fa-bank (alias)',1),(1429756927059169281,1414465565539700737,'root',1,'500','100.500',1,'部门管理','/system/dept','部门管理','fa fa-users',1),(1429757099981934594,1414465565539700737,'root',1,'600','100.600',1,'人员管理','/system/user','人员管理','fa fa-user',1),(1429757306371051522,1414465565539700737,'root',1,'700','100.700',1,'数据字典管理','/system/dict','数据字典','fa fa-asl-interpreting (alias)',1),(1430072272777121793,1415150615566487554,'root',1,'400','200.400',1,'巡检计划','/business/inspectionPlan','巡检计划','fa fa-blind',1),(1431131878366384130,NULL,'huadaoxxx',0,'100','100',1,'系统管理',NULL,NULL,'fa fa-map-o',1),(1431131878374772738,1431131878366384130,'huadaoxxx',1,'100','100.100',1,'菜单管理','/system/menu','菜单管理','fa fa-map-o',1),(1431131878399938562,1431131878366384130,'huadaoxxx',1,'200','100.200',1,'功能管理','/system/function','功能管理','fa fa-database',1),(1431131878408327169,1431131878366384130,'huadaoxxx',1,'300','100.300',1,'角色管理','/system/role','角色管理','fa fa-graduation-cap',1),(1431131878416715777,1431131878366384130,'huadaoxxx',1,'400','100.400',1,'企业管理','/system/company','企业管理','fa fa-bank (alias)',1),(1431131878425104386,1431131878366384130,'huadaoxxx',1,'500','100.500',1,'部门管理','/system/dept','部门管理','fa fa-users',1),(1431131878425104387,1431131878366384130,'huadaoxxx',1,'600','100.600',1,'人员管理','/system/user','人员管理','fa fa-user',1),(1431131878433492994,1431131878366384130,'huadaoxxx',1,'700','100.700',1,'数据字典管理','/system/dict','数据字典','fa fa-asl-interpreting (alias)',1),(1431131878433492995,NULL,'huadaoxxx',0,'200','200',1,'日常维护','',NULL,'fa fa-wrench',1),(1431131878441881602,1431131878433492995,'huadaoxxx',1,'100','200.100',1,'巡检点管理','/business/checkPoint','日常维护巡检点','fa fa-binoculars',1),(1431131878441881603,1431131878433492995,'huadaoxxx',1,'200','200.200',1,'常用巡检项','/business/checkItem','巡检项','fa fa-calendar-check-o',1),(1431131878450270210,1431131878433492995,'huadaoxxx',1,'300','200.300',1,'工单模板','/business/repairTemplate','工单模板','fa fa-gavel',1),(1431131878450270211,1431131878433492995,'huadaoxxx',1,'400','200.400',1,'巡检计划','/business/inspectionPlan','巡检计划','fa fa-blind',1),(1431160055067316225,NULL,'root',1,'999','999',1,'root专用',NULL,NULL,NULL,0),(1432186218002780161,NULL,'huadaoxxx1',0,'100','100',1,'系统管理',NULL,NULL,'fa fa-map-o',1),(1432186218011168769,1432186218002780161,'huadaoxxx1',1,'100','100.100',1,'菜单管理','/system/menu','菜单管理','fa fa-map-o',1),(1432186218040528897,1432186218002780161,'huadaoxxx1',1,'200','100.200',1,'功能管理','/system/function','功能管理','fa fa-database',1),(1432186218057306113,1432186218002780161,'huadaoxxx1',1,'300','100.300',1,'角色管理','/system/role','角色管理','fa fa-graduation-cap',1),(1432186218057306114,1432186218002780161,'huadaoxxx1',1,'400','100.400',1,'企业管理','/system/company','企业管理','fa fa-bank (alias)',1),(1432186218074083329,1432186218002780161,'huadaoxxx1',1,'500','100.500',1,'部门管理','/system/dept','部门管理','fa fa-users',1),(1432186218074083330,1432186218002780161,'huadaoxxx1',1,'600','100.600',1,'人员管理','/system/user','人员管理','fa fa-user',1),(1432186218082471938,1432186218002780161,'huadaoxxx1',1,'700','100.700',1,'数据字典管理','/system/dict','数据字典','fa fa-asl-interpreting (alias)',1),(1432186218090860546,NULL,'huadaoxxx1',0,'200','200',1,'日常维护','',NULL,'fa fa-wrench',1),(1432186218099249153,1432186218090860546,'huadaoxxx1',1,'100','200.100',1,'巡查点管理','/business/checkPoint','日常维护巡检点','fa fa-binoculars',1),(1432186218107637762,1432186218090860546,'huadaoxxx1',1,'200','200.200',1,'常用巡检项','/business/checkItem','巡检项','fa fa-calendar-check-o',1),(1432186218116026369,1432186218090860546,'huadaoxxx1',1,'300','200.300',1,'工单模板','/business/repairTemplate','工单模板','fa fa-gavel',1),(1432186218116026370,1432186218090860546,'huadaoxxx1',1,'400','200.400',1,'巡检计划','/business/inspectionPlan','巡检计划','fa fa-blind',1);
/*!40000 ALTER TABLE `sy_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_menu_btn`
--

DROP TABLE IF EXISTS `sy_menu_btn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_menu_btn`
--

LOCK TABLES `sy_menu_btn` WRITE;
/*!40000 ALTER TABLE `sy_menu_btn` DISABLE KEYS */;
INSERT INTO `sy_menu_btn` VALUES (1431089826874265601,'root',1429753843721441282,1,1431072725727973378,'添加','add',NULL,NULL,1),(1431089884965376002,'root',1429755402467414018,1,1430722738691248130,'添加','add',NULL,NULL,1),(1431131878391549953,'huadaoxxx',1431131878374772738,1,1431072725727973378,'添加','add',NULL,NULL,1),(1431131878399938563,'huadaoxxx',1431131878399938562,1,1430722738691248130,'添加','add',NULL,NULL,1),(1431160055092482050,'root',1431160055067316225,1,NULL,'list',NULL,NULL,NULL,0),(1432186218036334593,'huadaoxxx1',1432186218011168769,1,1431072725727973378,'添加','add',NULL,NULL,1),(1432186218048917505,'huadaoxxx1',1432186218040528897,1,1430722738691248130,'添加','add',NULL,NULL,1),(1432219136926420993,'root',1429753843721441282,1,1430722738691248130,'list','null',NULL,NULL,0);
/*!40000 ALTER TABLE `sy_menu_btn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_org`
--

DROP TABLE IF EXISTS `sy_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_org`
--

LOCK TABLES `sy_org` WRITE;
/*!40000 ALTER TABLE `sy_org` DISABLE KEYS */;
INSERT INTO `sy_org` VALUES (1416950954728886274,NULL,'root',NULL,NULL,0,1,'超级公司','超级',NULL,NULL,0),(1416951234841284609,1416950954728886274,'root',NULL,'1',1,1,'运营中心','运营',NULL,NULL,0),(1416951429960306689,1416950954728886274,'root',NULL,'2',0,1,'销售中心','销售',NULL,NULL,0),(1416951650727497729,1416950954728886274,'root',NULL,'3',0,1,'研发中心','研发',NULL,NULL,1),(1416951913362231298,1416951650727497729,'root',NULL,'3.1',1,1,'软件部','软件',NULL,NULL,1),(1432186429651554306,NULL,'huadaoxxx1',NULL,'111',0,1,'额v',NULL,NULL,NULL,1),(1432186490150195202,1432186429651554306,'huadaoxxx1',NULL,NULL,1,1,'12人',NULL,NULL,NULL,1),(1432188554330771457,NULL,'huadaoxxx1',NULL,NULL,1,1,'12',NULL,NULL,NULL,0),(1432188610471530498,1432188554330771457,'huadaoxxx1',NULL,NULL,1,1,'22',NULL,NULL,NULL,1),(1432188669741240321,1432188610471530498,'huadaoxxx1',NULL,NULL,0,1,'223232',NULL,NULL,NULL,1),(1432188772208087042,1432188554330771457,'huadaoxxx1',NULL,NULL,1,1,'23',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `sy_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_role`
--

DROP TABLE IF EXISTS `sy_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sy_role` (
  `id` bigint(20) NOT NULL,
  `enterprise_id` varchar(20) DEFAULT NULL COMMENT '租户id',
  `enabled` int(11) DEFAULT '1' COMMENT '状态{1：启用，0：停用}',
  `sort_num` int(11) DEFAULT '0' COMMENT '排序号',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `data_privilege` tinyint(1) DEFAULT NULL,
  `note` varchar(200) DEFAULT NULL COMMENT '备注',
  `delete_flag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_role`
--

LOCK TABLES `sy_role` WRITE;
/*!40000 ALTER TABLE `sy_role` DISABLE KEYS */;
INSERT INTO `sy_role` VALUES (1430809691507920898,'root',1,1,'超级root',0,NULL,0),(1432189116828880898,'huadaoxxx1',1,0,'企业管理员',0,NULL,0),(1432219484881686529,'root',1,3,'未3',0,NULL,0);
/*!40000 ALTER TABLE `sy_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_role_perm`
--

DROP TABLE IF EXISTS `sy_role_perm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sy_role_perm` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `menu_btn_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_role_perm`
--

LOCK TABLES `sy_role_perm` WRITE;
/*!40000 ALTER TABLE `sy_role_perm` DISABLE KEYS */;
INSERT INTO `sy_role_perm` VALUES (1432219297887031298,1430809691507920898,1431089826874265601),(1432219297887031299,1430809691507920898,1432219136926420993),(1432219297887031300,1430809691507920898,1431089884965376002);
/*!40000 ALTER TABLE `sy_role_perm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_url_mapping`
--

DROP TABLE IF EXISTS `sy_url_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sy_url_mapping` (
  `id` bigint(20) NOT NULL,
  `url` varchar(250) DEFAULT NULL COMMENT 'url地址',
  `perm_code` varchar(100) DEFAULT NULL,
  `handler` varchar(200) DEFAULT NULL COMMENT 'controller的className.method',
  `notes` varchar(300) DEFAULT NULL COMMENT '注释',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_url_mapping`
--

LOCK TABLES `sy_url_mapping` WRITE;
/*!40000 ALTER TABLE `sy_url_mapping` DISABLE KEYS */;
INSERT INTO `sy_url_mapping` VALUES (1430697393384591362,'post /auth','permission:auth','com.hd.microauservice.controller.AuthenticationController:auth',''),(1430697393653026818,'post /authbridge','permission:authbr','com.hd.microauservice.controller.AuthenticationController:authbridge',''),(1430697393959211009,'put /dictionary','dictionary:edit','com.hd.microauservice.controller.DictionaryController:editDictionary',''),(1430697394265395202,'post /dictionary','dictionary:create','com.hd.microauservice.controller.DictionaryController:createDictionary',''),(1430697394579968001,'get /dictionary','dictionary:list','com.hd.microauservice.controller.DictionaryController:getDictionary',''),(1430697394936483842,'delete /dictionary/{id}','dictionary:delete','com.hd.microauservice.controller.DictionaryController:delDictionary',''),(1430697395196530689,'get /dictionary/{id}/item','dictItem:get','com.hd.microauservice.controller.DictionaryController:getDictionaryItem',''),(1430697395506909185,'delete /dictionary/item/{id}','dictItem:delete','com.hd.microauservice.controller.DictionaryController:delDictionaryItem',''),(1430697395813093377,'put /dictionary/item','dictItem:edit','com.hd.microauservice.controller.DictionaryController:editDictionaryItem',''),(1430697396119277570,'post /dictionary/item','dictItem:create','com.hd.microauservice.controller.DictionaryController:createDictionaryItem',''),(1430697396433850369,'delete /enterprise/{id}','enterprise:delete','com.hd.microauservice.controller.EnterpriseController:deleteEnterprise',''),(1430697396740034562,'get /enterprise','enterprise:list','com.hd.microauservice.controller.EnterpriseController:getEnterprise','分页获取企业列表'),(1430697397054607361,'put /enterprise/{id}','enterprise:edit','com.hd.microauservice.controller.EnterpriseController:editEnterprise',''),(1430697397696335873,'post /enterprise','enterprise:create','com.hd.microauservice.controller.EnterpriseController:createEnterprise',''),(1430697398015102978,'get /function/{funcId}','func:get','com.hd.microauservice.controller.FunctionController:getFunc',''),(1430697398329675777,'get /function','func:get','com.hd.microauservice.controller.FunctionController:getfunction',''),(1430697398627471362,'delete /function/{funcId}','func:del','com.hd.microauservice.controller.FunctionController:delFunc',''),(1430697399323725826,'get /url-mapping','urlMaping:get','com.hd.microauservice.controller.FunctionController:getUrlMapping',''),(1430697399529246722,'get /function/opr/{funcOprId}/url','funcOpUrl:get','com.hd.microauservice.controller.FunctionController:getfunOpUrl','获取某个操作对应的Uri'),(1430697399835430913,'put /function/opr/{funcOprId}/url','funcOpUrl:update','com.hd.microauservice.controller.FunctionController:updatefunOpUrl','更新某个操作对应的Uri'),(1430697400141615106,'delete /function/opr/{funcOprId}','funcOpr:del','com.hd.microauservice.controller.FunctionController:delFuncOpr',''),(1430697400451993601,'put /function/{funcId}','func:edit','com.hd.microauservice.controller.FunctionController:editFunc',''),(1430697400766566402,'post /function','func:create','com.hd.microauservice.controller.FunctionController:createFunc',''),(1430697401072750594,'post /function/opr','funcOpr:create','com.hd.microauservice.controller.FunctionController:createFuncOpr',''),(1430697401387323393,'put /function/opr/{funcOprId}','funcOpr:edit','com.hd.microauservice.controller.FunctionController:editFuncOpr',''),(1430697401693507585,'delete /menu/btn/{id}','menuBtn:del','com.hd.microauservice.controller.MenuController:delMenuBtn',''),(1430697401999691777,'post /menu/btn','menuBtn:create','com.hd.microauservice.controller.MenuController:createMenuBtn',''),(1430697402310070274,'get /menu/my','menu:my','com.hd.microauservice.controller.MenuController:getMyMenu',''),(1430697402620448770,'put /menu/{id}','menu:edit','com.hd.microauservice.controller.MenuController:editMenu',''),(1430697402825969665,'post /menu','menu:create','com.hd.microauservice.controller.MenuController:createMenu',''),(1430697403035684866,'get /menu/{id}','menu:get','com.hd.microauservice.controller.MenuController:getMenu',''),(1430697403346063361,'delete /menu/{id}','menu:del','com.hd.microauservice.controller.MenuController:delMenu',''),(1430697403551584257,'put /menu/btn/{id}','menuBtn:edit','com.hd.microauservice.controller.MenuController:editMenuBtn',''),(1430697403866157057,'get /menu','enterprise:menu','com.hd.microauservice.controller.MenuController:getEnterpriseMenu',''),(1430697404071677953,'get /menu/myenterprise','menu:myenterprise','com.hd.microauservice.controller.MenuController:getMyEnterpriseMenu',''),(1430697404382056450,'post /org','org:create','com.hd.microauservice.controller.OrgController:createOrg',''),(1430697404688240641,'get /org/my/tree','org:tree','com.hd.microauservice.controller.OrgController:getOrgMyTree',''),(1430697405002813441,'put /org/{id}','org:edit','com.hd.microauservice.controller.OrgController:editOrg',''),(1430697405208334338,'get /org/tree','org:tree','com.hd.microauservice.controller.OrgController:getOrgTree',''),(1430697405518712834,'delete /org/{id}','org:delete','com.hd.microauservice.controller.OrgController:delOrg',''),(1430697405824897026,'put /role/{id}','role:edit','com.hd.microauservice.controller.RoleController:editRole',''),(1430697406034612226,'get /role','role:list','com.hd.microauservice.controller.RoleController:getRoles','分页获取角色列表'),(1430697406365962241,'post /role','role:create','com.hd.microauservice.controller.RoleController:createRole',''),(1430697406651174914,'delete /role/{id}','role:delete','com.hd.microauservice.controller.RoleController:delRole',''),(1430697406856695810,'get /cur-user/info','curUser:info','com.hd.microauservice.controller.UserController:getCurrentUser',''),(1430697407167074305,'delete /user/{id}','user:delete','com.hd.microauservice.controller.UserController:removeUser',''),(1430697407372595201,'get /org/{orgId}/user','orgUser:list','com.hd.microauservice.controller.UserController:getOrgUserList','分页获取部门用户列表信息'),(1430697407573921793,'get /user/{account}','user:get','com.hd.microauservice.controller.UserController:getUser','获取用户详情'),(1430697407892688898,'post /user','user:create','com.hd.microauservice.controller.UserController:createUser',''),(1430697408203067393,'put /user/{account}','user:edit','com.hd.microauservice.controller.UserController:editUser','编辑用户'),(1430697408505057282,'get /root/org/{orgId}/user','orgUser:list','com.hd.microauservice.controller.UserController:getRootOrgUserList','root分页获取部门用户列表信息');
/*!40000 ALTER TABLE `sy_url_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_user`
--

DROP TABLE IF EXISTS `sy_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_user`
--

LOCK TABLES `sy_user` WRITE;
/*!40000 ALTER TABLE `sy_user` DISABLE KEYS */;
INSERT INTO `sy_user` VALUES (3,1416951234841284609,'huadao',1,'liwei','李威',NULL,NULL,NULL,NULL,NULL,1),(4,1416951234841284609,'huadao',1,'liwei','李威',NULL,NULL,NULL,NULL,NULL,1),(2323,1416951234841284609,'huadao',1,'liwei','李威',NULL,NULL,NULL,NULL,NULL,1),(1416950954728886274,1416950954728886274,'root',1,'root','超级root',NULL,NULL,NULL,NULL,NULL,0),(1430090670638632962,NULL,'root',1,'liwei2',NULL,NULL,NULL,NULL,NULL,NULL,0),(1430090837437714434,NULL,'root',1,'liwei8',NULL,NULL,NULL,NULL,NULL,NULL,0),(1430091363483127810,1416951913362231298,'root',1,'lijianfang','李建防',NULL,'13273815537',NULL,'开发',NULL,1),(1430786155552706562,1416950954728886274,'root',1,'wwww','wwww',NULL,NULL,NULL,NULL,NULL,1),(1430787576767451138,1416950954728886274,'root',1,'eeee','eee',NULL,NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `sy_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sy_user_role`
--

DROP TABLE IF EXISTS `sy_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sy_user_role` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sy_user_role`
--

LOCK TABLES `sy_user_role` WRITE;
/*!40000 ALTER TABLE `sy_user_role` DISABLE KEYS */;
INSERT INTO `sy_user_role` VALUES (1430090670735101954,1430090670638632962,12121),(1430090670743490561,1430090670638632962,3445),(1430090837479657473,1430090837437714434,12121),(1430090837496434690,1430090837437714434,3445),(1430797376129601538,1430091363483127810,1416958685066039298),(1430797376129601539,1430091363483127810,1430074800369569794),(1430811086004948993,1417016827556663297,1430809691507920898),(1432219250235543554,1416950954728886274,1430809691507920898);
/*!40000 ALTER TABLE `sy_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'patrol'
--

--
-- Dumping routines for database 'patrol'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-09-01  8:40:34
