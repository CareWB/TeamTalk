-- MySQL dump 10.14  Distrib 5.5.50-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: teamtalk
-- ------------------------------------------------------
-- Server version	5.5.50-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `IMAdmin`
--

DROP TABLE IF EXISTS `IMAdmin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMAdmin` (
  `id` mediumint(6) unsigned NOT NULL AUTO_INCREMENT,
  `uname` varchar(40) NOT NULL COMMENT '用户名',
  `pwd` char(32) NOT NULL COMMENT '密码',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '用户状态 0 :正常 1:删除 可扩展',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间´',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间´',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMAdmin`
--

LOCK TABLES `IMAdmin` WRITE;
/*!40000 ALTER TABLE `IMAdmin` DISABLE KEYS */;
INSERT INTO `IMAdmin` VALUES (1,'admin','21232f297a57a5a743894a0e4a801fc3',0,0,0);
/*!40000 ALTER TABLE `IMAdmin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMAudio`
--

DROP TABLE IF EXISTS `IMAudio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMAudio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fromId` int(11) unsigned NOT NULL COMMENT '发送者Id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收者Id',
  `path` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '语音存储的地址',
  `size` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '文件大小',
  `duration` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '语音时长',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_fromId_toId` (`fromId`,`toId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMAudio`
--

LOCK TABLES `IMAudio` WRITE;
/*!40000 ALTER TABLE `IMAudio` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMAudio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMDepart`
--

DROP TABLE IF EXISTS `IMDepart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMDepart` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `departName` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '部门名称',
  `priority` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '显示优先级',
  `parentId` int(11) unsigned NOT NULL COMMENT '上级部门id',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '状态',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_departName` (`departName`),
  KEY `idx_priority_status` (`priority`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMDepart`
--

LOCK TABLES `IMDepart` WRITE;
/*!40000 ALTER TABLE `IMDepart` DISABLE KEYS */;
INSERT INTO `IMDepart` VALUES (1,'tt',0,0,0,1482030217,1482030217);
/*!40000 ALTER TABLE `IMDepart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMDiscovery`
--

DROP TABLE IF EXISTS `IMDiscovery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMDiscovery` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `itemName` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '名称',
  `itemUrl` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'URL',
  `itemPriority` int(11) unsigned NOT NULL COMMENT '显示优先级',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '状态',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_itemName` (`itemName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMDiscovery`
--

LOCK TABLES `IMDiscovery` WRITE;
/*!40000 ALTER TABLE `IMDiscovery` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMDiscovery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroup`
--

DROP TABLE IF EXISTS `IMGroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '群名称',
  `avatar` varchar(256) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '群头像',
  `creator` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建者用户id',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '群组类型，1-固定;2-临时群',
  `userCnt` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '成员人数',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否删除,0-正常，1-删除',
  `version` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '群版本号',
  `lastChated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '最后聊天时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`(191)),
  KEY `idx_creator` (`creator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroup`
--

LOCK TABLES `IMGroup` WRITE;
/*!40000 ALTER TABLE `IMGroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMember`
--

DROP TABLE IF EXISTS `IMGroupMember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMember` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '群Id',
  `userId` int(11) unsigned NOT NULL COMMENT '用户id',
  `status` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '是否退出群，0-正常，1-已退出',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_userId_status` (`groupId`,`userId`,`status`),
  KEY `idx_userId_status_updated` (`userId`,`status`,`updated`),
  KEY `idx_groupId_updated` (`groupId`,`updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户和群的关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMember`
--

LOCK TABLES `IMGroupMember` WRITE;
/*!40000 ALTER TABLE `IMGroupMember` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMember` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_0`
--

DROP TABLE IF EXISTS `IMGroupMessage_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_0`
--

LOCK TABLES `IMGroupMessage_0` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_1`
--

DROP TABLE IF EXISTS `IMGroupMessage_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_1`
--

LOCK TABLES `IMGroupMessage_1` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_2`
--

DROP TABLE IF EXISTS `IMGroupMessage_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_2`
--

LOCK TABLES `IMGroupMessage_2` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_2` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_3`
--

DROP TABLE IF EXISTS `IMGroupMessage_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_3` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_3`
--

LOCK TABLES `IMGroupMessage_3` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_3` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_3` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_4`
--

DROP TABLE IF EXISTS `IMGroupMessage_4`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_4` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_4`
--

LOCK TABLES `IMGroupMessage_4` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_4` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_4` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_5`
--

DROP TABLE IF EXISTS `IMGroupMessage_5`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_5` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_5`
--

LOCK TABLES `IMGroupMessage_5` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_5` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_5` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_6`
--

DROP TABLE IF EXISTS `IMGroupMessage_6`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_6` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_6`
--

LOCK TABLES `IMGroupMessage_6` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_6` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_6` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMGroupMessage_7`
--

DROP TABLE IF EXISTS `IMGroupMessage_7`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMGroupMessage_7` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `userId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '消息内容',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '2' COMMENT '群消息类型,101为群语音,2为文本',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_groupId_status_created` (`groupId`,`status`,`created`),
  KEY `idx_groupId_msgId_status_created` (`groupId`,`msgId`,`status`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IM群消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMGroupMessage_7`
--

LOCK TABLES `IMGroupMessage_7` WRITE;
/*!40000 ALTER TABLE `IMGroupMessage_7` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMGroupMessage_7` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMHotel`
--

DROP TABLE IF EXISTS `IMHotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMHotel` (
  `id` int(11) unsigned NOT NULL DEFAULT '1' COMMENT 'id',
  `cityCode` varchar(5) COLLATE utf8mb4_bin NOT NULL COMMENT '所属城市编码',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `score` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '评分',
  `tags` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '标签',
  `mustSee` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '是否必去',
  `url` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '介绍网址',
  `class` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '等级',
  `price` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '价格',
  `distance` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '离景点距离',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0 is ok',
  `lng` varchar(32) COLLATE utf8mb4_bin DEFAULT '0',
  `lat` varchar(32) COLLATE utf8mb4_bin DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_cityCode_name_class` (`cityCode`,`name`,`class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMHotel`
--

LOCK TABLES `IMHotel` WRITE;
/*!40000 ALTER TABLE `IMHotel` DISABLE KEYS */;
INSERT INTO `IMHotel` VALUES (1,'XMN','厦门鹭江宾馆',5,'经济型',1,'/hotel/429225.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_1','1',800,100,0,'118.08133897483','24.459952827965'),(2,'XMN','厦门永丽达花园酒店',5,'经济型',1,'/hotel/6112181.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_2','1',398,100,0,'118.090092','24.460786'),(3,'XMN','厦门丽斯海景酒店',5,'经济型',1,'/hotel/890106.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_3','1',366,100,0,'118.15293251428','24.44051429389'),(4,'XMN','厦门泰谷酒店',5,'经济型',1,'/hotel/346248.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_4','1',426,100,0,'118.08686109776','24.455731088117'),(5,'XMN','厦门美仑方之缘酒店',5,'经济型',1,'/hotel/432094.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_5','1',229,100,0,'118.08568288176','24.464575295582'),(6,'XMN','厦门金后酒店',5,'经济型',1,'/hotel/436708.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_6','1',427,100,0,'118.0919710734','24.462273488863'),(7,'XMN','厦门美仑金悦酒店',5,'经济型',1,'/hotel/467592.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_7','1',185,100,0,'118.10929502745','24.474656567214'),(8,'XMN','厦门圣希罗酒店',4,'经济型',1,'/hotel/469816.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_8','1',239,100,0,'118.14022531817','24.49936371904'),(9,'XMN','厦门华侨大厦',4,'经济型',1,'/hotel/419463.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_9','1',500,100,0,'118.09235833749','24.461672323149'),(10,'XMN','厦门海旅温德姆至尊酒店',5,'经济型',1,'/hotel/5548882.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_10','1',699,100,0,'118.047894','24.461849'),(11,'XMN','厦门香克斯酒店',5,'经济型',1,'/hotel/2492767.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_11','1',204,100,0,'118.13207301615','24.475150425495'),(12,'XMN','厦门鼓浪屿鼓浪别墅酒店',4,'经济型',1,'/hotel/434318.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_12','1',548,100,0,'118.06669049142','24.44851629348'),(13,'XMN','厦门米仑酒店',5,'经济型',1,'/hotel/429062.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_13','1',154,100,0,'118.10412690025','24.476015941784'),(14,'XMN','斯铂瑞城市酒店（厦门中山路步行街店）',5,'经济型',1,'/hotel/2297567.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_14','1',129,100,0,'118.092587','24.462453'),(15,'XMN','厦门鼓浪屿云之晨海景花园酒店',5,'经济型',1,'/hotel/2636318.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_15','1',578,100,0,'118.07502894422','24.453503038284'),(16,'XMN','厦门海岸国际酒店',5,'经济型',1,'/hotel/385410.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_16','1',448,100,0,'118.07994436863','24.46292428339'),(17,'XMN','厦门鼓浪屿南瓜石客栈',5,'经济型',1,'/hotel/481408.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_17','1',226,100,0,'118.07450068483','24.450889521696'),(18,'XMN','慢生活·厦门雅厝别院',5,'经济型',1,'/hotel/436704.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_18','1',369,100,0,'118.1324216002','24.431201085835'),(19,'XMN','厦门隐舍轻奢海景度假酒店',5,'经济型',1,'/hotel/3803569.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_19','1',358,100,0,'118.164404','24.448696'),(20,'XMN','厦门睿弘城际酒店',4,'经济型',1,'/hotel/429460.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_20','1',161,100,0,'118.08340758021','24.461186940247'),(21,'XMN','厦门罗约海滨温泉酒店',5,'经济型',1,'/hotel/1914691.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_21','1',500,100,0,'118.06299129356','24.561339834977'),(22,'XMN','厦门鼓浪屿磐诺假日酒店',5,'经济型',1,'/hotel/482751.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_22','1',500,100,0,'118.06644314746','24.452316100955'),(23,'XMN','厦门伊禾酒店',5,'经济型',1,'/hotel/3243942.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_23','1',216,100,0,'118.08868348823','24.462227871009'),(24,'XMN','成旅晶赞酒店（厦门枫悦店）（原枫悦大酒店）',5,'经济型',1,'/hotel/392553.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_24','1',388,100,0,'118.11914180451','24.499507207697'),(25,'XMN','厦门君怡酒店',4,'经济型',1,'/hotel/2290014.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_25','1',269,100,0,'118.13788460133','24.509565599733'),(26,'SZX','深圳中南海怡酒店',4,'经济型',1,'/hotel/457399.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_1','1',2688,100,0,'114.03056523702','22.543359331786'),(27,'SZX','深圳楚天大酒店',4,'经济型',1,'/hotel/450229.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_2','1',315,100,0,'114.05396999166','22.533495410446'),(28,'SZX','深圳华强北和颐酒店',5,'经济型',1,'/hotel/2030013.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_3','1',617,100,0,'114.0950990846','22.546284804733'),(29,'SZX','深圳长城大酒店',5,'经济型',1,'/hotel/654875.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_4','1',553,100,0,'114.11334088837','22.55632600782'),(30,'SZX','深圳百合酒店',5,'经济型',1,'/hotel/456141.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_5','1',485,100,0,'114.1268688034','22.605121760894'),(31,'SZX','深圳景田酒店',4,'经济型',1,'/hotel/533870.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_6','1',731,100,0,'114.05101827218','22.558538330897'),(32,'SZX','深圳福瑞诗酒店',4,'经济型',1,'/hotel/1462776.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_7','1',283,100,0,'114.09009148453','22.544187709502'),(33,'SZX','桔子酒店·精选（深圳罗湖店）',5,'经济型',1,'/hotel/2247932.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_8','1',452,100,0,'114.132984','22.550489'),(34,'SZX','深圳罗湖口岸和颐酒店',5,'经济型',1,'/hotel/1355586.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_9','1',384,100,0,'114.12890472888','22.54008865966'),(35,'SZX','深圳大梅沙湾游艇度假酒店',5,'经济型',1,'/hotel/433905.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_10','1',593,100,0,'114.31861935014','22.603937249785'),(36,'SZX','深圳华强北华联宾馆',4,'经济型',1,'/hotel/662468.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_11','1',431,100,0,'114.10045076533','22.547445622596'),(37,'SZX','深圳凯美豪盛酒店',4,'经济型',1,'/hotel/426195.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_12','1',258,100,0,'114.12753716467','22.539502949364'),(38,'SZX','迎商酒店（深圳东门店）',5,'经济型',1,'/hotel/5153544.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_13','1',256,100,0,'114.13689153091','22.55525519337'),(39,'SZX','深圳发展中心酒店',5,'经济型',1,'/hotel/6037590.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_14','1',638,100,0,'114.125481','22.544957'),(40,'SZX','皇朝商务酒店（深圳皇岗口岸店）',4,'经济型',1,'/hotel/486124.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_15','1',276,100,0,'114.078089','22.530453'),(41,'SZX','深港商务公寓（深圳地王大厦店）',5,'经济型',1,'/hotel/5758334.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_16','1',283,100,0,'114.118503','22.55003'),(42,'SZX','龙泉酒店（深圳北站店）',4,'经济型',1,'/hotel/1512604.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_17','1',259,100,0,'114.03306398681','22.651120898877'),(43,'SZX','深圳加来酒店',4,'经济型',1,'/hotel/1436676.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_18','1',438,100,0,'114.06570753193','22.525594806851'),(44,'SZX','深圳怡景湾大酒店',4,'经济型',1,'/hotel/486533.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_19','1',326,100,0,'114.22137726405','22.725085026581'),(45,'SZX','美高花园服务公寓（深圳南海大道店）',4,'经济型',1,'/hotel/429965.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_20','1',403,100,0,'113.92696141395','22.502215738679'),(46,'SZX','深港公寓（深圳科技园店）',4,'经济型',1,'/hotel/5148182.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_21','1',276,100,0,'113.958781','22.547114'),(47,'SZX','深圳华强北华霆酒店（原华霆酒店）',4,'经济型',1,'/hotel/435400.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_22','1',538,100,0,'114.10203644563','22.549314449407'),(48,'SZX','深圳四川宾馆',4,'经济型',1,'/hotel/925657.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_23','1',422,100,0,'114.10159718449','22.554866619618'),(49,'SZX','她他国际公寓（深圳One39店）',5,'经济型',1,'/hotel/5815709.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_24','1',384,100,0,'114.118204','22.549718'),(50,'SZX','呼噜栈酒店（深圳中信店）',4,'经济型',1,'/hotel/536405.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_25','1',229,100,0,'114.10694443663','22.547551582111'),(51,'CAN','美豪丽致酒店（广州五羊新城店）',5,'经济型',1,'/hotel/371132.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_1','1',598,100,0,'113.31857937413','23.126410383'),(52,'CAN','碧桂园空港凤凰酒店（广州新白云机场店）',5,'经济型',1,'/hotel/1431478.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_2','1',399,100,0,'113.311294','23.441497'),(53,'CAN','星伦万达广场主题公寓（广州长隆店）',5,'经济型',1,'/hotel/4496105.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_3','1',149,100,0,'113.35704641324','23.013145180818'),(54,'CAN','广州粤大金融城国际酒店',5,'经济型',1,'/hotel/4547905.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_4','1',550,100,0,'113.37925319767','23.125607918939'),(55,'CAN','尚品假日酒店（广州新机场店）',4,'经济型',1,'/hotel/2606606.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_5','1',229,100,0,'113.296871','23.368921'),(56,'CAN','星伦保利中汇国际公寓（广州火车东站店）（原伦凯保利国际公寓）',5,'经济型',1,'/hotel/4590350.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_6','1',348,100,0,'113.329761','23.154224'),(57,'CAN','嘻哈商务公寓(广州琶洲保利世贸店)',4,'经济型',1,'/hotel/789204.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_7','1',219,100,0,'113.3733735494','23.101818156468'),(58,'CAN','柏高酒店（广州龙口西岗顶地铁站店）',5,'经济型',1,'/hotel/449125.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_8','1',328,100,0,'113.342617','23.143456'),(59,'CAN','雅朵童趣公寓（广州番禺万达汉溪长隆地铁站店）',5,'经济型',1,'/hotel/1917651.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_9','1',125,100,0,'113.35567307344','23.011855183388'),(60,'CAN','广州良友启程酒店',5,'经济型',1,'/hotel/4105260.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_10','1',232,100,0,'113.34968492921','23.13399856057'),(61,'CAN','迎商·雅兰酒店（广州北京路店）',4,'经济型',1,'/hotel/647570.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_11','1',318,100,0,'113.273164','23.135596'),(62,'CAN','迎商酒店（广州客村地铁站敦和店）',4,'经济型',1,'/hotel/2728682.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_12','1',177,100,0,'113.32311095231','23.095010384634'),(63,'CAN','广州同吉假日酒店',4,'经济型',1,'/hotel/1418729.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_13','1',575,100,0,'113.35070843408','23.132374062689'),(64,'CAN','港润粤北酒店（广州世贸中心店）（原粤北酒店）',4,'经济型',1,'/hotel/512229.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_14','1',311,100,0,'113.29025246338','23.137772015258'),(65,'CAN','柏高酒店（广州天河北水荫路店）（原天河北东风公园西门店）',5,'经济型',1,'/hotel/2582285.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_15','1',298,100,0,'113.31808252375','23.148924580854'),(66,'CAN','嘻哈英伦亲子度假公寓（广州番禺万达店）',5,'经济型',1,'/hotel/2725409.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_16','1',128,100,0,'113.3555948831','23.014089480702'),(67,'CAN','易成国际酒店公寓（广州东站保利中汇店）',5,'经济型',1,'/hotel/471190.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_17','1',355,100,0,'113.33008773462','23.153780124673'),(68,'CAN','广州富豪酒店（北京路店）',4,'经济型',1,'/hotel/468056.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_18','1',219,100,0,'113.27863693475','23.133350056288'),(69,'CAN','鑫余之家服务公寓（广州汉溪长隆店）',5,'经济型',1,'/hotel/4905091.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_19','1',666,100,0,'113.34636002011','23.003845757271'),(70,'CAN','易成国际酒店公寓（广州东站威尼国际店）',4,'经济型',1,'/hotel/482661.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_20','1',249,100,0,'113.32868431451','23.156732160814'),(71,'CAN','广州伊莲·萨维尔国际酒店公寓',4,'经济型',1,'/hotel/4657816.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_21','1',380,100,0,'113.34918482285','23.123691958235'),(72,'CAN','柏高商务酒店（广州东站沙河服装城店）',5,'经济型',1,'/hotel/511399.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_22','1',376,100,0,'113.31449846929','23.161803474894'),(73,'CAN','广州恒丰酒店',4,'经济型',1,'/hotel/3418826.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_23','1',236,100,0,'113.31284','23.034986'),(74,'CAN','柏高商务酒店（广州江泰路地铁站店）',4,'经济型',1,'/hotel/449118.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_24','1',227,100,0,'113.2860935948','23.089943804041'),(75,'CAN','星伦国际公寓（广州合生广场店）（原凯迪国际公寓）',4,'经济型',1,'/hotel/6547745.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_25','1',208,100,0,'113.321467','23.091736');
/*!40000 ALTER TABLE `IMHotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_0`
--

DROP TABLE IF EXISTS `IMMessage_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_0`
--

LOCK TABLES `IMMessage_0` WRITE;
/*!40000 ALTER TABLE `IMMessage_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_1`
--

DROP TABLE IF EXISTS `IMMessage_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_1`
--

LOCK TABLES `IMMessage_1` WRITE;
/*!40000 ALTER TABLE `IMMessage_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_2`
--

DROP TABLE IF EXISTS `IMMessage_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_2`
--

LOCK TABLES `IMMessage_2` WRITE;
/*!40000 ALTER TABLE `IMMessage_2` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_3`
--

DROP TABLE IF EXISTS `IMMessage_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_3` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_3`
--

LOCK TABLES `IMMessage_3` WRITE;
/*!40000 ALTER TABLE `IMMessage_3` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_3` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_4`
--

DROP TABLE IF EXISTS `IMMessage_4`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_4` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_4`
--

LOCK TABLES `IMMessage_4` WRITE;
/*!40000 ALTER TABLE `IMMessage_4` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_4` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_5`
--

DROP TABLE IF EXISTS `IMMessage_5`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_5` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_5`
--

LOCK TABLES `IMMessage_5` WRITE;
/*!40000 ALTER TABLE `IMMessage_5` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_5` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_6`
--

DROP TABLE IF EXISTS `IMMessage_6`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_6` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_6`
--

LOCK TABLES `IMMessage_6` WRITE;
/*!40000 ALTER TABLE `IMMessage_6` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_6` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMMessage_7`
--

DROP TABLE IF EXISTS `IMMessage_7`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMMessage_7` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateId` int(11) unsigned NOT NULL COMMENT '用户的关系id',
  `fromId` int(11) unsigned NOT NULL COMMENT '发送用户的id',
  `toId` int(11) unsigned NOT NULL COMMENT '接收用户的id',
  `msgId` int(11) unsigned NOT NULL COMMENT '消息ID',
  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '消息内容',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '消息类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_relateId_status_created` (`relateId`,`status`,`created`),
  KEY `idx_relateId_status_msgId_created` (`relateId`,`status`,`msgId`,`created`),
  KEY `idx_fromId_toId_created` (`fromId`,`toId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMMessage_7`
--

LOCK TABLES `IMMessage_7` WRITE;
/*!40000 ALTER TABLE `IMMessage_7` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMMessage_7` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPlayDetail`
--

DROP TABLE IF EXISTS `IMPlayDetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPlayDetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `travelBasicId` int(11) unsigned NOT NULL COMMENT 'IMTravelBasicInfo id',
  `type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '1-景点 2-酒店',
  `itemId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'id',
  `dayTimeFrom` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '2017-02-07 18:00',
  `dayTimeTo` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '2017-02-08 12:00',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0 is ok',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPlayDetail`
--

LOCK TABLES `IMPlayDetail` WRITE;
/*!40000 ALTER TABLE `IMPlayDetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMPlayDetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMRecentSession`
--

DROP TABLE IF EXISTS `IMRecentSession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMRecentSession` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) unsigned NOT NULL COMMENT '用户id',
  `peerId` int(11) unsigned NOT NULL COMMENT '对方id',
  `type` tinyint(1) unsigned DEFAULT '0' COMMENT '类型，1-用户,2-群组',
  `status` tinyint(1) unsigned DEFAULT '0' COMMENT '用户:0-正常, 1-用户A删除,群组:0-正常, 1-被删除',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_userId_peerId_status_updated` (`userId`,`peerId`,`status`,`updated`),
  KEY `idx_userId_peerId_type` (`userId`,`peerId`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMRecentSession`
--

LOCK TABLES `IMRecentSession` WRITE;
/*!40000 ALTER TABLE `IMRecentSession` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMRecentSession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMRelationShip`
--

DROP TABLE IF EXISTS `IMRelationShip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMRelationShip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `smallId` int(11) unsigned NOT NULL COMMENT '用户A的id',
  `bigId` int(11) unsigned NOT NULL COMMENT '用户B的id',
  `status` tinyint(1) unsigned DEFAULT '0' COMMENT '用户:0-正常, 1-用户A删除,群组:0-正常, 1-被删除',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_smallId_bigId_status_updated` (`smallId`,`bigId`,`status`,`updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMRelationShip`
--

LOCK TABLES `IMRelationShip` WRITE;
/*!40000 ALTER TABLE `IMRelationShip` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMRelationShip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMScenic`
--

DROP TABLE IF EXISTS `IMScenic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMScenic` (
  `id` int(11) unsigned NOT NULL DEFAULT '1' COMMENT 'id',
  `cityCode` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '所属城市编码',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `score` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '评分',
  `tags` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '标签',
  `free` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '是否免费',
  `mustSee` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '是否必去',
  `url` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '介绍网址',
  `class` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '等级',
  `playTime` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '游玩时长',
  `price` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '价格',
  `bestTimeFrom` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '最佳游戏开始时间',
  `bestTimeTo` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '最佳游戏结束时间',
  `lng` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '经度',
  `lat` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '纬度',
  `address` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '地址',
  `desc` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT '介绍',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0 is ok',
  PRIMARY KEY (`id`),
  KEY `idx_cityCode_name_class` (`cityCode`,`name`,`class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMScenic`
--

LOCK TABLES `IMScenic` WRITE;
/*!40000 ALTER TABLE `IMScenic` DISABLE KEYS */;
INSERT INTO `IMScenic` VALUES (1,'XMN','鼓浪屿',4,'海边',0,1,'gulangyu','1',3,75,'09:00','17:00','118.07348603976','24.452265001262','福建省厦门市思明区','鼓浪屿是厦门最大的一个卫星岛之一，由于历史原因，中外风格各异的建筑物在此地被完好地汇集、保留，现成为著名的风景区。',0),(2,'XMN','厦门大学',4,'学校',0,1,'xiamendaxue','1',3,0,'09:00','17:00','118.10477402066','24.443314045528','厦门市思明区思明南路422号','被誉为“中国最美丽的校园之一，校园依山傍海。',0),(3,'XMN','南普陀寺',4,'宗教场所',0,1,'nanputuosi','1',3,100,'09:00','17:00','118.10273432817','24.446569267095','福建省 厦门市 思明区思明南路515号','香火很旺盛，据说是一个很灵验的寺庙，游人络绎不绝。建筑精美。',0),(4,'XMN','环岛路',5,'海边',0,1,'huandaolu','1',3,0,'09:00','17:00','118.11280163802','24.437136933926','思明区环岛路沿线','厦门集旅游观光和休闲娱乐为一体的滨海走廊，风景优美，沙滩也很细腻。',0),(5,'XMN','日光岩',4,'其他',0,1,'riguangyan','1',3,102,'09:00','17:00','118.07396672304','24.448610748152','中国福建省厦门市思明区晃右路晃岩路56-64','由两块巨石一竖一横相倚而立，海拔92.7米，为鼓浪屿最高峰。',0),(6,'XMN','曾厝垵',4,'乡村',0,1,'zengcuoan','1',3,0,'09:00','17:00','118.13209697087','24.433600015683','福建省厦门市思明区','曾厝是厦门最美的渔村，交通便利，比较安静和悠闲，商业气氛很浓。 ',0),(7,'XMN','中山路',4,'其他',0,1,'zhongshanlu','1',3,15,'09:00','17:00','118.08745774467','24.460389565619',' 福建省厦门市思明区','中山路的中华城晚上特别美丽，有着浓郁的南洋风情，给人感觉很干净。',0),(8,'XMN','厦门海底世界',4,'展馆',0,1,'xiamenhaidishijie','1',3,81,'09:00','17:00','118.07783895877','24.452080054354',' 厦门市思明区鼓浪屿龙头路2号','厦门海底世界坐落在鼓浪屿东岸黄家渡，原为鼓浪屿公园，紧靠轮渡码头。 ',0),(9,'XMN','菽庄花园',4,'其他',0,1,'shuzhuanghuayuan','1',3,0,'09:00','17:00','118.07589116293','24.445179340157','思明区鼓浪屿港仔後路7号','既有江南园林的精巧，又有波涛汹涌的壮观。空气挺清新的，美景相互映衬。 ',0),(10,'XMN','胡里山炮台',4,'历史遗址',0,1,'hulishanpaotai','1',3,23,'09:00','17:00','118.11300779913','24.435091234888',' 福建省厦门市思明区胡里山上','胡里山炮台位于福建厦门岛东南，毗邻厦门大学园区。学生有优惠，门票便宜。',0),(11,'XMN','钢琴博物馆',4,'展馆',0,1,'gangqinbowuguan','1',3,0,'09:00','17:00','118.07608708336','24.444704322226','福建省厦门市思明区港后路7号','鼓浪屿钢琴博物馆堪称世界一流，里面的钢琴很漂亮。历史都很悠久。',0),(12,'XMN','集美学村',4,'乡村',0,1,'jimeixuecun','1',3,24,'09:00','17:00','118.09909259763','24.572565292416',' 福建省 厦门市 集美区嘉庚路','集美学村是传统的闽南建筑，还附有悠久的历史感，而且中国最美的学村。 ',0),(13,'XMN','厦门台湾小吃街',4,'其他',0,1,'shamentaiwanxiaochijie','1',3,0,'09:00','17:00','118.08191196578','24.462763022543',' 厦门市思明区人和路','南方的美食就是比北方精致，康熙推荐过，而且很有特色。东西挺便宜。',0),(14,'XMN','皓月园',4,'公园',0,1,'haoyueyuan','1',3,102,'09:00','17:00','118.08246999316','24.447643055431','厦门思明区鼓浪屿东部覆鼎岩海滨','座落在鼓浪屿东南，濒临鹭江，是一座郑成功纪念园。',0),(15,'XMN','五老峰',4,'山峰',0,1,'xiamenwulaofeng','1',3,0,'09:00','17:00','118.10569604144','24.452376018617','厦门市思明区思明南路南普陀寺后','五老峰，得名于白云缭绕之时，其形状酷似五个须发皆白的老人翘首遥望茫茫大海。等山眺望，大海无边，俯瞰厦门市景，大有壮阔之感。',0),(16,'XMN','万石植物园',4,'自然保护区',0,1,'wanshizhiwuyuan','1',3,0,'09:00','17:00','118.10041104063','24.460121002962',' 厦门市思明区虎园路25号','空气清新，植物园面积很大，可以乘坐电瓶车游览，也可以爬山。 ',0),(17,'XMN','观音山',4,'海边',0,1,'xiamenguanyinshan','1',3,0,'09:00','17:00','118.01321638752','24.871223301562','位于厦门东部，紧临环岛路','',0),(18,'XMN','港仔后海滨浴场',4,'海边',0,1,'gangzihouhaibinyuchang','1',3,0,'09:00','17:00','118.07452367245','24.445975172063',' 福建厦门市鼓浪屿菽庄花园边','',0),(19,'XMN','鼓浪屿音乐厅',4,'现代建筑',0,1,'gulangyuyinleting','1',3,0,'09:00','17:00','118.07777401128','24.449423005546','福建省厦门市思明区鼓浪屿晃岩路1号','许多世界音乐名家和艺术团体曾来鼓浪屿音乐厅演出，很浪漫的地方。 ',0),(20,'XMN','白鹭洲公园',4,'公园',0,1,'bailuzhougongyuan','1',3,0,'09:00','17:00','118.09606800298','24.478968814064','厦门市白鹭洲路','内有400只从荷兰引进的广场鸽，这里是厦门最大的全开放广场公园。',0),(21,'XMN','沙雕文化公园',4,'公园',0,1,'shadiaowenhuagongyuan','1',3,0,'09:00','17:00','118.20524985124','24.49990620025',' 思明区环岛路观音山商业街21号','亚洲最大的沙雕文化公园，每年4月份举办全国沙滩排球锦标赛。',0),(22,'XMN','白城沙滩',4,'海边',0,1,'baichengshatan','1',3,0,'09:00','17:00','118.109838','24.437918','环岛南路(近胡里山炮台)','环境还不错，是挺漂亮的。是个不错的地方。交通很方便。',0),(23,'XMN','厦门大桥',4,'现代建筑',0,1,'xiamendaqiao','1',3,0,'09:00','17:00','118.10802301203','24.563718057677',' 厦门市集美区和湖里区中间','我国第一座跨越海峡的公路大桥，每当夜幕来临，大桥上灯火辉煌，景色迷人。',0),(24,'XMN','郑成功纪念馆',4,'历史建筑',0,1,'zhengchenggongjinianguan','1',3,0,'09:00','17:00','118.07333009392','24.449013382427','福建省厦门市鼓浪屿日光岩','郑成功纪念馆是1962年为纪念郑成功收复台湾300周年而建立。',0),(25,'XMN','陈嘉庚纪念胜地',4,'公园',0,1,'jimeichenjiagengjinianshengdi','1',3,24,'09:00','17:00','118.11194501388','24.571786044702','福建厦门市集美区嘉庚路149号','陈嘉庚：一个生前曾被毛泽东称誉为“华侨旗帜、民族光辉”。厦门大学、集美大学（前身为集美学村各校）两校师生都尊称其为“校主”的人。',0),(26,'XMN','野山谷',4,'山峰',0,1,'yeshangu','1',3,45,'09:00','17:00','118.0546870207','24.879610022564','厦门市同安小坪与安溪龙门隧道交界处','有“闽南绿肺”之称，有原生态自然、千亩茶园，诗画美景尽现。 ',0),(27,'XMN','海湾公园',4,'公园',0,1,'haiwangongyuan','1',3,0,'09:00','17:00','118.08303089512','24.479877313914',' 海湾公园位于厦门市中心区域西海岸','公园分为天园、地园、林园、草园、水花园、滨海风光和星光大道7大景区。',0),(28,'XMN','万国建筑博览',4,'历史建筑',0,1,'wanguojianzhubolan','1',3,0,'09:00','17:00','118.07509077272','24.453408064105',' ','这里是鼓浪屿中西文化交流的精萃景观。中国传统建筑，如宗教建筑、园林建筑、居民建筑等，在鼓浪屿都可以找到其影子。',0),(29,'XMN','日月谷温泉',4,'峡谷',0,1,'riyueguwenquan','1',3,348,'09:00','17:00','117.94823997373','24.565245017915','厦门市海沧区孚莲路1888-1889号','园区里有泉源文化区、明潭岛、孔雀园等景点可供游客观赏，互动体验。 ',0),(30,'XMN','厦门中山公园',4,'公园',0,1,'xiamenzhongshangongyuan','1',3,0,'09:00','17:00','118.09589822324','24.465097952174','厦门市思明区公园南路','一个集休闲、展览、科普、娱乐于一体的综合性文化公园，规模颇大、设计精巧、布局优美、中西合璧。',0),(31,'XMN','毓园',4,'公园',0,1,'yuyuan','1',3,0,'09:00','17:00','118.079660902','24.446999786851','福建厦门鼓浪屿东南部复兴路','毓园的建筑，布局自然雅致，园中立着林巧稚大夫的汉白玉雕像，。',0),(32,'XMN','世界名人蜡像馆',4,'现代建筑',0,1,'shijiemingrenlaxiangguan','1',3,42,'09:00','17:00','118.08153997747','24.450769053323',' 福建省厦门市思明区鹿礁路111号','蜡像塑造得非常逼真，人物比较丰富。是鼓浪屿众多景点中的一个。',0),(33,'XMN','天竺山国家森林公园',4,'公园',0,1,'tianzhushanguojiasenlingongyuan','1',3,0,'09:00','17:00','117.931991','24.586314','厦门市海沧区东孚镇洪塘村天竺山路1号','厦门后花园，奇岩怪石、湖光山色、唐代真寂寺遗址组成公园美景。',0),(34,'XMN','芙蓉隧道',4,'其他',0,1,'furongsuidao','1',3,0,'09:00','17:00','118.109187','24.441571','思明区思明南路422号厦门大学','色彩很好，对于这样文艺范的东西都很心动，不过隧道太长了。艺术气息浓厚。',0),(35,'XMN','厦门方特梦幻王国',4,'其他',0,1,'xiamenfangtemenghuanwangguo','1',3,205,'09:00','17:00','118.18755996999','24.688762014875','厦门市同安区石浔南路1111号（中洲大桥附近）','',0),(36,'XMN','厦门博物馆',4,'展馆',0,1,'xiamenbowuguan','1',3,0,'09:00','17:00','118.11629963967','24.497085761256','思明区鼓浪屿鼓新路43号','博物馆主楼八卦楼巍峨壮观，是近代厦门的标志性风貌建筑。',0),(37,'XMN','快乐谷',4,'公园',0,1,'kuailegu','1',3,60,'09:00','17:00','118.113879','24.512171','福建省厦门市湖里区华荣路1719号','既有惊险刺激的各种滑道，又有悠然自得的戏水天地，是老少皆宜的新型消暑开心乐园。',0),(38,'XMN','琴园',4,'公园',0,1,'qinyuan','1',3,0,'09:00','17:00','118.07143898116','24.447865016039',' 福建厦门市鼓浪屿英雄山','岛上的小吃真是多，很有特色，强烈推荐，地方很大很美，风景很不错。',0),(39,'XMN','厦门大学情人谷',4,'学校',0,1,'xiamendaxueqingrengu','1',3,0,'09:00','17:00','118.101747','24.440044','','厦大的情人真幸福，是厦门大学的最浪漫的地方之一。山顶水库，茂密丛林。',0),(40,'XMN','钢琴码头',4,'现代建筑',0,1,'gangqinmatou','1',3,0,'09:00','17:00','118.0799320106','24.45258999401','厦门市鼓浪屿岛东','如同一架三角钢琴的钢琴码头，是钢琴之岛迎接游人的起点，踏上码头的一瞬间，你将会感受到琴岛非同凡响的音乐气质。',0),(41,'XMN','鸿山公园',4,'公园',0,1,'hongshangongyuan','1',3,0,'09:00','17:00','118.09304161172','24.453629274336','福建省 厦门市 思明区思明南路中段','集名胜古迹、现代园林、游乐设施于一体，其中“鸿山织雨”奇观为厦门八大景之一。',0),(42,'XMN','风琴博物馆',4,'其他',0,1,'fengqinbowuguan','1',3,102,'09:00','17:00','118.07500902692','24.453428047053',' 福建省厦门市思明区鼓浪屿路43号','国内唯一一家，世界上最大的风琴博物馆，还是很有看头的。鼓浪屿特别美。',0),(43,'XMN','厦门轮渡码头',4,'其他',0,1,'xiamenlundumatou','1',3,0,'09:00','17:00','118.07926681537','24.46129877666',' 福建省厦门市思明区','',0),(44,'XMN','椰风寨',4,'其他',0,1,'yefengzhai','1',3,0,'09:00','17:00','118.17564295927','24.453911008434','福建省厦门市思明区环岛南路','海域很干净。很好玩的一个地方，是环岛路上一道美丽的风景。 ',0),(45,'XMN','观海园',4,'其他',0,1,'guanhaiyuan','1',3,0,'09:00','17:00','118.0780875199','24.44409796447','福建省厦门市鼓浪屿田尾路8号','园里有中国传统的园林幽处，又有西洋别墅的开敞风格，四季均有不同感受。',0),(46,'XMN','翠丰温泉',4,'休闲度假',0,1,'cuifengwenquan','1',3,0,'09:00','17:00','118.13816529408','24.798736802102','福建省厦门市同安区汀溪街777号','自然天成的山地景观与人工雕凿的植被景观浑然一体，温泉泡着很舒服。',0),(47,'XMN','厦门科技馆',4,'展馆',0,1,'xiamenkejiguan','1',3,70,'09:00','17:00','118.11446197609','24.496392027038','思明区体育路95号文化艺术中心内','服务很热情周到，而且科技馆真是很好玩，好玩的地方。',0),(48,'XMN','嘉庚公园',4,'公园',0,1,'jiagenggongyuan','1',3,24,'09:00','17:00','118.11283703125','24.573183018355','厦门市集美区嘉庚路24号','',0),(49,'XMN','铁路文化公园',4,'公园',0,1,'tieluwenhuagongyuan','1',3,0,'09:00','17:00','118.10047095761','24.461081033678','厦门市思明区虎园路（万石湖西北面）','',0),(50,'XMN','万石山',4,'其他',0,1,'wanshishan','1',3,0,'09:00','17:00','118.10056671697','24.451946339788','福建省 厦门市 思明区狮山北麓','',0),(51,'XMN','萤火虫主题公园',5,'其他',0,1,'yinghuochongzhutigongyuan','1',3,0,'09:00','17:00','118.13203498779','24.447766988283',' 福建厦门市文曾路怡情谷','',0),(52,'XMN','台湾民俗村',4,'公园',0,1,'xiamentaiwanminsucun','1',3,0,'09:00','17:00','118.14676773714','24.436500437508','思明区环岛路35号','',0),(53,'XMN','南湖公园',4,'公园',0,1,'xiamennanhugongyuan','1',3,0,'09:00','17:00','118.11301399744','24.485199016604','厦门新市区','',0),(54,'XMN','厦门铁道文化公园',4,'公园',0,1,'shamentiedaowenhuagongyuan','1',3,0,'09:00','17:00','118.10047095761','24.461081033678','厦门市思明区文屏路至和平码头之间','是由一条老铁路改造而成的公园。闽南红砖与砖红色木板铺地，伴铁轨而行，形成独具特色的步道',0),(55,'XMN','大嶝岛',4,'其他',0,1,'dadengdao','1',3,40,'09:00','17:00','118.33670248969','24.562201759743',' 福建厦门翔安区','',0),(56,'XMN','筼筜湖',4,'湖泊',0,1,'yundanghu','1',3,0,'09:00','17:00','118.09592966393','24.477489712788','厦门市思明区滨湖南路','厦门市区唯一的人工湖泊，湖泊中央是美丽的白鹭洲，鲜花、绿树、青草、曲径映衬着幢幢现代建筑，一派现代都市气象。',0),(57,'XMN','虎溪岩',4,'自然保护区',0,1,'huxiyan','1',3,0,'09:00','17:00','118.09799037677','24.453627629652','福建省厦门市思明区虎园路','巨岩下有一棱层洞，洞前石穴中流泉成溪，称\"虎溪\"。山上岩寺，也名虎溪岩寺。',0),(58,'XMN','湖里公园',4,'公园',0,1,'huligongyuan','1',3,0,'09:00','17:00','118.11274414646','24.51052316943','厦门市湖里区华泰路32号','',0),(59,'XMN','景州乐园',4,'公园',0,1,'jingzhouleyuan','1',3,0,'09:00','17:00','118.14456212762','24.436815355188','厦门市黄厝路35号','在风光秀丽的“黄金海岸”线上，一个高品位的大型游乐园映入眼帘。',0),(60,'XMN','五缘湾湿地公园',4,'公园',0,1,'wuyuanwanshidigongyuan','1',3,0,'09:00','17:00','118.1830580221','24.524177004236','湖里区五缘湾(近五缘湾大桥)','',0),(61,'SZX','深圳世界之窗',4,'其他',0,1,'shijiezhichuang','1',3,70,'09:00','17:00','113.97945104379','22.540135007455','深圳市南山区深南大道9037号','国家首批5A级旅游景区，世界各国经典景观的微缩再现，在中国的牌楼下，买一本“护照”，就可以开始“环球之旅”了。',0),(62,'SZX','深圳欢乐谷',4,'公园',0,1,'shenzhenhuanlegu','1',3,69,'09:00','17:00','113.98742602044','22.547864005004','深圳市南山区华侨城西街18号','亚太十大主题公园、国家首批5A级旅游景区，内有9大园区，是狂欢者的天堂。',0),(63,'SZX','大梅沙',4,'海边',0,1,'dameisha','1',3,165,'09:00','17:00','114.31373100531','22.598541001856',' 广东省 深圳市 盐田区大梅沙盐梅路9号','垃圾多多多多多，和朋友一起来玩最有意思，海水清澈，沙子也比较细腻。',0),(64,'SZX','东部华侨城',4,'公园',0,1,'dongbuhuaqiaocheng','1',3,578,'09:00','17:00','114.29973899869','22.623307019564','广东省深圳市盐田区艺海东路东部华侨城','国内首个集休闲度假、观光旅游、户外运动、科普教育、生态探险等主题于一体的大型综合性国家生态旅游示范区。',0),(65,'SZX','西冲',4,'海边',0,1,'xichong','1',3,10,'09:00','17:00','114.54082299748','22.478242027126','广东省深圳市龙岗区','西冲的海水很干净，海滩漂亮的哦，晚上在这边露营很舒服，很多人。',0),(66,'SZX','红树林',4,'自然保护区',0,1,'shenzhenhongshulin','1',3,0,'09:00','17:00','114.007884','22.532395','深圳市福田区滨海大道和深圳湾畔','我国面积最小的国家级自然保护区，深圳市的绿色长廊。',0),(67,'SZX','仙湖植物园',4,'自然保护区',0,1,'xianhuzhiwuyuan','1',3,0,'09:00','17:00','114.17711796102','22.581557967151',' 广东省深圳市罗湖区莲塘仙湖路160号','仙湖是一个还不错的地方，里面的风景很漂亮。门票价格记不清楚了。',0),(68,'SZX','东门',4,'其他',0,1,'dongmen','1',3,70,'09:00','17:00','114.1279390581','22.552365264207','深圳市罗湖区东门中路与解放路交界','东门老街位于深圳市罗湖区中心地段，真实地记录着深圳城市发展的一段历史。',0),(69,'SZX','小梅沙',4,'海边',0,1,'xiaomeisha','1',3,30,'09:00','17:00','114.331421','22.608613','广东省深圳市盐田区大鹏湾畔','环境不错，很美丽的地方，有迷人的海滩，早上的海水最清澈。',0),(70,'SZX','大鹏所城',4,'历史建筑',0,1,'dapengsuocheng','1',3,0,'09:00','17:00','114.52002102662','22.600061983893',' 深圳市龙岗区南门西路','原岭南海防军事要塞，古城中明清民居、青石小巷均值得一游。',0),(71,'SZX','地王观光',4,'观景台',0,1,'diwangguanguang','1',3,0,'09:00','17:00','114.11718364623','22.548875284653',' 深圳市罗湖区深南东路5002号信兴广场商业中心地王大厦第69层','景观一般，不过很壮观。门票稍贵，只是俯瞰深圳的美景，绝对的壮观。 ',0),(72,'SZX','海上世界',4,'其他',0,1,'haishangshijie','1',3,0,'09:00','17:00','113.92266600845','22.489356000209',' 广东省 深圳市 南山区太子路','以“明华轮”为中心，集休闲娱乐、餐饮购物、文化艺术于一体，一座应有尽有的国际滨海新城。\n',0),(73,'SZX','深圳园博园',4,'公园',0,1,'shenzhenyuanboyuan','1',3,43,'09:00','17:00','114.00984574969','22.544712960085','广东省深圳市福田区侨城东路深南大道路口','深圳国际园林花卉博览园（简称“园博园”）地处深圳市福田区竹子林西片，面积约66万平方米',0),(74,'SZX','深圳海洋世界',4,'公园',0,1,'shenzhenhaiyangshijie','1',3,40,'09:00','17:00','114.33525629176','22.610598450797','广东省深圳市盐田区盐葵路','深圳海洋世界又称小梅沙海洋世界位于广东省深圳市东部黄金海岸线上享有“东方夏威夷”美誉的小梅沙海滨旅游区。',0),(75,'SZX','锦绣中华',4,'公园',0,1,'jinxiuzhonghua','1',3,45,'09:00','17:00','113.99440324917','22.537012992517','广东省 深圳市 南山区深南大道','目前世界上面积最大、内容最丰富的实景微缩景区。82个景点均按中国版图位置分布，比例大部分按1：15建造。',0),(76,'SZX','海上田园',4,'其他',0,1,'haishangtianyuan','1',3,0,'09:00','17:00','113.77501198098','22.738408988201','深圳市宝安区沙井西环路','是深圳迄今为止最大、最典型的生态文化主题旅游景区，风景还不错。 ',0),(77,'SZX','中信明思克航母(已关闭)',4,'其他',0,1,'zhongxinmingsikehangmu','1',3,0,'09:00','17:00','114.247454','22.557908','广东省深圳市盐田区沙头角','中国乃至世界上第一座以航空母舰为主体的军事主题公园。',0),(78,'SZX','大芬油画村',4,'其他',0,1,'dafenyouhuacun','1',3,0,'09:00','17:00','114.14228661404','22.613063959633','深圳市龙岗区布吉镇布吉村','全球绘画者集中的油画生产基地。',0),(79,'SZX','中英街',4,'其他',0,1,'zhongyingjie','1',3,30,'09:00','17:00','114.2386079613','22.551641975282',' 广东省深圳市盐田区','英国殖民主义者强行租借的老街，“一街两制”为独特的人文景观。',0),(80,'SZX','东冲西冲',4,'海边',0,1,'dongchongxichong','1',3,0,'09:00','17:00','114.53863930661','22.484360104437',' 深圳市龙岗区南澳镇','深圳最美的海岸线，海水很干净漂亮啊，而且很美的沙滩，水比较清澈。',0),(81,'SZX','凤凰山',4,'山峰',0,1,'shenzhenfenghuangshan','1',3,0,'09:00','17:00','113.862371','22.678375',' 深圳市宝安区福永街道凤凰村','凤山福水福盈地，文天祥后代于此山脚世代居住，登高南瞰深圳湾。',0),(82,'SZX','光明农场',4,'其他',0,1,'guangmingnongchang','1',3,89,'09:00','17:00','113.94967706699','22.771079957609',' 深圳市宝安区光明街道','“绿色食品之国，农业旅游圣地”，可品尝乳鸽、牛奶、水果，观赏乳牛、落地皇鸽。',0),(83,'SZX','荷兰花卉小镇',4,'其他',0,1,'helanhuahuixiaozhen','1',3,0,'09:00','17:00','113.916723987','22.548850000667','','荷兰花卉小镇主打“花文化”，是一个集休闲、科普为一体的特色公园。',0),(84,'SZX','马峦山',4,'山峰',0,1,'maluanshan','1',3,0,'09:00','17:00','114.28385399287','22.663544007884',' 深圳市东部山区','这里有深圳最大的瀑布，在这里可以远足登山、观海观瀑。',0),(85,'SZX','青青世界',4,'海边',0,1,'qingqingshijie','1',3,51,'09:00','17:00','113.90693524169','22.511816284241',' 广东省 深圳市 南山区月亮湾青青街1号','景色是相当的美丽啦，空气还是很不错的。里面的环境也还比较不错。',0),(86,'SZX','中国民俗文化村',4,'公园',0,1,'zhongguominsuwenhuacun','1',3,0,'09:00','17:00','113.99168201229','22.536281026528',' 深圳市深南大道9005','中国第一个荟萃各民族民间艺术、民俗风情和民居建筑于一园的大型文化旅游景区。',0),(87,'SZX','野生动物园',4,'公园',0,1,'yeshengdongwuyuan','1',3,230,'09:00','17:00','113.97507997901','22.600932991618',' 深圳市南山区西丽湖东侧4065','我国第一家亚热带新型园林生态环境风景区，荣获“中华之最”光荣称号。',0),(88,'SZX','荔枝公园',4,'公园',0,1,'lizhigongyuan','1',3,0,'09:00','17:00','114.10905398137','22.552959880263','广东省 深圳市 南山区沙河西路5002号','',0),(89,'SZX','罗湖口岸',4,'其他',0,1,'luohukouan','1',3,10,'09:00','17:00','114.12477801084','22.534940010589','广东省深圳市福田区人民南路','罗湖口岸位于深圳罗湖商业中心南侧，与香港新界一河之隔，深港两地由一座双层人行桥和一座铁路桥相连。',0),(90,'SZX','梧桐山',4,'山峰',0,1,'wutongshan','1',3,0,'09:00','17:00','114.20208995589','22.595470041098',' 深圳市莲塘罗沙路2076号','梧桐山是深圳郊区登山游览的好地方，空气还是很清新的，梧桐山植被茂密。',0),(91,'SZX','华侨城创意文化园',4,'其他',0,1,'huaqiaochengchuangyiwenhuayuan','1',3,0,'09:00','17:00','113.99991866534','22.542629663949','深圳华侨城原东部工业区内','',0),(92,'SZX','欢乐海岸',4,'其他',0,1,'huanlehaian','1',3,50,'09:00','17:00','113.99870496476','22.532971983882',' 广东省深圳市南山区白石路东8号欢乐海岸水秀剧场','',0),(93,'SZX','深圳博物馆',4,'其他',0,1,'shenzhenbowuguan','1',3,0,'09:00','17:00','114.06836298754','22.54931696092',' 广东省深圳市福田区同心路6号','',0),(94,'SZX','洪湖公园',4,'公园',0,1,'honghugongyuan','1',3,0,'09:00','17:00','114.1263310014','22.575174016028',' ','',0),(95,'SZX','旧天堂书店',4,'购物娱乐',0,1,'tianshengqiaodaxiagu','1',3,0,'09:00','17:00','113.99929003114','22.546024756912','深圳市南山区华侨城创意园北区A5栋120#','',0),(96,'SZX','深圳湾公园',4,'公园',0,1,'shenzhenwangongyuan','1',3,0,'09:00','17:00','113.958905','22.505047','滨海大道(近望海路)','',0),(97,'SZX','三门岛',4,'海边',0,1,'sanmendao','1',3,0,'09:00','17:00','114.64674202046','22.466368994502',' 深圳东部海域大亚湾与大鹏湾交汇处','湛蓝的海水，纯白的沙滩，当你厌倦了那些满是游人的海滩，三门岛绝对会给你天堂般的感受。',0),(98,'SZX','深圳莲花山公园',4,'公园',0,1,'shenzhenlianhuashangongyuan','1',3,0,'09:00','17:00','114.06402498041','22.560122021388','深圳市福田区红荔路6030号(儿童医院对面)','雨后的莲花山公园很幽静迷人，很清新的空气。莲花山并不险峻雄伟。',0),(99,'SZX','羊台山森林公园',4,'其他',0,1,'yangtaishansenlingongyuan','1',3,0,'09:00','17:00','113.98660703531','22.668614041146',' 广东省深圳市宝安区','',0),(100,'SZX','华侨城',4,'其他',0,1,'huaqiaocheng','1',3,45,'09:00','17:00','113.99129699855','22.544819022498','广东省深圳市南山区华侨城','这里长年繁花似锦、绿树成荫，汇聚了中国最为集中的文化主题公园群、文化主题酒店群和文化艺术设施群，',0),(101,'SZX','观澜版画村',4,'其他',0,1,'guanlanbanhuacun','1',3,0,'09:00','17:00','114.09516599853','22.748539009661','宝安区观澜大水田','',0),(102,'SZX','南头古城',4,'城市',0,1,'nantougucheng','1',3,0,'09:00','17:00','113.92980798641','22.549238021719','南山区南山大道3109号中山公园南侧(近深南大道北侧)','',0),(103,'SZX','杨梅坑',4,'其他',0,1,'yangmeikeng','1',3,5,'09:00','17:00','114.5786988815','22.548724248231','广东深圳龙岗区','杨梅坑是由两条大坑汇合而成。一条是正尾坑，源头在大雁顶与三角山之间。',0),(104,'SZX','西丽果场',4,'乡村',0,1,'xiliguochang','1',3,0,'09:00','17:00','113.94505483599','22.606973504477',' 南山区沙河西路5002号(近西丽湖)','青山绿水赏心悦目，颗颗红荔甜上心头，品荔节定会让你感受到“日啖荔枝三百颗、不辞长作岭南人”的快乐和惬意！',0),(105,'SZX','坝光村',4,'其他',0,1,'baguangcun','1',3,0,'09:00','17:00','114.50521299928','22.651186910124',' 龙岗区葵涌镇坝光村(近塩坝高速终点站)','',0),(106,'SZX','仙人掌沙滩',4,'其他',0,1,'xianrenzhangshatan','1',3,0,'09:00','17:00','114.5432900139','22.480934008156','广东省深圳市龙岗区','',0),(107,'SZX','欢乐干线',4,'公园',0,1,'huanleganxian','1',3,45,'09:00','17:00','113.99376230819','22.539077681266','广东省 深圳市 南山区华侨区','',0),(108,'SZX','大鹏半岛',4,'其他',0,1,'dapengbandao','1',3,0,'09:00','17:00','114.56269826376','22.52364320813',' ','包括大鹏、葵涌、南澳，形似哑铃，奇峰多、沙滩软，被誉为深圳的“桃花源”。',0),(109,'SZX','茵特拉根小镇',4,'其他',0,1,'yintelagenxiaozhen','1',3,0,'09:00','17:00','114.28258801091','22.638748966056','深圳盐田区的东部华侨城','深圳茵特拉根小镇是在深圳盐田区的东部华侨城，中国人来这里看到的是欧洲瑞士阿尔卑斯山麓茵特拉根的建筑、赛马特的花卉、谢菲尔德的彩绘等多种题材和元素的风光；欧洲人来这里仿佛是回到了家乡。深圳人喜欢人造风景，而且都是大手笔。华侨城人把欧洲山地小镇搬迁到了深圳东部三洲田的山谷中。',0),(110,'SZX','何香凝美术馆',4,'展馆',0,1,'hexiangningmeishuguan','1',3,0,'09:00','17:00','113.9881118767','22.539233318192','','',0),(111,'CAN','莲花山',4,'山峰',0,1,'fanyulianhuashan','1',3,87,'09:00','17:00','113.51030703881','22.99008897742','广州市番禺区市桥禺山大道西横江村侧','莲花山上或悬崖峭壁，或奇岩异洞，风景很不错，感觉还挺好玩。',0),(112,'SZX','深圳东湖公园',4,'公园',0,1,'shenzhendonghugongyuan','1',3,0,'09:00','17:00','114.1554410406','22.571129966391','罗湖区爱国路4006号东湖公园内（近人民检察院侧门）','水库公园，感受流动与轻灵气息。',0),(113,'SZX','弘法寺',4,'宗教场所',0,1,'hongfasi','1',3,0,'09:00','17:00','114.18798404413','22.583631992336','莲塘仙湖路160号仙湖植物园内','',0),(114,'SZX','大梅沙海滨栈道',4,'自然保护区',0,1,'dameishahaibinzhandao','1',3,0,'09:00','17:00','114.31229200971','22.587575981801','深圳市西起盐田东港区，东至大梅沙海滨公园','',0),(115,'SZX','福田口岸',4,'其他',0,1,'futiankouan','1',3,10,'09:00','17:00','114.07650298292','22.521286001109','广东省深圳市福田区福田口岸','',0),(116,'SZX','桔钓沙',5,'其他',0,1,'judiaosha','1',3,0,'09:00','17:00','114.56528628193','22.564638895676','广东省深圳市龙岗区南澳镇东部沿海','',0),(117,'SZX','七娘山',4,'其他',0,1,'qiniangshan','1',3,0,'09:00','17:00','114.55701800853','22.531973021298','广东省深圳市龙岗区','',0),(118,'SZX','邓小平画像',4,'其他',0,1,'dengxiaopinghuaxiang','1',3,0,'09:00','17:00','114.11061200244','22.547511027505','','',0),(119,'SZX','浪漫欧洲',5,'其他',0,1,'langmanouzhou','1',3,0,'09:00','17:00','113.98157544649','22.541746767386',' 广东省深圳市南山区华侨城世界之窗景区内','',0),(120,'SZX','观澜山水田园漂流',4,'其他',0,1,'guanlanshanshuitianyuanpiaoliu','1',3,0,'09:00','17:00','114.11217110149','22.723649100191','广东省深圳市宝安区观澜镇君子布村环观南路观澜山水田园农庄','',0),(121,'CAN','沙面',4,'其他',0,1,'shamian','1',3,0,'09:00','17:00','113.2513129841','23.112766974137','广州市珠江岔口白鹅潭畔','树木茂密，很幽静很有特色。而且保存的很好，建筑很有风格。 ',0),(122,'CAN','石室圣心大教堂',4,'宗教场所',0,1,'shishishengxindajiaotang','1',3,0,'09:00','17:00','113.26657187097','23.120379142841',' 广东省广州市一德路','环境很幽静，圣洁而美丽。虔诚的信徒，一走进去就很庄严神圣的感觉。',0),(123,'CAN','白云山',4,'山峰',0,1,'guangzhoubaiyunshan','1',3,0,'09:00','17:00','113.3062619728','23.191478030266',' 广园中路白云山南路','羊城第一秀，如今已成为城市的天然氧吧。',0),(124,'CAN','长隆欢乐世界',5,'公园',0,1,'changlonghuanleshijie','1',3,200,'09:00','17:00','113.33789903522','23.005241010951','广东省广州市番禺区迎宾路','机动设施非常充足，收费稍贵，但是很有特色，马戏很好看。',0),(125,'CAN','上下九步行街',4,'其他',0,1,'shangxiajiubuxingjie','1',3,0,'09:00','17:00','113.252412','23.119545','广州市荔湾区上九路、下九路、第十甫路','很好的逛街的地方，无论是公交还是地铁都很方便。老字号味道还不错。',0),(126,'CAN','越秀公园',4,'公园',0,1,'yuexiugongyuan','1',3,2,'09:00','17:00','113.26803862421','23.148351599537','广东省 广州市 越秀区解放北路960号','越秀山以西汉时南越王赵佗曾在山上建“朝汉台”而得名，著名古迹镇海楼。',0),(127,'CAN','岭南印象园',4,'其他',0,1,'lingnanyinxiangyuan','1',3,54,'09:00','17:00','113.41187871598','23.04145445358',' 广州大学城外环西路岭南印象园','空气很新鲜，总体不错，而且环境很好。享受各种美食，很适合拍照。',0),(128,'CAN','中山大学',4,'学校',0,1,'zhongshandaxue','1',3,0,'09:00','17:00','113.30522398078','23.099409038728','广州市新港西路135号','在广州难得一见的幽静地方，里面的风景也不错。建筑很有代表性格调很好。',0),(129,'CAN','广州动物园',4,'公园',0,1,'guangzhoudongwuyuan','1',3,10,'09:00','17:00','113.31177702965','23.147619049197','广州市先烈中路120号','总体满意，只是里面的小卖部卖的东西好贵。地方很好找，但就是假期人太多。',0),(130,'CAN','黄埔军校旧址',4,'历史遗址',0,1,'huangpujunxiaojiuzhi','1',3,2,'09:00','17:00','113.43092396058','23.092060039015','黄埔区长洲岛','史迹丰富，山水相隔，已初步重现划昔日风姿，爱国主义教育的好地方。',0),(131,'CAN','广州塔',4,'现代建筑',0,1,'guangzhouta','1',3,114,'09:00','17:00','113.33112140998','23.11220432656','广州塔位于广州市中心','总高度600米，广州的新地标，一座以观光旅游为主，具有广播电视发射、文化娱乐和城市窗口功能的大型城市基础设施。',0),(132,'CAN','红专厂',4,'其他',0,1,'hongzhuanchang','1',3,0,'09:00','17:00','113.377298','23.1165','天河区员村四横路128号D1栋(近美林海岸)','是城市中心难得的集时代气息、艺术、创意、文化于一体的生活体验区。',0),(133,'CAN','大夫山',4,'自然保护区',0,1,'dafushan','1',3,58,'09:00','17:00','113.31532901946','22.955812017819',' 番禺区禺山西路688号','公园里踩单车挺好玩的，适合骑单车和锻炼的地方，而且公园环境很好。',0),(134,'CAN','陈家祠',4,'宗教场所',0,1,'chenjiaci','1',3,0,'09:00','17:00','113.253043','23.132023',' 中山七路恩龙里34号','中国清代宗祠建筑，典型的南方古建筑，工艺非常精湛，文化气息浓厚。 ',0),(135,'CAN','白水寨',4,'瀑布',0,1,'baishuizhai','1',3,54,'09:00','17:00','113.77039280422','23.586497191427','增城派潭镇白水寨风景名胜区','被誉为“北回归线上的瑰丽翡翠”。有全国唯一用海船木建造的亲水栈道以及广东最长的登山步道“天南第一梯”。',0),(136,'CAN','中山纪念堂',4,'公园',0,1,'zhongshanjiniantang','1',3,9,'09:00','17:00','113.27121898504','23.139696978834',' 广州市东风中路259号','感觉特别宏伟，建筑上的彩绘很漂亮。环境挺优美安静。',0),(137,'CAN','小洲村',4,'乡村',0,1,'xiaozhoucun','1',3,0,'09:00','17:00','113.36539176518','23.066293693303',' ','环境很美，很多有特色的东西，沿着错综的巷陌，安静地看看安然的样子。',0),(138,'CAN','南沙天后宫',4,'宗教场所',0,1,'nanshatianhougong','1',3,0,'09:00','17:00','113.62128801335','22.761852011126',' 南沙区大角山东南麓','依山傍水，其规模是现今世界同类建筑之最，被誉为“天下天后第一宫”。',0),(139,'CAN','南越王墓博物馆',4,'展馆',0,1,'xihannanyuewangbowuguan','1',3,0,'09:00','17:00','113.26793298348','23.143985014602','广东省 广州市 越秀区解放北路867号','南越王墓博物馆是岭南地区年代最早的一座大型彩绘石室墓，是近年来我国五大考古发现之一。',0),(140,'CAN','广州大学城',4,'学校',0,1,'guangzhoudaxuecheng','1',3,0,'09:00','17:00','113.40084968026','23.064409223371',' 广东省广州市番禺区','环境优美，非常宽敞。很适合骑车游玩，娱乐休闲场所、美食聚集地也很多。',0),(141,'CAN','宝墨园',4,'乡村',0,1,'baomoyuan','1',3,48,'09:00','17:00','113.30078401597','22.901170980612','番禺区沙湾镇紫坭村','空气很好，人很多，但是比南粤苑大很多。岭南水乡特色，精致的园林。',0),(142,'CAN','华南植物园',4,'公园',0,1,'huananzhiwuyuan','1',3,13,'09:00','17:00','113.37291902637','23.188636043593',' 广东省 广州市 天河区兴科路723号','植物园风景优美，空气清新，清凉而惬意，温室景区非常漂亮，票价较贵。',0),(143,'CAN','珠江',4,'河流',0,1,'zhujiang','1',3,0,'09:00','17:00','113.39166270981','23.1140914426','','广府文化随珠江而流传，珠江可谓是广州的母亲河。珠江晚上夜景很美。',0),(144,'CAN','长隆水上乐园',5,'其他',0,1,'changlongshuishangleyuan','1',3,180,'09:00','17:00','113.33068501317','23.008280026357',' 广州番禺番禺大道','目前世界上至大、至先进、水上游乐项目至多的大型水上乐园。',0),(145,'CAN','长隆野生动物世界',5,'公园',0,1,'changlongyeshengdongwushijie','1',3,8,'09:00','17:00','113.32073699829','23.007398009642','广东省广州市番禺区大石街','中国最具国际水准的国家级野生动物园。其中动物表演驰名国内外。',0),(146,'CAN','百万葵园',4,'公园',0,1,'baiwankuiyuan','1',3,120,'09:00','17:00','113.625673','22.635207',' 广州市番禺区南沙区新垦镇','百万葵园距离市区较远，环境很美，只是整个园区很小。 ',0),(147,'CAN','西关古老大屋',4,'历史建筑',0,1,'xiguangulaodawu','1',3,0,'09:00','17:00','113.24257498674','23.125435012133',' 广州市十三甫正街15号','西关是广州的旧称，这一带拥有典型的传统旧屋，这些老屋过去多是豪门富商的住宅，高大明亮、装饰精美。',0),(148,'CAN','海心沙',4,'其他',0,1,'haixinsha','1',3,0,'09:00','17:00','113.330888','23.117287',' 天河区珠江新城临江大道(近广州歌剧院)','上游不远为二沙岛与广州大桥，下游不远处是新建成的猎德大桥，适合拍照。',0),(149,'CAN','云台花园',4,'公园',0,1,'yuntaihuayuan','1',3,6,'09:00','17:00','113.29613690191','23.163526642469','广州白云山入口处','背依白云山的云台岭、园中遍植中外四季名贵花卉，是我国最大的园林式花园。',0),(150,'CAN','光孝寺',4,'历史建筑',0,1,'guangxiaosi','1',3,10,'09:00','17:00','113.2626786149','23.134614485182',' 广东省广州市越秀区光孝路109号','羊城年代最古、规模最大的佛教名刹，来这气势十分雄伟寺院，结构威严壮丽殿宇膜拜。',0),(151,'CAN','石门国家森林公园',4,'自然保护区',0,1,'shimenguojiasenlingongyuan','1',3,0,'09:00','17:00','113.77375246682','23.634104205611',' ','空气非常好，三四月份天池花海的油菜花地很漂亮，而且石门公园也非常棒。 ',0),(152,'CAN','香草世界',4,'公园',0,1,'xiangcaoshijie','1',3,65,'09:00','17:00','113.2673417188','23.46771196769','广州市花都区花山镇','华南地区最大的香草主题公园，收集和引种了世界各地著名香草，种植不同品种花卉。',0),(153,'CAN','火炉山',4,'公园',0,1,'huolushan','1',3,0,'09:00','17:00','113.40549904008','23.190637009219','广州市天河区','因山上泥土为红色而称为火炉山，是休闲游玩的理想之地。',0),(154,'CAN','广州荔枝湾涌',4,'城市',0,1,'guangzhoulizhiwanyong','1',3,0,'09:00','17:00','113.24463695721','23.12759903613',' 广东省广州市荔湾区荔枝湾路(近泮溪酒家)','',0),(155,'CAN','广州海洋馆',4,'其他',0,1,'guangzhouhaiyangguan','1',3,10,'09:00','17:00','113.30996631523','23.14992077885','广东省广州市越秀区先烈中路120号动物园内','广州海洋馆里风景很好，算是不错的地方，但就是人太多，表演很精彩。',0),(156,'CAN','流溪河国家森林公园',4,'自然保护区',0,1,'liuxiheguojiasenlingongyuan','1',3,32,'09:00','17:00','113.78992600837','23.756633006342','广东省广州市从化市良口镇流溪河林场','有着亚热带森林特色，被誉为镶嵌在北回归线的一颗绿色明珠。 ',0),(157,'CAN','珠江夜游',4,'河流',0,1,'zhujiangyeyou','1',3,163,'09:00','17:00','113.27748403715','23.121088993598','珠江广州河段','视野很开阔，在彩灯照耀下珠江的夜色非常迷人，珠江夜景很美。',0),(158,'CAN','碧水湾温泉',5,'温泉',0,1,'bishuiwanwenquan','1',3,170,'09:00','17:00','113.7227826122','23.705504653671','广州从化流溪温泉度假区（良口）。','碧水湾温泉度假村位于广州从化流溪河畔，四周层峦叠嶂，树木葱郁。',0),(159,'CAN','南沙湿地公园',4,'公园',0,1,'nanshashidigongyuan','1',3,38,'09:00','17:00','113.64571204185','22.61614603303','广州市南沙区万顷沙镇新港大道1号','誉为“广州之肾”，风景优美，碧波荡漾、绿树成荫、荷花飘香、万鸟齐飞。',0),(160,'CAN','天河公园',4,'其他',0,1,'tianhegongyuan','1',3,2,'09:00','17:00','113.37281401445','23.134261050425',' ','',0),(161,'CAN','广州博物馆',4,'展馆',0,1,'guangzhoubowuguan','1',3,30,'09:00','17:00','113.27201353626','23.144144821307',' 广州市越秀山镇海楼','我国最早期创建的博物馆之一，址镇海楼距今已有六百多年的历史，是广州著名的古建筑，被誉为“五岭以南第一楼”。',0),(162,'CAN','广东省博物馆',4,'展馆',0,1,'guangdongshengbowuguan','1',3,0,'09:00','17:00','113.33324096183','23.120475030664','广东省广州市天河区珠江东路2号','',0),(163,'CAN','余荫山房',4,'其他',0,1,'yuyinshanfang','1',3,15,'09:00','17:00','113.40184500276','23.01802639674','广州市番禺区南村镇北大街余荫山房','建筑了深柳堂、玲珑水榭、来薰亭、孔雀亭和廊桥等，布局巧妙，建筑紧凑。',0),(164,'CAN','广州塔广场',4,'其他',0,1,'guangzhoutaguangchang','1',3,0,'09:00','17:00','113.33099510823','23.112233244261','广州市海珠区阅江西路222号广州塔下','',0),(165,'CAN','黄埔古港景观区',4,'其他',0,1,'huangpugugangjingguanqu','1',3,0,'09:00','17:00','113.39650502566','23.093412970965',' 石基路528附近','',0),(166,'CAN','麓湖公园',4,'其他',0,1,'luhugongyuan','1',3,0,'09:00','17:00','113.29004100072','23.155141993876','广州市越秀区广园中路麓景路11号','山水园林公园，马尾松、台湾相思桉、竹混交成林，内有为纪念冼星海而建的星海园。',0),(167,'CAN','TIT创意园',4,'其他',0,1,'TITchuangyiyuan','1',3,0,'09:00','17:00','113.33035704183','23.103348981615',' 新港中路','',0),(168,'CAN','黄花岗公园',4,'公园',0,1,'huanghuaganggongyuan','1',3,10,'09:00','17:00','113.30157802821','23.146327979592','广东省广州市越秀区先烈中路79号','黄花浩气之地，是为纪念孙中山先生领导的同盟会在广州“三·二九”起义战役中牺牲的烈士而建的。',0),(169,'CAN','北京路千年古道遗址',4,'其他',0,1,'beijingluqianniangudaoyizhi','1',3,0,'09:00','17:00','113.27528013442','23.131263712999','北京路(近中山五路)','',0),(170,'CAN','溪头村',4,'乡村',0,1,'xitoucun','1',3,0,'09:00','17:00','113.87420997197','23.719721116486','广州市从化市','',0),(171,'CAN','南海神庙',4,'其他',0,1,'nanhaishenmiao','1',3,8,'09:00','17:00','113.50288802344','23.086248981872','广州市黄埔区南岗镇庙头村旭日街22号','中国古代人民祭海的场所，是我国古代海神庙中惟一遗存下来的最完整的建筑群。',0),(172,'CAN','南湖游乐园',4,'公园',0,1,'nanhuyouleyuan','1',3,55,'09:00','17:00','113.34101597568','23.226553980976','广州市白云区广州大道北983号南湖游乐园','乐园依山傍水，环境优雅，风景秀丽，空气清新怡人，轻松有趣的儿童天地。',0),(173,'CAN','天河体育中心',4,'现代建筑',0,1,'tianhetiyuzhongxin','1',3,0,'09:00','17:00','113.33467798116','23.140770014345',' 广东省广州市天河区','天河体育中心位于市区东部，毗邻火车东站，是广州目前最大的体育场地。',0),(174,'CAN','广州起义烈士陵园',4,'其他',0,1,'guangzhouqiyilieshilingyuan','1',3,0,'09:00','17:00','113.29253002563','23.135581981008','广州市越秀区中山二路92号','',0),(175,'CAN','十香园',4,'公园',0,1,'shixiangyuan','1',3,0,'09:00','17:00','113.28032196392','23.098643973198',' 广州市怀德大街3号','清末著名画家居廉、居巢兄弟的居住、作画及授徒之所',0),(176,'CAN','流花湖公园',4,'其他',0,1,'liuhuahugongyuan','1',3,8,'09:00','17:00','113.25629597459','23.142164005418','广州市越秀区流花路100号','',0),(177,'CAN','帽峰山',4,'山峰',0,1,'maofengshan','1',3,0,'09:00','17:00','113.46925744143','23.308276396209','广州市白云区太和镇与良田镇交界处 ‎','',0),(178,'CAN','沙湾古镇',4,'古镇',0,1,'shawanguzhen','1',3,25,'09:00','17:00','113.33777803347','22.90973199713','广东省广州市番禺区沙湾大巷涌路10号','显得挺安静的。推荐炸牛奶，可以品尝特色的姜撞奶，很有地道的岭南风味。 ',0),(179,'CAN','花城广场',4,'其他',0,1,'huachengguangchang','1',3,0,'09:00','17:00','113.33120899477','23.131590984066','','',0);
/*!40000 ALTER TABLE `IMScenic` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `IMScenicTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMScenicTag` (
  `id` int(11) unsigned NOT NULL DEFAULT '1' COMMENT 'id',
  `literature` int(11) unsigned NOT NULL COMMENT '',
  `comfort` int(11) unsigned NOT NULL COMMENT '',
  `exploration` int(11) unsigned NOT NULL COMMENT '',
  `excite` int(11) unsigned NOT NULL COMMENT '',
  `encounter` int(11) unsigned NOT NULL COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMScenicTag`
--

LOCK TABLES `IMScenicTag` WRITE;
/*!40000 ALTER TABLE `IMScenicTag` DISABLE KEYS */;
INSERT INTO `IMScenicTag` VALUES 
(1,5,3,0,0,3),
(2,5,4,0,0,3),
(3,5,4,0,0,0),
(4,0,5,0,0,1),
(5,5,3,4,0,0),
(6,4,5,0,0,3),
(7,4,5,3,0,1),
(8,5,4,0,0,0),
(9,5,4,0,0,3),
(10,0,5,0,0,0),
(11,5,4,0,0,3),
(12,5,5,0,0,3),
(13,0,5,0,0,4),
(14,2,5,0,0,0),
(15,0,3,5,4,0),
(16,5,4,0,0,0),
(18,0,5,0,3,2),
(19,5,4,0,0,3),
(20,4,5,0,0,0),
(21,0,5,0,3,0),
(22,3,5,0,0,4),
(24,5,4,1,0,0),
(26,3,5,4,0,0),
(27,4,3,0,2,5),
(28,5,4,3,0,0),
(30,5,4,1,0,0),
(33,0,5,2,0,0),
(34,5,4,3,0,2),
(35,0,3,4,5,0),
(36,5,4,0,0,0),
(38,5,4,3,0,2),
(39,3,5,0,0,4),
(41,3,5,0,0,0),
(42,5,4,0,0,0),
(45,0,5,4,3,0),
(49,5,4,3,0,0),
(50,0,3,4,0,0),
(51,5,4,1,2,3),
(52,5,4,3,0,2),
(53,4,5,0,0,0),
(55,0,5,2,0,0),
(57,0,3,5,2,0),
(60,4,5,0,0,0),
(61,5,3,4,0,0),
(62,0,2,4,5,1),
(63,0,5,0,0,2),
(64,5,4,2,3,1),
(65,0,2,5,4,1),
(66,3,5,1,0,2),
(67,3,5,1,0,2),
(68,1,4,0,0,0),
(69,0,5,0,0,2),
(70,5,4,2,0,1),
(71,0,4,2,5,0),
(72,5,4,0,0,3),
(73,5,4,0,0,0),
(74,3,5,2,0,0),
(75,5,4,3,0,2),
(76,5,4,0,0,3),
(78,5,4,3,0,2),
(79,5,4,3,0,0),
(80,3,2,5,4,1),
(81,0,1,5,4,0),
(82,5,4,3,2,1),
(83,3,2,0,0,1),
(85,3,5,2,0,1),
(86,5,4,0,0,2),
(88,1,4,0,0,0),
(91,5,4,3,0,2),
(92,2,4,1,0,5),
(93,5,4,3,0,0),
(94,0,5,0,0,0),
(95,5,1,0,0,3),
(96,1,5,2,0,3),
(98,2,3,0,0,0),
(99,0,3,2,0,0),
(100,5,4,1,0,3),
(101,5,4,0,0,3),
(102,5,4,3,0,0),
(103,2,3,0,0,4),
(104,0,5,0,3,0),
(105,5,4,2,0,3),
(108,0,5,3,0,2),
(110,5,4,0,0,1),
(113,5,4,0,0,0),
(114,5,4,0,0,3),
(121,5,4,1,0,2),
(122,5,4,0,0,0),
(123,4,3,2,0,0),
(124,0,5,3,0,2),
(125,5,4,0,0,3),
(126,5,4,1,0,0),
(127,5,4,3,0,1),
(128,5,4,0,0,1),
(130,5,4,0,0,0),
(131,0,5,0,4,0),
(132,5,4,3,0,2),
(133,3,5,1,0,2),
(134,5,4,0,0,0),
(135,3,4,1,2,0),
(136,5,4,0,0,0),
(137,5,4,0,0,2),
(138,5,4,0,0,0),
(139,5,4,0,0,0),
(140,5,4,0,0,3),
(141,5,4,0,0,0),
(142,0,5,0,0,0),
(146,5,4,1,0,3),
(147,5,4,3,0,0),
(149,2,3,0,0,0),
(150,4,3,0,0,0),
(151,2,3,0,0,1),
(152,2,3,0,0,2),
(153,0,3,0,0,0),
(154,3,2,0,0,0),
(155,3,4,0,0,0),
(156,0,4,0,0,0),
(157,3,4,0,0,2),
(158,0,4,0,0,0),
(159,0,4,0,0,0),
(160,3,4,0,0,0),
(161,4,3,0,0,0),
(162,4,3,0,0,0),
(163,3,2,0,0,0),
(164,3,2,0,0,0),
(165,4,2,0,0,0),
(166,4,3,0,0,0),
(167,3,0,0,0,0),
(168,4,3,0,0,0),
(169,3,2,0,0,2),
(170,5,3,0,0,0),
(171,5,3,0,0,0),
(172,4,5,0,0,0),
(173,4,3,0,0,0),
(174,5,3,0,0,0),
(175,5,4,0,0,0),
(176,2,5,0,0,0),
(177,3,5,0,0,0),
(178,5,4,0,0,0),
(179,4,3,0,0,0);
/*!40000 ALTER TABLE `IMScenicTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMTickets`
--

DROP TABLE IF EXISTS `IMTickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMTickets` (
  `id` mediumint(6) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '票类型 1-机票，2-火车，3-汽车',
  `no` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `place_from_code` varchar(5) COLLATE utf8mb4_bin NOT NULL COMMENT '出发地城市编码',
  `place_from` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '出发地城市',
  `place_to_code` varchar(5) COLLATE utf8mb4_bin NOT NULL COMMENT '目的地城市编码',
  `place_to` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '目的地城市',
  `time_start` varchar(6) COLLATE utf8mb4_bin NOT NULL COMMENT '起始时间´',
  `time_end` varchar(6) COLLATE utf8mb4_bin NOT NULL COMMENT '结束时间´',
  `class` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '等级',
  `price` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '价格',
  `comment` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '编号',
  PRIMARY KEY (`id`),
  KEY `idx_type_placefrom_placeto_timestart_timeend_class` (`type`,`no`,`place_from`,`place_to`,`time_start`,`time_end`,`class`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMTickets`
--

LOCK TABLES `IMTickets` WRITE;
/*!40000 ALTER TABLE `IMTickets` DISABLE KEYS */;
INSERT INTO `IMTickets` VALUES (1,2,'D936','SZX','深圳北','XMN','厦门北','20:15','23:26',0,15050,NULL),(2,2,'D942','SZX','深圳北','XMN','厦门北','20:20','23:31',0,15050,NULL),(3,2,'D3126','SZX','深圳北','XMN','厦门北','7:00','10:42',0,15050,NULL),(4,2,'D2350','SZX','深圳北','XMN','厦门北','7:35','11:13',0,15050,NULL),(5,2,'D674','SZX','深圳北','XMN','厦门北','7:45','11:33',0,15450,NULL),(6,2,'D2342','SZX','深圳北','XMN','厦门北','7:50','11:26',0,15050,NULL),(7,2,'D2294','SZX','深圳北','XMN','厦门北','7:55','11:41',0,15050,NULL),(8,2,'D2302','SZX','深圳北','XMN','厦门北','8:06','11:47',0,15450,NULL),(9,2,'D3108','SZX','深圳北','XMN','厦门北','8:11','11:54',0,15050,NULL),(10,2,'D2344','SZX','深圳北','XMN','厦门北','8:16','12:14',0,15050,NULL),(11,2,'D2308','SZX','深圳北','XMN','厦门北','8:32','12:25',0,15050,NULL),(12,2,'D2346','SZX','深圳北','XMN','厦门北','8:42','12:34',0,15050,NULL),(13,2,'D2284','SZX','深圳北','XMN','厦门北','8:47','12:39',0,15050,NULL),(14,2,'G1602','SZX','深圳北','XMN','厦门北','8:52','12:50',0,15050,NULL),(15,2,'D2282','SZX','深圳北','XMN','厦门北','9:13','13:07',0,15050,NULL),(16,2,'D682','SZX','深圳北','XMN','厦门北','9:24','13:15',0,15450,NULL),(17,2,'D2310','SZX','深圳北','XMN','厦门北','9:45','13:34',0,15050,NULL),(18,2,'D2286','SZX','深圳北','XMN','厦门北','9:55','13:47',0,15050,NULL),(19,2,'D2334','SZX','深圳北','XMN','厦门北','10:11','14:09',0,15450,NULL),(20,2,'D2316','SZX','深圳北','XMN','厦门北','10:25','14:16',0,15450,NULL),(21,2,'D2288','SZX','深圳北','XMN','厦门北','10:40','14:31',0,15050,NULL),(22,2,'D3112','SZX','深圳北','XMN','厦门北','11:17','15:11',0,15050,NULL),(23,2,'D2318','SZX','深圳北','XMN','厦门北','11:56','15:39',0,15050,NULL),(24,2,'D684','SZX','深圳北','XMN','厦门北','12:15','16:07',0,15450,NULL),(25,2,'D2338','SZX','深圳北','XMN','厦门北','12:20','16:20',0,15450,NULL),(26,2,'D2322','SZX','深圳北','XMN','厦门北','13:00','16:53',0,15050,NULL),(27,2,'D2324','SZX','深圳北','XMN','厦门北','13:54','17:54',0,15050,NULL),(28,2,'D2304','SZX','深圳北','XMN','厦门北','14:55','18:31',0,15050,NULL),(29,2,'D2298','SZX','深圳北','XMN','厦门北','15:06','18:58',0,15050,NULL),(30,2,'D2328','SZX','深圳北','XMN','厦门北','15:25','19:18',0,15050,NULL),(31,2,'D2296','SZX','深圳北','XMN','厦门北','15:38','19:23',0,15050,NULL),(32,2,'D2330','SZX','深圳北','XMN','厦门北','15:46','19:31',0,15050,NULL),(33,2,'D2306','SZX','深圳北','XMN','厦门北','16:07','19:56',0,15050,NULL),(34,2,'D672','SZX','深圳北','XMN','厦门北','16:14','20:03',0,15050,NULL),(35,2,'D2326','SZX','深圳北','XMN','厦门北','16:19','20:17',0,15050,NULL),(36,2,'D2312','SZX','深圳北','XMN','厦门北','16:43','20:37',0,15050,NULL),(37,2,'D2314','SZX','深圳北','XMN','厦门北','17:10','20:52',0,15050,NULL),(38,2,'D2315','XMN','厦门北','SZX','深圳北','7:42','11:23',0,15050,NULL),(39,2,'D2335','XMN','厦门北','SZX','深圳北','7:59','11:57',0,15450,NULL),(40,2,'D681','XMN','厦门北','SZX','深圳北','8:04','11:49',0,15050,NULL),(41,2,'D2313','XMN','厦门北','SZX','深圳北','8:50','12:38',0,15050,NULL),(42,2,'D2327','XMN','厦门北','SZX','深圳北','9:08','12:59',0,15050,NULL),(43,2,'D2311','XMN','厦门北','SZX','深圳北','9:32','13:25',0,15050,NULL),(44,2,'D2295','XMN','厦门北','SZX','深圳北','9:47','13:36',0,15050,NULL),(45,2,'D2297','XMN','厦门北','SZX','深圳北','10:44','14:26',0,15050,NULL),(46,2,'D2301','XMN','厦门北','SZX','深圳北','10:55','14:32',0,15050,NULL),(47,2,'D2323','XMN','厦门北','SZX','深圳北','11:15','14:55',0,15050,NULL),(48,2,'D3337','XMN','厦门北','SZX','深圳北','11:26','15:24',0,15050,NULL),(49,2,'D2329','XMN','厦门北','SZX','深圳北','11:37','15:29',0,15050,NULL),(50,2,'D2341','XMN','厦门北','SZX','深圳北','11:47','15:39',0,15050,NULL),(51,2,'D2303','XMN','厦门北','SZX','深圳北','11:54','15:34',0,15050,NULL),(52,2,'D673','XMN','厦门北','SZX','深圳北','11:57','15:50',0,15450,NULL),(53,2,'D2325','XMN','厦门北','SZX','深圳北','12:17','15:59',0,15050,NULL),(54,2,'D2349','XMN','厦门北','SZX','深圳北','12:28','16:15',0,15050,NULL),(55,2,'D2321','XMN','厦门北','SZX','深圳北','13:00','16:49',0,15050,NULL),(56,2,'D2345','XMN','厦门北','SZX','深圳北','13:27','17:08',0,15050,NULL),(57,2,'D2343','XMN','厦门北','SZX','深圳北','13:40','17:20',0,15050,NULL),(58,2,'D683','XMN','厦门北','SZX','深圳北','13:41','17:40',0,15450,NULL),(59,2,'D2331','XMN','厦门北','SZX','深圳北','14:19','17:57',0,15050,NULL),(60,2,'D3111','XMN','厦门北','SZX','深圳北','14:28','18:12',0,15050,NULL),(61,2,'D2317','XMN','厦门北',
'SZX','深圳北','14:37','18:36',0,15450,NULL),(62,2,'D2333','XMN','厦门北','SZX','深圳北','14:53','18:53',0,15450,NULL),(63,2,'D2287','XMN','厦门北','SZX','深圳北','15:43','19:19',0,15050,NULL),(64,2,'G1601','XMN','厦门北','SZX','深圳北','15:57','19:37',0,15050,NULL),(65,2,'D2319','XMN','厦门北','SZX','深圳北','16:03','19:59',0,15050,NULL),(66,2,'D2305','XMN','厦门北','SZX','深圳北','16:20','20:19',0,15450,NULL),(67,2,'D2307','XMN','厦门北','SZX','深圳北','16:24','20:12',0,15050,NULL),(68,2,'D2337','XMN','厦门北','SZX','深圳北','16:42','20:33',0,15450,NULL),(69,2,'D3125','XMN','厦门北','SZX','深圳北','16:57','20:39',0,15050,NULL),(70,2,'D2285','XMN','厦门北','SZX','深圳北','17:03','20:45',0,15050,NULL),(71,2,'D685','XMN','厦门北','SZX','深圳北','17:12','21:10',0,15450,NULL),(72,2,'D2309','XMN','厦门北','SZX','深圳北','17:17','21:16',0,15050,NULL),(73,2,'D3107','XMN','厦门北','SZX','深圳北','17:56','21:24',0,15050,NULL),(74,2,'D2281','XMN','厦门北','SZX','深圳北','18:15','22:09',0,15050,NULL),(75,2,'D2353','XMN','厦门北','SZX','深圳北','18:45','22:39',0,15050,NULL),(76,2,'D2283','XMN','厦门北','SZX','深圳北','19:08','22:51',0,15050,NULL),(77,2,'D2293','XMN','厦门北','SZX','深圳北','19:21','23:08',0,15050,NULL),(78,2,'D671','XMN','厦门北','SZX','深圳北','20:00','23:38',0,15050,NULL),(79,2,'海南航空HU7065','SZX','宝安国际机场T3','XMN','高崎国际机场T4','07:45','08:55',0,38500,NULL),(80,2,'厦门航空MF8069','SZX','宝安国际机场T3','XMN','高崎国际机场T4','09:40','10:55',0,128400,NULL),(81,2,'河北航空NS8069','SZX','宝安国际机场T3','XMN','高崎国际机场T4','09:40','10:55',0,129500,NULL),(82,2,'厦门航空MF846','SZX','宝安国际机场T3','XMN','高崎国际机场T4','20:15','21:25',0,129200,NULL);
/*!40000 ALTER TABLE `IMTickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMTravelBasicInfo`
--

DROP TABLE IF EXISTS `IMTravelBasicInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMTravelBasicInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) unsigned NOT NULL COMMENT '用户id',
  `cost` int(11) unsigned NOT NULL,
  `personNum` int(11) unsigned NOT NULL COMMENT '游玩人数',
  `placeFromCode` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '出发地城市',
  `placeBackCode` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '回程城市',
  `placeToCode` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '目的地城市',
  `dateFrom` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '起始日期',
  `dateTo` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '结束日期',
  `toolType` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '交通类型 pb:TransportToolType',
  `timeFrom` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '起始时间',
  `timeTo` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '结束时间',
  `qualityType` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'pb:QualityType',
  `transit` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '中转次数',
  `transToolToId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '去程交通工具',
  `transToolBackId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '去程交通工具',
  `playQualityType` int(11) unsigned NOT NULL COMMENT 'pb:QualityType',
  `playTimeFrom` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '起始时间',
  `playTimeTo` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '结束时间',
  `playToolType` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '交通类型 pb:TransportToolType',
  `positionType` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'pb:PositionType',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0 is ok',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMTravelBasicInfo`
--

LOCK TABLES `IMTravelBasicInfo` WRITE;
/*!40000 ALTER TABLE `IMTravelBasicInfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMTravelBasicInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMTravelTool`
--

DROP TABLE IF EXISTS `IMTravelTool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMTravelTool` (
  `id` int(11) unsigned NOT NULL COMMENT 'id',
  `type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '票类型 1-机票，2-火车，3-汽车',
  `no` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `placeFromCode` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '出发地编码',
  `placeFrom` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '出发地名称',
  `placeToCode` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '目的地编码',
  `placeTo` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '目的地名称',
  `timeFrom` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '起始时间´',
  `timeTo` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '结束时间´',
  `class` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '等级',
  `price` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '价格',
  `comment` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '说明',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0 is ok',
  PRIMARY KEY (`id`),
  KEY `idx_type_placeFrom_placeTo_timeFrom_timeTo_class` (`type`,`no`,`placeFrom`,`placeTo`,`timeFrom`,`timeTo`,`class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMTravelTool`
--

LOCK TABLES `IMTravelTool` WRITE;
/*!40000 ALTER TABLE `IMTravelTool` DISABLE KEYS */;
INSERT INTO `IMTravelTool` VALUES (1,1,'D936','SZX','深圳北','XMN','厦门北','20:15','23:26','二等座',15050,NULL,0),(2,1,'D942','SZX','深圳北','XMN','厦门北','20:20','23:31','二等座',15050,NULL,0),(3,1,'D3126','SZX','深圳北','XMN','厦门北','07:00','10:42','二等座',15050,NULL,0),(4,1,'D2350','SZX','深圳北','XMN','厦门北','07:35','11:13','二等座',15050,NULL,0),(5,1,'D674','SZX','深圳北','XMN','厦门北','07:45','11:33','二等座',15450,NULL,0),(6,1,'D2342','SZX','深圳北','XMN','厦门北','07:50','11:26','二等座',15050,NULL,0),(7,1,'D2294','SZX','深圳北','XMN','厦门北','07:55','11:41','二等座',15050,NULL,0),(8,1,'D2302','SZX','深圳北','XMN','厦门北','08:06','11:47','二等座',15450,NULL,0),(9,1,'D3108','SZX','深圳北','XMN','厦门北','08:11','11:54','二等座',15050,NULL,0),(10,1,'D2344','SZX','深圳北','XMN','厦门北','08:16','12:14','二等座',15050,NULL,0),(11,1,'D2308','SZX','深圳北','XMN','厦门北','08:32','12:25','二等座',15050,NULL,0),(12,1,'D2346','SZX','深圳北','XMN','厦门北','08:42','12:34','二等座',15050,NULL,0),(13,1,'D2284','SZX','深圳北','XMN','厦门北','08:47','12:39','二等座',15050,NULL,0),(14,1,'G1602','SZX','深圳北','XMN','厦门北','08:52','12:50','二等座',15050,NULL,0),(15,1,'D2282','SZX','深圳北','XMN','厦门北','09:13','13:07','二等座',15050,NULL,0),(16,1,'D682','SZX','深圳北','XMN','厦门北','09:24','13:15','二等座',15450,NULL,0),(17,1,'D2310','SZX','深圳北','XMN','厦门北','09:45','13:34','二等座',15050,NULL,0),(18,1,'D2286','SZX','深圳北','XMN','厦门北','09:55','13:47','二等座',15050,NULL,0),(19,1,'D2334','SZX','深圳北','XMN','厦门北','10:11','14:09','二等座',15450,NULL,0),(20,1,'D2316','SZX','深圳北','XMN','厦门北','10:25','14:16','二等座',15450,NULL,0),(21,1,'D2288','SZX','深圳北','XMN','厦门北','10:40','14:31','二等座',15050,NULL,0),(22,1,'D3112','SZX','深圳北','XMN','厦门北','11:17','15:11','二等座',15050,NULL,0),(23,1,'D2318','SZX','深圳北','XMN','厦门北','11:56','15:39','二等座',15050,NULL,0),(24,1,'D684','SZX','深圳北','XMN','厦门北','12:15','16:07','二等座',15450,NULL,0),(25,1,'D2338','SZX','深圳北','XMN','厦门北','12:20','16:20','二等座',15450,NULL,0),(26,1,'D2322','SZX','深圳北','XMN','厦门北','13:00','16:53','二等座',15050,NULL,0),(27,1,'D2324','SZX','深圳北','XMN','厦门北','13:54','17:54','二等座',15050,NULL,0),(28,1,'D2304','SZX','深圳北','XMN','厦门北','14:55','18:31','二等座',15050,NULL,0),(29,1,'D2298','SZX','深圳北','XMN','厦门北','15:06','18:58','二等座',15050,NULL,0),(30,1,'D2328','SZX','深圳北','XMN','厦门北','15:25','19:18','二等座',15050,NULL,0),(31,1,'D2296','SZX','深圳北','XMN','厦门北','15:38','19:23','二等座',15050,NULL,0),(32,1,'D2330','SZX','深圳北','XMN','厦门北','15:46','19:31','二等座',15050,NULL,0),(33,1,'D2306','SZX','深圳北','XMN','厦门北','16:07','19:56','二等座',15050,NULL,0),(34,1,'D672','SZX','深圳北','XMN','厦门北','16:14','20:03','二等座',15050,NULL,0),(35,1,'D2326','SZX','深圳北','XMN','厦门北','16:19','20:17','二等座',15050,NULL,0),(36,1,'D2312','SZX','深圳北','XMN','厦门北','16:43','20:37','二等座',15050,NULL,0),(37,1,'D2314','SZX','深圳北','XMN','厦门北','17:10','20:52','二等座',15050,NULL,0),(38,1,'D2315','XMN','厦门北','SZX','深圳北','07:42','11:23','二等座',15050,NULL,0),(39,1,'D2335','XMN','厦门北','SZX','深圳北','07:59','11:57','二等座',15450,NULL,0),(40,1,'D681','XMN','厦门北','SZX','深圳北','08:04','11:49','二等座',15050,NULL,0),(41,1,'D2313','XMN','厦门北','SZX','深圳北','08:50','12:38','二等座',15050,NULL,0),(42,1,'D2327','XMN','厦门北','SZX','深圳北','09:08','12:59','二等座',15050,NULL,0),(43,1,'D2311','XMN','厦门北','SZX','深圳北','09:32','13:25','二等座',15050,NULL,0),(44,1,'D2295','XMN','厦门北','SZX','深圳北','09:47','13:36','二等座',15050,NULL,0),(45,1,'D2297','XMN','厦门北','SZX','深圳北','10:44','14:26','二等座',15050,NULL,0),(46,1,'D2301','XMN','厦门北','SZX','深圳北','10:55','14:32','二等座',15050,NULL,0),(47,1,'D2323','XMN','厦门北','SZX','深圳北','11:15','14:55','二等座',15050,NULL,0),(48,1,'D3337','XMN','厦门北','SZX','深圳北','11:26','15:24','二等座',15050,NULL,0),(49,1,'D2329','XMN','厦门北','SZX','深圳北','11:37','15:29','二等座',15050,NULL,0),(50,1,'D2341','XMN','厦门北','SZX','深圳北','11:47','15:39','二等座',15050,NULL,0),(51,1,'D2303','XMN','厦门北','SZX','深圳北','11:54','15:34','二等座',15050,NULL,0),(52,1,'D673','XMN','厦门北','SZX','深圳北','11:57','15:50','二等座',15450,NULL,0),(53,1,'D2325','XMN','厦门北','SZX','深圳北','12:17','15:59','二等座',15050,NULL,0),(54,1,'D2349','XMN','厦门北','SZX','深圳北','12:28','16:15','二等座',15050,NULL,0),(55,1,'D2321','XMN','厦门北','SZX','深圳北','13:00','16:49','二等座',15050,NULL,0),(56,1,'D2345','XMN','厦门北','SZX','深圳北','13:27','17:08','二等座',15050,NULL,0),(57,1,'D2343','XMN','厦门北','SZX','深圳北','13:40','17:20','二等座',15050,NULL,0),(58,1,'D683','XMN','厦门北','SZX','深圳北','13:41','17:40','二等座',15450,NULL,0),(59,1,'D2331','XMN','厦门北','SZX','深圳北','14:19','17:57','二等座',15050,NULL,0),(60,1,'D3111','XMN','厦门北','SZX','深圳北','14:28','18:12','二等座',15050,NULL,0),(61,1,'D2317','XMN','厦门北','SZX','深圳北','14:37','18:36','二等座',15450,NULL,0),(62,1,'D2333','XMN','厦门北','SZX','深圳北','14:53','18:53','二等座',15450,NULL,0),(63,1,'D2287','XMN','厦门北','SZX','深圳北','15:43','19:19','二等座',15050,NULL,0),(64,1,'G1601','XMN','厦门北','SZX','深圳北','15:57','19:37','二等座',15050,NULL,0),(65,1,'D2319','XMN','厦门北','SZX','深圳北','16:03','19:59','二等座',15050,NULL,0),(66,1,'D2305','XMN','厦门北','SZX','深圳北','16:20','20:19','二等座',15450,NULL,0),(67,1,'D2307','XMN','厦门北','SZX','深圳北','16:24','20:12','二等座',15050,NULL,0),(68,1,'D2337','XMN','厦门北','SZX','深圳北','16:42','20:33','二等座',15450,NULL,0),(69,1,'D3125','XMN','厦门北','SZX','深圳北','16:57','20:39','二等座',15050,NULL,0),(70,1,'D2285','XMN','厦门北','SZX','深圳北','17:03','20:45','二等座',15050,NULL,0),(71,1,'D685','XMN','厦门北','SZX','深圳北','17:12','21:10','二等座',15450,NULL,0),(72,1,'D2309','XMN','厦门北','SZX','深圳北','17:17','21:16','二等座',15050,NULL,0),(73,1,'D3107','XMN','厦门北','SZX','深圳北','17:56','21:24','二等座',15050,NULL,0),(74,1,'D2281','XMN','厦门北','SZX','深圳北','18:15','22:09','二等座',15050,NULL,0),(75,1,'D2353','XMN','厦门北','SZX','深圳北','18:45','22:39','二等座',15050,NULL,0),(76,1,'D2283','XMN','厦门北','SZX','深圳北','19:08','22:51','二等座',15050,NULL,0),(77,1,'D2293','XMN','厦门北','SZX','深圳北','19:21','23:08','二等座',15050,NULL,0),(78,1,'D671','XMN','厦门北','SZX','深圳北','20:00','23:38','二等座',15050,NULL,0),(79,2,'海南航空HU7065','SZX','宝安国际机场T3','XMN','高崎国际机场T4','07:45','08:55','二等座',38500,NULL,0),(80,2,'厦门航空MF8069','SZX','宝安国际机场T3','XMN','高崎国际机场T4','09:40','10:55','二等座',128400,NULL,0),(81,2,'河北航空NS8069','SZX','宝安国际机场T3','XMN','高崎国际机场T4','09:40','10:55','二等座',129500,NULL,0),(82,2,'厦门航空MF846','SZX','宝安国际机场T3','XMN','高崎国际机场T4','20:15','21:25','二等座',129200,NULL,0);
/*!40000 ALTER TABLE `IMTravelTool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMUser`
--

DROP TABLE IF EXISTS `IMUser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMUser` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `sex` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '1男2女0未知',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户名',
  `domain` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '拼音',
  `nick` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '花名,绰号等',
  `password` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(4) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '混淆码',
  `phone` varchar(11) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '手机号码',
  `email` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'email',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '自定义用户头像',
  `departId` int(11) unsigned NOT NULL COMMENT '所属部门Id',
  `status` tinyint(2) unsigned DEFAULT '0' COMMENT '1. 试用期 2. 正式 3. 离职 4.实习',
  `created` int(11) unsigned NOT NULL COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',
  `push_shield_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0关闭勿扰 1开启勿扰',
  `sign_info` varchar(128) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '个性签名',
  PRIMARY KEY (`id`),
  KEY `idx_domain` (`domain`),
  KEY `idx_name` (`name`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMUser`
--

LOCK TABLES `IMUser` WRITE;
/*!40000 ALTER TABLE `IMUser` DISABLE KEYS */;
INSERT INTO `IMUser` VALUES (1,1,'wb','0','wb','dc85325889913846c8b5472385b38293','631','','','',1,0,1482030230,1482030230,0,'');
/*!40000 ALTER TABLE `IMUser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMRoute`
--

DROP TABLE IF EXISTS `IMRoute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMRoute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) unsigned NOT NULL COMMENT '用户id',
  `lineId` int(11) unsigned NOT NULL COMMENT '线路编号',
  `cityCode` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '所属城市编码',
  `dayCount` int(11) unsigned NOT NULL COMMENT '游玩几天',
  `startTool` int(11) unsigned NOT NULL COMMENT '起程交通工具',
  `endTool` int(11) unsigned NOT NULL COMMENT '回程交通工具',
  `startTime` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '出游时间',
  `endTime` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '结束时间',
  `quality` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '旅游质量',
  `dayNum` int(11) unsigned NOT NULL COMMENT '第几天的行程',
  `routes` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '路线',
  `hotels` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '酒店',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0 is ok',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMRoute`
--

LOCK TABLES `IMCollectRoute` WRITE;
/*!40000 ALTER TABLE `IMCollectRoute` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMCollectRoute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMCollectRoute`
--

DROP TABLE IF EXISTS `IMCollectRoute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMCollectRoute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) unsigned NOT NULL COMMENT '用户id',
  `lineId` int(11) unsigned NOT NULL COMMENT '线路编号',
  `dateFrom` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '起始日期',
  `dateTo` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '结束日期',
  `startToolNo` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `endToolNo` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '编号',
  `status` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '0 is ok',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMCollectRoute`
--

LOCK TABLES `IMCollectRoute` WRITE;
/*!40000 ALTER TABLE `IMCollectRoute` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMCollectRoute` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



DELIMITER $$

DROP PROCEDURE IF EXISTS insert_or_update_my_travel$$
CREATE PROCEDURE insert_or_update_my_travel(
IN userId INT,
IN dbidx INT,
IN cost INT,
IN dateFrom VARCHAR(16),
IN dateTo VARCHAR(16),
IN placeToCode VARCHAR(8),
IN personNum INT,
IN placeFromCode VARCHAR(8),
IN placeBackCode VARCHAR(8),
IN travelToolType INT,
IN travelTimeFrom VARCHAR(8),
IN travelTimeTo VARCHAR(8),
IN travelQualityType INT,
IN transit INT,
IN transToolToId INT,
IN transToolBackId INT,

IN playQualityType INT,
IN playTimeFrom VARCHAR(8),
IN playTimeTo VARCHAR(8),
IN playToolType INT,
IN positionType INT,
IN placeInfos VARCHAR(1024),

OUT ret INT,
OUT newId INT
)

BEGIN
    DECLARE idx INTEGER DEFAULT 1;
    DECLARE itemCount INTEGER DEFAULT 0;
    DECLARE item VARCHAR(1024);
    DECLARE i_type VARCHAR(1024);
    DECLARE i_id VARCHAR(1024);
    DECLARE i_timeFrom VARCHAR(1024);
    DECLARE i_timeTo VARCHAR(1024);
    DECLARE t_error INTEGER DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1;
    
    SET ret = 1;
   
    START TRANSACTION;
        IF dbidx=0 THEN
            INSERT INTO IMTravelBasicInfo (userId, cost, personNum, placeFromCode, placeBackCode, placeToCode, dateFrom, dateTo, toolType, timeFrom, timeTo, qualityType, transit, transToolToId, transToolBackId, playQualityType, playTimeFrom, playTimeTo, playToolType, positionType, status) VALUES(userId, cost, personNum, placeFromCode, placeBackCode, placeToCode, dateFrom, dateTo, travelToolType, travelTimeFrom, travelTimeTo, travelQualityType, transit, transToolToId, transToolBackId, playQualityType, playTimeFrom, playTimeTo, playToolType, positionType, 0); 
            SET newId=last_insert_id();
        ELSE
            SET newId=dbidx;
            DELETE FROM IMPlayDetail WHERE travelBasicId=newId;
            UPDATE IMTravelBasicInfo SET 
            cost=cost, 
            personNum=personNum, 
            placeFromCode=placeFromCode, 
            placeBackCode=placeBackCode,
            placeToCode=placeToCode,
            dateFrom=dateFrom,
            dateTo=dateTo,
            toolType=travelToolType,
            timeFrom=travelTimeFrom,
            timeTo=travelTimeTo,
            qualityType=travelQualityType,
            transit=transit,
            transToolToId=transToolToId,
            transToolBackId=transToolBackId,
            playQualityType=playQualityType,
            playTimeFrom=playTimeFrom,
            playTimeTo=playTimeTo,
            playToolType=playToolType,
            positionType=positionType
            WHERE id=newId AND userId=userId;
        END IF;
        
        
        SET itemCount = split_count(placeInfos, '|');
        
        WHILE(idx <= itemCount) DO
        BEGIN
            SET item = split(placeInfos, '|', idx);
            IF split_count(item, '&')=4 THEN
                SET i_type      = split(item, '&', 1);
                SET i_id        = split(item, '&', 2);
                SET i_timeFrom  = split(item, '&', 3);
                SET i_timeTo    = split(item, '&', 4);
                INSERT INTO IMPlayDetail (travelBasicId, type, itemId, dayTimeFrom, dayTimeTo) VALUES(newId, CONVERT(i_type, SIGNED), CONVERT(i_id, SIGNED), i_timeFrom, i_timeTo);
            END IF;
            
            SET idx = idx + 1;
        END;
        END WHILE;

    IF t_error=1 THEN
        ROLLBACK;
    ELSE
        COMMIT;
        SET ret = 0;
    END IF;
    
    SELECT ret, newId;
END $$

SET GLOBAL log_bin_trust_function_creators = 1$$
DROP FUNCTION IF EXISTS split_count$$
CREATE FUNCTION split_count(
f_string varchar(1024),
f_delimiter varchar(100)
) RETURNS int(11)
BEGIN
    IF length(f_string) = length(replace(f_string,f_delimiter,'')) THEN
        return 0;
    ELSE
        return 1+(length(f_string) - length(replace(f_string,f_delimiter,''))) / length(f_delimiter);
    END IF;
END$$

DROP FUNCTION IF EXISTS split$$
CREATE FUNCTION split(
f_string varchar(1024),
f_delimiter varchar(100),
f_order int) RETURNS varchar(1024) CHARSET utf8
BEGIN
    declare result varchar(1024) default '';
    set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),reverse(f_delimiter),1));
    return result;
END$$

DELIMITER ;

-- Dump completed on 2016-12-18 20:21:02
