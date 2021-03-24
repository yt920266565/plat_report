/*!40101 SET NAMES utf8mb4 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

USE `publish`;

/*Data for the table `erp_dictionary` */

insert  into `erp_dictionary`(`type`,`code`,`name`,`sort`) values 
('client','ALL','不区分客户',10),
('client','SMS','深圳住友',20),
('client','SMV','越南住友',30),
('client','LKJ','江西立讯',40),
('client','MKZ','珠海紫翔',50),
('client','MKS','苏州紫翔',60),
('client','MKQ','台湾紫翔',70),
('client','MFS','苏州维信',80),
('client','MFY','盐城维信',90),
('client','YPE','永丰电子',100),
('client','FJS','上海藤仓',110),
('client','FJT','泰国藤仓',120),
('client','ARS','深圳富葵',130),
('client','FXS','深圳富士康',140),
('client','FXK','昆山富士康',150),
('client','CRS','苏州嘉联益',160),
('client','CRK','昆山嘉联益',170),
('client','OFN','南昌欧菲光',180),
('client','LGK','韩国LGD',190),
('client','LGY','烟台LGD',200),
('client','AFS','上海安费诺',210),
('client','ARQ','秦皇島宏启胜',220),
('client','ARH','淮安庆鼎',230),
('client','AFH','海盐安费诺',240),
('client','WWD','东莞维沃',250),
('client','VICQ','苏州胜谦进',260),
('client','TPK','厦门宸鸿',270),
('client','JDI','Japan Display Inc.',280),
('client','SPJ','Sharp corporation',290),
('client','GIS','成都業成',300),
('client','FYS','苏州复扬',310),
('client','FLD','东莞富强',320),
('client','DFC','常熟达富',330),
('client','FMC','昆山淳华',340),
('client','ASET','台湾ASE',350),
('client','ACC','常州瑞声',360),
('client','YMH','香港燕麦',370),
('client','SG','美国SG',380);

insert  into `erp_dictionary`(`type`,`code`,`name`,`sort`) values 
('product','Button','Button',10),
('product','compass','compass',20),
('product','Cyclone','Cyclone',30),
('product','D-Flex','D-Flex',40),
('product','Dock','Dock',50),
('product','F2','F2',60),
('product','Fargo','Fargo',70),
('product','Google','Google',80),
('product','Guass','Guass',90),
('product','Huawei','Huawei',100),
('product','I-Flex','I-Flex',110),
('product','J1','J1',120),
('product','Left Button','Left Button',130),
('product','Mesa','Mesa',140),
('product','Other','Other',150),
('product','Pad','Pad',160),
('product','Proteus','Proteus',170),
('product','R-camera','R-camera',180),
('product','RFP','RFP',190),
('product','Right Button','Right Button',200),
('product','Saiss','Saiss',210),
('product','Sensor','Sensor',220),
('product','SIM','SIM',230),
('product','Smyrna','Smyrna',240),
('product','Speaker','Speaker',250),
('product','Strobe','Strobe',260),
('product','Tesla','Tesla',270),
('product','T-Flex','T-Flex',280),
('product','Thalassa','Thalassa',290),
('product','Touch Panel','Touch Panel',300),
('product','Touch sensor','Touch sensor',310),
('product','UAT','UAT',320),
('product','U-Flex','U-Flex',330),
('product','Watch','Watch',340),
('product','Wifi','Wifi',350);

insert  into `erp_dictionary`(`type`,`code`,`name`,`sort`) values 
('test','RF','射频测试',10),
('test','LEAK','漏音测试',20),
('test','AIRLEAK','气密性测试',30),
('test','VPP','灵敏度测试',40),
('test','MIC','麦克风测试',50),
('test','FUNC','功能测试',60);

/*Data for the table `sm_function` */

insert  into `sm_function`(`id`,`name`,`description`,`url`,`method`,`pid`) values 
(0,'version.admin','版本管理',NULL,NULL,NULL),
(10,'version.apply','版本申请','/versions','POST',0),
(20,'version.test ','版本测试','/versions/*/test','POST',0),
(30,'version.publish','版本发布','/versions/*/publish','POST',0),
(40,'version.approve','版本审批','/versions/*/approve','POST',0),
(50,'version.maintenance','版本回退','/versions/*/rollback','POST',0),
(60,'version.maintenance','版本维护','/versions/*/maintenance','POST',0),
(70,'version.published.query','发布版本查询','/versions/published','GET',0),
(80,'version.published.download','版本文件下载','/versions/*/files/*','GET',0),
(1000,'system.admin','系统管理',NULL,NULL,NULL),
(1010,'user.admin','用户管理',NULL,NULL,1000),
(1020,'user.post','新增用户','/users','POST',1010),
(1030,'user.put','修改用户','/users','PUT',1010),
(1040,'user.delete','删除用户','/users','DELETE',1010),
(1100,'role.admin','角色管理',NULL,NULL,1000), 
(1110,'role.post','新增角色','/roles','POST',1100),
(1120,'role.put','修改角色','/roles','PUT',1100),
(1130,'role.delete','删除角色','/roles','DELETE',1100),
(1140,'role.users','分配用户','/roles/*/users','PUT',1100),
(1200,'data.maintenance','数据维护',NULL,NULL,1000),
(1210,'data.department','同步部门','/data/departments','PUT',1200)；

/*Data for the table `sm_role` */

insert  into `sm_role`(`id`,`name`,`description`,`createTime`) values 
(-1,'adminstrators','系统管理员','2020-03-06 09:16:18'),

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
