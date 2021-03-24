/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : plat_publish

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 17/11/2020 10:54:31
*/
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for import_file_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `import_file_log`  (
  `count` int(0) NULL DEFAULT NULL,
  `auto` tinyint(1) NULL DEFAULT NULL,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sm_department
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sm_department`  (
  `id` int(0) NOT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pid` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sm_function
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sm_function`  (
  `id` int(0) NOT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pid` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sm_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sm_role`  (
  `id` int(0) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createtime` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sm_role_department
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sm_role_department`  (
  `roleid` int(0) NOT NULL,
  `departmentId` int(0) NOT NULL,
  PRIMARY KEY (`roleid`, `departmentId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sm_role_function
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sm_role_function`  (
  `roleid` int(0) NOT NULL,
  `functionid` int(0) NOT NULL,
  PRIMARY KEY (`roleid`, `functionid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sm_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sm_user`  (
  `id` int(0) NOT NULL,
  `username` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `realname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deptid` int(0) NOT NULL,
  `createtime` datetime(0) NOT NULL,
  `deptName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_UNIQUE`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sm_user_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sm_user_role`  (
  `userid` int(0) NOT NULL,
  `roleid` int(0) NOT NULL,
  PRIMARY KEY (`userid`, `roleid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for summary_day
-- ----------------------------
CREATE TABLE IF NOT EXISTS `summary_day`  (
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `workArea` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `workshop` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `productType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `partName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `testType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `machineType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `machineId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dutyDate` date NOT NULL,
  `waitDuaration` bigint(0) NULL DEFAULT 0,
  `stopDuaration` bigint(0) NULL DEFAULT 0,
  `totalCount` int(0) NULL DEFAULT 0,
  `firstPassRatio` float NULL DEFAULT 0,
  `retestPassRatio` float NULL DEFAULT 0,
  `firstTotalCount` int(0) NULL DEFAULT 0,
  `firstPassCount` int(0) NULL DEFAULT 0,
  `retestTotalCount` int(0) NULL DEFAULT 0,
  `retestPassCount` int(0) NULL DEFAULT 0,
  `top1NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top1NgCount` int(0) NULL DEFAULT NULL,
  `top2NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top2NgCount` int(0) NULL DEFAULT NULL,
  `top3NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top3NgCount` int(0) NULL DEFAULT NULL,
  `top1AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top1AlarmCount` int(0) NULL DEFAULT NULL,
  `top2AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top2AlarmCount` int(0) NULL DEFAULT NULL,
  `top3AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top3AlarmCount` int(0) NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ip`, `dutyDate`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for summary_duty
-- ----------------------------
CREATE TABLE IF NOT EXISTS `summary_duty`  (
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `workArea` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `workshop` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `productType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `partName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `testType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `machineType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `machineId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dutyDate` date NOT NULL,
  `duty` int(0) NOT NULL,
  `waitDuaration` bigint(0) NULL DEFAULT 0,
  `stopDuaration` bigint(0) NULL DEFAULT 0,
  `totalCount` int(0) NULL DEFAULT 0,
  `firstPassRatio` float NULL DEFAULT 0,
  `retestPassRatio` float NULL DEFAULT 0,
  `firstTotalCount` int(0) NULL DEFAULT 0,
  `firstPassCount` int(0) NULL DEFAULT 0,
  `retestTotalCount` int(0) NULL DEFAULT 0,
  `retestPassCount` int(0) NULL DEFAULT 0,
  `top1NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top1NgCount` int(0) NULL DEFAULT NULL,
  `top2NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top2NgCount` int(0) NULL DEFAULT NULL,
  `top3NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top3NgCount` int(0) NULL DEFAULT NULL,
  `top1AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top1AlarmCount` int(0) NULL DEFAULT NULL,
  `top2AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top2AlarmCount` int(0) NULL DEFAULT NULL,
  `top3AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top3AlarmCount` int(0) NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ip`, `dutyDate`, `duty`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for summary_hour
-- ----------------------------
CREATE TABLE IF NOT EXISTS `summary_hour`  (
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `workArea` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `workshop` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `productType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `partName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `testType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `machineType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `machineId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dutyDate` date NOT NULL,
  `duty` int(0) NOT NULL,
  `testDate` date NOT NULL,
  `testHour` int(0) NOT NULL,
  `uphTotalCount` int(0) NULL DEFAULT 0,
  `uphPassCount` int(0) NULL DEFAULT 0,
  `waitDuaration` bigint(0) NOT NULL DEFAULT 0,
  `stopDuaration` bigint(0) NOT NULL DEFAULT 0,
  `totalCount` int(0) NULL DEFAULT 0,
  `firstPassRatio` float NULL DEFAULT 0,
  `retestPassRatio` float NULL DEFAULT 0,
  `firstTotalCount` int(0) NULL DEFAULT 0,
  `firstPassCount` int(0) NULL DEFAULT 0,
  `retestTotalCount` int(0) NULL DEFAULT 0,
  `retestPassCount` int(0) NULL DEFAULT 0,
  `top1NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top1NgCount` int(0) NULL DEFAULT NULL,
  `top2NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top2NgCount` int(0) NULL DEFAULT NULL,
  `top3NgItem` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top3NgCount` int(0) NULL DEFAULT NULL,
  `top1AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top1AlarmCount` int(0) NULL DEFAULT NULL,
  `top2AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top2AlarmCount` int(0) NULL DEFAULT NULL,
  `top3AlarmItem` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `top3AlarmCount` int(0) NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`ip`, `dutyDate`, `duty`, `testHour`) USING BTREE,
  INDEX `index_dutydate`(`dutyDate`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
