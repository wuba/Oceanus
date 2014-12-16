/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.123.153
 Source Server Type    : MySQL
 Source Server Version : 50535
 Source Host           : 192.168.123.153
 Source Database       : test_oceanus_schema3

 Target Server Type    : MySQL
 Target Server Version : 50535
 File Encoding         : utf-8

 Date: 12/15/2014 18:20:05 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_product`
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` bigint(20) NOT NULL,
  `sn` varchar(50) DEFAULT NULL,
  `sale` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试用表';

SET FOREIGN_KEY_CHECKS = 1;
