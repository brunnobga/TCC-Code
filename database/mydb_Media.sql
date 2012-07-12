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
-- Table structure for table `Media`
--

DROP TABLE IF EXISTS `Media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Media` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` tinytext,
  `size` bigint(20) unsigned DEFAULT NULL,
  `length` time DEFAULT NULL,
  `path` text,
  `format` varchar(45) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `heigth` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `note` text,
  `date` datetime DEFAULT NULL,
  `description` text,
  `tags` text,
  `artifact` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `m_type` (`type`),
  KEY `m_artifact` (`artifact`),
  CONSTRAINT `m_artifact` FOREIGN KEY (`artifact`) REFERENCES `Artifact` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `m_type` FOREIGN KEY (`type`) REFERENCES `MediaType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Media`
--

LOCK TABLES `Media` WRITE;
/*!40000 ALTER TABLE `Media` DISABLE KEYS */;
INSERT INTO `Media` VALUES (6,'Akyio CIF Original',45619200,'00:00:10','/media/hd/UT/SASQV2/videodb/akiyo_cif.yuv','yuv',352,288,1,'','2012-07-08 21:14:57','original','',3),(7,'Akyio QCIF original',11404800,'00:00:10','/media/hd/UT/SASQV2/videodb/akiyo_qcif.yuv','yuv',176,144,1,'','2012-07-08 21:30:05','original','',3),(8,'Bus CIF original',22809600,'00:00:10','/media/hd/UT/SASQV2/videodb/bus_cif.yuv','yuv',352,288,1,'','2012-07-08 20:30:30','original','',3),(9,'Bus TS degradado',5104200,'00:00:10','/media/hd/UT/SASQV2/videodb/busnet.ts','ts',352,244,2,'','2012-07-08 20:28:43','degradado','',4),(14,'akyio teste',45619200,'00:00:10','/media/hd/UT/SASQV2/videodb/akyio_teste.yuv','yuv',352,288,1,NULL,NULL,NULL,NULL,1),(15,'Bus CIF blur',22809600,'00:00:10','/media/hd/UT/SASQV2/videodb/bus_blur.yuv','yuv',352,288,1,'','2012-07-10 20:56:23',' ','',1),(16,'BUS ts',5104200,'00:00:10','/media/hd/UT/SASQV2/videodb/bus_ts2.ts','ts',352,244,2,'','2012-07-10 20:56:50',' ','',1),(17,'Akyio QCIF block',11404800,'00:00:10','/media/hd/UT/SASQV2/videodb/akyio_block.yuv','yuv',176,144,1,NULL,NULL,NULL,NULL,NULL),(20,'Akyio QCIF blur median3',11404800,'00:00:10','/media/hd/UT/SASQV2/videodb/akyio_blur.yuv','yuv',176,144,1,NULL,NULL,NULL,NULL,NULL),(21,'Bus TS',5104200,'00:00:10','/media/hd/UT/SASQV2/videodb/bus_cif_ts_original.ts','ts',352,288,2,'','2012-07-10 22:47:06','original','',3),(22,'Bus TS d2',5104200,'00:00:10','/media/hd/UT/SASQV2/videodb/bus_ts_d2.ts','ts',352,288,2,NULL,NULL,NULL,NULL,NULL),(23,'Akyio QCIF blur',11404800,'00:00:10','/media/hd/UT/SASQV2/videodb/akyio_blur2.yuv','yuv',176,144,1,NULL,NULL,NULL,NULL,NULL),(24,'Bus TS d5',5104200,'00:00:10','/media/hd/UT/SASQV2/videodb/bus_ts_d5.ts','ts',352,288,2,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `Media` ENABLE KEYS */;
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
