CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `mydb`;
-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.5.24-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Session`
--

DROP TABLE IF EXISTS `Session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `spectorsCount` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `gradeOptions` varchar(45) DEFAULT NULL,
  `title` tinytext,
  `description` text,
  `metric` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `s_metric` (`metric`),
  CONSTRAINT `s_metric` FOREIGN KEY (`metric`) REFERENCES `Metric` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Session`
--

LOCK TABLES `Session` WRITE;
/*!40000 ALTER TABLE `Session` DISABLE KEYS */;
INSERT INTO `Session` VALUES (1,'2012-05-27 17:19:19',NULL,1,2,0,NULL,'Sessão 1','',1),(2,'2012-05-27 18:31:08',NULL,1,1,0,NULL,'Sessão 2','',1),(3,'2012-06-07 12:15:51',NULL,1,1,0,NULL,'Sessão 3','',1),(4,'2012-06-07 12:17:44',NULL,1,1,0,NULL,'Sessão 4','',5),(5,'2012-06-07 12:20:49',NULL,1,1,0,NULL,'Sessão 5','',5),(47,'2012-07-10 22:59:02',NULL,1,0,0,NULL,'Sessão 6','',1),(48,'2012-07-10 23:06:38',NULL,1,0,0,NULL,'Session 7','',1),(49,'2012-07-10 23:20:39',NULL,1,0,0,NULL,'Session 8','',1),(50,'2012-07-10 23:24:24',NULL,1,0,0,NULL,'Session 10','',5),(51,'2012-07-10 23:30:47',NULL,1,0,0,NULL,'Session 9','',5),(52,'2012-07-10 23:44:18',NULL,1,0,0,NULL,'Session 11','',1),(53,'2012-07-11 17:47:38',NULL,1,0,0,NULL,'Sessao 0','',5);
/*!40000 ALTER TABLE `Session` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-07-12 16:10:06
