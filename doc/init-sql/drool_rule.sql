/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.7.46
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 192.168.7.46:3306
 Source Schema         : drools

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 14/06/2023 09:38:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for drool_rule
-- ----------------------------
DROP TABLE IF EXISTS `drool_rule`;
CREATE TABLE `drool_rule`  (
  `rule_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '规则di',
  `rule_name` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '规则名称',
  `kie_base_name` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '规则库名称',
  `kie_package_name` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '规则路径',
  `rule_content` text CHARACTER SET utf8mb4 NULL COMMENT '规则内容',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '规则' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
