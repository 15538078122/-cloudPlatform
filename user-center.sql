/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1_3333
Source Server Version : 80011
Source Host           : 127.0.0.1:3333
Source Database       : user-center

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2021-07-09 16:43:48
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `ENTER_AC_UNIQUE` (`enterprise`,`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1410874401045483521', 'hxxxkj', 'admin9', '$2a$10$gs/TsC8UCpEYaLp2JmhZ3OZbTf0f96Gtw2reDvaW/Jso5ztOn1kaG', '2021-07-02 16:14:05');
INSERT INTO `account` VALUES ('1410874550106853377', 'hxxxkj', 'admin0', '$2a$10$CWSgurqlKbY8ztGpwg4PPeOPrX9/cMMFz9It3NEa8UdhCt6O9tvP2', '2021-07-02 16:14:40');
INSERT INTO `account` VALUES ('1410875869186101249', 'hxxxkj', 'admin', '$2a$10$SuZTzQu8RoDWu4olg.xruePuOqYdCBn7Asq6XJkQFLVfcbVuljFCG', '2021-07-02 16:19:55');
INSERT INTO `account` VALUES ('1410878758633738242', 'abc', 'liwei', '$2a$10$mMk76qSV0ypVAddQehhMFO99yrDqOWV5nz8VOInsEKU1v5LoSB2xK', '2021-07-02 16:31:24');
INSERT INTO `account` VALUES ('1410892362053455874', 'abc', 'admin', '$2a$10$MgJ8oBuJKSE4bUoPoyfipOWX60dcBqji90QAVZrgSsDD8MZwOa6O6', '2021-07-02 17:25:27');
