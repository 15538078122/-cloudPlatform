/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1_3333
Source Server Version : 80011
Source Host           : 127.0.0.1:3333
Source Database       : user-center

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2021-07-14 11:00:49
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
INSERT INTO `account` VALUES ('1410878758633738242', 'root', 'liwei', '$2a$10$mMk76qSV0ypVAddQehhMFO99yrDqOWV5nz8VOInsEKU1v5LoSB2xK', '2021-07-02 16:31:24');
INSERT INTO `account` VALUES ('1414893219057111042', 'root', 'testuser', '$2a$10$dBEt/CMhXStoUfPXT9hTJ.2wcRSq7ZFr26AOhAg/13TARVnwuc6Oe', '2021-07-13 18:23:26');
INSERT INTO `account` VALUES ('1414919679964745729', 'root', 'admin', '$2a$10$ujQvGjrc31j7gZoycSOYduudyw6mc2pHc5At3RaQBqMcD0vrV8i0C', '2021-07-13 20:08:34');
