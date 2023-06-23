/*
 Navicat Premium Data Transfer

 Source Server         : 云主机-tweet
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : 103.106.190.60:3306
 Source Schema         : tweet

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 23/06/2023 14:36:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tweet_search_keyword
-- ----------------------------
DROP TABLE IF EXISTS `tweet_search_keyword`;
CREATE TABLE `tweet_search_keyword`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tweet_search_keyword
-- ----------------------------
INSERT INTO `tweet_search_keyword` VALUES (1, 'ltc20', '2023-06-23 14:35:21');
INSERT INTO `tweet_search_keyword` VALUES (2, 'drc20', '2023-06-23 14:35:57');

SET FOREIGN_KEY_CHECKS = 1;
