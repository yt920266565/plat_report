/*!40101 SET NAMES utf8mb4 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`publish` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `publish`;

-- ----------------------------
-- Table structure for sm_user
-- ----------------------------
DROP TABLE IF EXISTS `sm_user`;
CREATE TABLE `sm_user`  (
  `id` int(0) NOT NULL,
  `username` varchar(40)  NOT NULL,
  `password` varchar(200)  NULL DEFAULT NULL,
  `realname` varchar(40)  NOT NULL,
  `email` varchar(40)  NULL DEFAULT NULL,
  `phone` varchar(40)  NULL DEFAULT NULL,
  `deptid` int(0) NOT NULL,
  `deptname` varchar(40)  NOT NULL,
  `createtime` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

-- ----------------------------
-- Table structure for sm_role
-- ----------------------------
DROP TABLE IF EXISTS `sm_role`;
CREATE TABLE `sm_role`  (
  `id` int(0) NOT NULL,
  `name` varchar(100)  NOT NULL,
  `description` varchar(200)  NULL DEFAULT NULL,
  `createtime` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

-- ----------------------------
-- Table structure for sm_function
-- ----------------------------
DROP TABLE IF EXISTS `sm_function`;
CREATE TABLE `sm_function`  (
  `id` int(0) NOT NULL,
  `name` varchar(40)  NOT NULL,
  `description` varchar(200)  NOT NULL,
  `url` varchar(200)  NULL DEFAULT NULL,
  `method` varchar(100)  NULL DEFAULT NULL,
  `pid` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

-- ----------------------------
-- Table structure for sm_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sm_user_role`;
CREATE TABLE `sm_user_role`  (
  `userid` int(11) NOT NULL,
  `roleid` int(11) NOT NULL,
  PRIMARY KEY (`userid`, `roleid`) USING BTREE
);

-- ----------------------------
-- Table structure for sm_role_function
-- ----------------------------
DROP TABLE IF EXISTS `sm_role_function`;
CREATE TABLE `sm_role_function`  (
  `roleid` int(11) NOT NULL,
  `functionid` int(11) NOT NULL,
  PRIMARY KEY (`roleid`, `functionid`) USING BTREE
);

-- ----------------------------
-- Table structure for sm_role_department
-- ----------------------------
DROP TABLE IF EXISTS `sm_role_department`;
CREATE TABLE `sm_role_department`  (
  `roleid` int(11) NOT NULL,
  `departmentId` int(11) NOT NULL,
  PRIMARY KEY (`roleid`, `departmentId`) USING BTREE
);

-- ----------------------------
-- Table structure for sm_department
-- ----------------------------
DROP TABLE IF EXISTS `sm_department`;
CREATE TABLE `sm_department`  (
  `id` int(0) NOT NULL,
  `name` varchar(40)  NOT NULL,
  `description` varchar(200)  NULL DEFAULT NULL,
  `pid` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
);

-- ----------------------------
-- Table structure for ver_main
-- ----------------------------
DROP TABLE IF EXISTS `ver_main`;
CREATE TABLE `ver_main`  (
  `flowNo` varchar(20)  NOT NULL,
  `projName` varchar(200)  NOT NULL,
  `product` varchar(40)  NOT NULL,
  `subPartNo` varchar(40)  NOT NULL,
  `testType` varchar(40)  NOT NULL,
  `verType` varchar(40)  NOT NULL,
  `deptId` varchar(40)  NOT NULL,
  `client` varchar(40)  NULL DEFAULT NULL,
  `verlevel` int(10) NOT NULL,
  `pkgType` int(5) NOT NULL,
  `verId` varchar(100)  NOT NULL,
  `verNo` varchar(40)  NOT NULL,
  `cfgManager` int(11) NOT NULL,
  `testManager` int(11) NOT NULL,
  `verInfo` varchar(1000)  NOT NULL,
  `status` int(5) NOT NULL,
  `currenter` int(11) NULL DEFAULT NULL,
  `createTime` datetime(0) NOT NULL,
  `applicant` int(11) NOT NULL,
  `applyTime` datetime(0) NULL DEFAULT NULL,
  `builder` int(11) NULL DEFAULT NULL,
  `buildTime` datetime(0) NULL DEFAULT NULL,
  `testTime` datetime(0) NULL DEFAULT NULL,
  `testResult` int(11) NULL DEFAULT NULL,
  `tester` int(11) NULL DEFAULT NULL,
  `publishTime` datetime(6) NULL DEFAULT NULL,
  `publisher` int(5) NULL DEFAULT NULL,
  `buildInfo` varchar(1000)  NULL DEFAULT NULL,
  `tempInfo` varchar(1000)  NULL DEFAULT NULL,
  `verPath` varchar(500)  NULL DEFAULT NULL,
  PRIMARY KEY (`flowNo`) USING BTREE
);

-- ----------------------------
-- Table structure for ver_operator
-- ----------------------------
DROP TABLE IF EXISTS `ver_operation`;
CREATE TABLE `ver_operation`  (
  `flowNo` varchar(20)  NOT NULL COMMENT '操作流水号',
  `mFlowNo` varchar(20)  NOT NULL,
  `name` varchar(40)  NOT NULL,
  `result` int(11) NOT NULL,
  `comment` varchar(1000)  NULL DEFAULT NULL,
  `comment1` varchar(1000)  NULL DEFAULT NULL,
  `comment2` varchar(1000)  NULL DEFAULT NULL,
  `comment3` varchar(1000)  NULL DEFAULT NULL,
  `opTime` datetime(0) NULL DEFAULT NULL,
  `operator` int(11) NOT NULL,
  PRIMARY KEY (`flowNo`) USING BTREE
);

-- ----------------------------
-- Table structure for ver_test
-- ----------------------------
DROP TABLE IF EXISTS `ver_test`;
CREATE TABLE `ver_test`  (
  `flowNo` varchar(20)  NOT NULL COMMENT '操作流水号',
  `mainFlowNo` varchar(20)  NOT NULL,
  `tester` varchar(40)  NOT NULL,
  `result` int(11) NOT NULL,
  `startTime` varchar(1000)  NOT NULL,
  `endTime` varchar(1000)  NOT NULL,
  `fbuga` int(11) NOT NULL,
  `fbugb` int(11) NOT NULL,
  `opTime` datetime(0) NOT NULL,
  `operator` varchar(255)  NOT NULL,
  `fbugc` int(11) NOT NULL,
  `fbugd` int(11) NOT NULL,
  `reportPath` varchar(200)  NOT NULL,
  `comment` varchar(1000)  NOT NULL,
  PRIMARY KEY (`flowNo`) USING BTREE
);

-- ----------------------------
-- Table structure for ver_publish
-- ----------------------------
DROP TABLE IF EXISTS `ver_publish`;
CREATE TABLE `ver_publish`  (
  `flowNo` varchar(20)  NOT NULL,
  `projName` varchar(200)  NULL DEFAULT NULL,
  `product` varchar(40)  NOT NULL,
  `subPartNo` varchar(40)  NOT NULL,
  `testType` varchar(40)  NOT NULL,
  `verType` varchar(40)  NOT NULL,
  `deptId` varchar(40)  NOT NULL,
  `client` varchar(40)  NOT NULL,
  `verlevel` int(10) NULL DEFAULT NULL,
  `pkgType` int(5) NOT NULL,
  `verId` varchar(100)  NOT NULL,
  `verNo` varchar(40)  NOT NULL,
  `cfgManager` int(11) NOT NULL,
  `testManager` int(11) NOT NULL,
  `verInfo` varchar(1000)  NULL DEFAULT NULL,
  `status` int(5) NULL DEFAULT NULL,
  `currenter` int(11) NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `applicant` int(11) NULL DEFAULT NULL,
  `applyTime` datetime(0) NULL DEFAULT NULL,
  `builder` int(11) NULL DEFAULT NULL,
  `buildTime` datetime(0) NULL DEFAULT NULL,
  `testTime` datetime(0) NULL DEFAULT NULL,
  `testResult` int(11) NULL DEFAULT NULL,
  `tester` int(11) NULL DEFAULT NULL,
  `publishTime` datetime(6) NULL DEFAULT NULL,
  `publisher` int(5) NULL DEFAULT NULL,
  `buildInfo` varchar(1000)  NULL DEFAULT NULL,
  `tempInfo` varchar(1000)  NULL DEFAULT NULL,
  `verPath` varchar(500)  NULL DEFAULT NULL,
  PRIMARY KEY (`flowNo`) USING BTREE
);

-- ----------------------------
-- Table structure for ver_over
-- ----------------------------
DROP TABLE IF EXISTS `ver_over`;
CREATE TABLE `ver_over`  (
  `flowNo` varchar(20)  NOT NULL,
  `projName` varchar(200)  NULL DEFAULT NULL,
  `product` varchar(40)  NOT NULL,
  `subPartNo` varchar(40)  NOT NULL,
  `testType` varchar(40)  NOT NULL,
  `verType` varchar(40)  NOT NULL,
  `deptId` varchar(40)  NOT NULL,
  `client` varchar(40)  NOT NULL,
  `verlevel` int(10) NULL DEFAULT NULL,
  `pkgType` int(5) NOT NULL,
  `verId` varchar(100)  NOT NULL,
  `verNo` varchar(40)  NOT NULL,
  `cfgManager` int(11) NOT NULL,
  `testManager` int(11) NOT NULL,
  `verInfo` varchar(1000)  NULL DEFAULT NULL,
  `status` int(5) NOT NULL,
  `currenter` int(11) NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `applicant` int(11) NULL DEFAULT NULL,
  `applyTime` datetime(0) NULL DEFAULT NULL,
  `builder` int(11) NULL DEFAULT NULL,
  `buildTime` datetime(0) NULL DEFAULT NULL,
  `testTime` datetime(0) NULL DEFAULT NULL,
  `testResult` int(11) NULL DEFAULT NULL,
  `tester` int(11) NULL DEFAULT NULL,
  `publishTime` datetime(6) NULL DEFAULT NULL,
  `publisher` int(5) NULL DEFAULT NULL,
  `buildInfo` varchar(1000)  NULL DEFAULT NULL,
  `tempInfo` varchar(1000)  NULL DEFAULT NULL,
  `verPath` varchar(500)  NULL DEFAULT NULL,
  PRIMARY KEY (`flowNo`) USING BTREE
);

/*Table structure for table `erp_dictionary` */

DROP TABLE IF EXISTS `erp_dictionary`;

CREATE TABLE `erp_dictionary` (
  `type` varchar(40) NOT NULL,
  `code` varchar(40) NOT NULL,
  `name` varchar(40) NOT NULL,
  `sort` int(11) NOT NULL,
  PRIMARY KEY (`type`,`code`)
);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
