-- ----------------------------
-- Table structure for drool_rule
-- ----------------------------
DROP TABLE IF EXISTS `drool_rule`;
CREATE TABLE `drool_rule`  (
  `rule_id` varchar(32) NOT NULL COMMENT '规则di',
  `rule_name` varchar(255) NULL DEFAULT NULL COMMENT '规则名称',
  `kie_base_name` varchar(255) NULL DEFAULT NULL COMMENT '规则库名称',
  `kie_package_name` varchar(255) NULL DEFAULT NULL COMMENT '规则路径',
  `rule_content` text NULL COMMENT '规则内容',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`rule_id`) USING BTREE
) COMMENT = '规则';

