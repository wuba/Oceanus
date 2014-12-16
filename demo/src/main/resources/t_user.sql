/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.123.148
 Source Server Type    : MySQL
 Source Server Version : 50535
 Source Host           : 192.168.123.148
 Source Database       : test_oceanus

 Target Server Type    : MySQL
 Target Server Version : 50535
 File Encoding         : utf-8

 Date: 12/11/2014 10:50:55 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL,
  `uname` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试用表';

SET FOREIGN_KEY_CHECKS = 1;
