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

 Date: 22/06/2023 22:46:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tweet_r_mention
-- ----------------------------
DROP TABLE IF EXISTS `tweet_r_mention`;
CREATE TABLE `tweet_r_mention`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tweet_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tweet_mention` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
