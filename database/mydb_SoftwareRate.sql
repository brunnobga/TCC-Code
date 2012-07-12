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
-- Table structure for table `SoftwareRate`
--

DROP TABLE IF EXISTS `SoftwareRate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SoftwareRate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` double DEFAULT NULL,
  `metric` int(11) NOT NULL,
  `media` int(11) NOT NULL,
  `referenceMedia` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sr_metric` (`metric`),
  KEY `sr_media` (`media`),
  KEY `sr_referenceMedia` (`referenceMedia`),
  CONSTRAINT `sr_media` FOREIGN KEY (`media`) REFERENCES `Media` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sr_metric` FOREIGN KEY (`metric`) REFERENCES `Metric` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sr_referenceMedia` FOREIGN KEY (`referenceMedia`) REFERENCES `Media` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SoftwareRate`
--

LOCK TABLES `SoftwareRate` WRITE;
/*!40000 ALTER TABLE `SoftwareRate` DISABLE KEYS */;
INSERT INTO `SoftwareRate` VALUES (1,35.160047,3,14,6),(2,16.553222,2,20,7),(3,35.941978,3,20,7),(4,0.982565,4,20,7),(5,192.501273,2,8,15);
/*!40000 ALTER TABLE `SoftwareRate` ENABLE KEYS */;
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
