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
  PRIMARY KEY (`id`),
  KEY `idx_cityCode_name_class` (`cityCode`,`name`,`class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMHotel`
--

LOCK TABLES `IMHotel` WRITE;
/*!40000 ALTER TABLE `IMHotel` DISABLE KEYS */;
INSERT INTO `IMHotel` VALUES (1,'XMN','厦门鼓浪屿嫣然小屋旅馆',100,'奢华',1,'http://www.163.com','50',45700,784,0),(2,'XMN','厦门温柔心度假屋',100,'经济',1,'http://www.baidu.com','50',62000,300,0);
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
INSERT INTO `IMScenic` VALUES (1,'XMN','鼓浪屿',83,'文娱 自然',0,1,'http://www.baidu.com','50',8,100,'09:00','17:00',0);
/*!40000 ALTER TABLE `IMScenic` ENABLE KEYS */;
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
