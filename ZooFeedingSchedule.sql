-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: zoofeedingschedule
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `animal`
--

DROP TABLE IF EXISTS `animal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `animal` (
  `animal_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `species` varchar(100) NOT NULL,
  `diet_type` enum('HERBIVORE','CARNIVORE','OMNIVORE') NOT NULL,
  `weight_kg` decimal(8,2) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `habitat_id` int NOT NULL,
  PRIMARY KEY (`animal_id`),
  KEY `fk_animal_habitat` (`habitat_id`),
  CONSTRAINT `fk_animal_habitat` FOREIGN KEY (`habitat_id`) REFERENCES `habitat` (`habitat_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `animal_chk_1` CHECK ((`weight_kg` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animal`
--

LOCK TABLES `animal` WRITE;
/*!40000 ALTER TABLE `animal` DISABLE KEYS */;
INSERT INTO `animal` VALUES (1,'Raja','Orangutan','OMNIVORE',78.50,'2012-03-15',1),(2,'Nemo','Clownfish','OMNIVORE',0.25,'2020-07-01',2),(3,'Shira','Great White Shark','CARNIVORE',522.00,'2015-11-20',2),(4,'Simba','African Lion','CARNIVORE',190.00,'2018-05-10',3),(5,'Zara','African Elephant','HERBIVORE',4200.00,'2010-01-30',3);
/*!40000 ALTER TABLE `animal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feeding_schedule`
--

DROP TABLE IF EXISTS `feeding_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feeding_schedule` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `habitat_id` int NOT NULL,
  `staff_id` int NOT NULL,
  `food_id` int NOT NULL,
  `feeding_time` time NOT NULL,
  `quantity_kg` decimal(6,3) NOT NULL,
  `notes` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `completed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`schedule_id`),
  UNIQUE KEY `uq_schedule_slot` (`habitat_id`,`feeding_time`,`food_id`),
  KEY `idx_fs_staff_day` (`staff_id`),
  KEY `idx_fs_animal` (`habitat_id`),
  KEY `fk_fs_food` (`food_id`),
  CONSTRAINT `fk_fs_animal` FOREIGN KEY (`habitat_id`) REFERENCES `animal` (`animal_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_fs_food` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_fs_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `feeding_schedule_chk_1` CHECK ((`quantity_kg` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=311 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feeding_schedule`
--

LOCK TABLES `feeding_schedule` WRITE;
/*!40000 ALTER TABLE `feeding_schedule` DISABLE KEYS */;
INSERT INTO `feeding_schedule` VALUES (302,1,1,16,'08:00:00',2.500,'Vitamin D drops','2026-03-24 20:54:52','2026-03-24 20:54:52',0),(303,1,1,17,'08:10:00',2.500,NULL,'2026-03-24 20:54:52','2026-03-25 05:16:27',1),(304,1,1,18,'08:20:00',2.500,NULL,'2026-03-24 20:54:52','2026-03-25 05:16:30',1),(305,2,2,19,'09:30:00',0.050,'Disperse in tank section B','2026-03-24 20:54:52','2026-03-29 16:09:08',1),(306,3,2,17,'11:00:00',15.000,'Drop from feeding platform','2026-03-24 20:54:52','2026-03-24 20:54:52',0),(307,3,2,20,'11:10:00',15.000,NULL,'2026-03-24 20:54:52','2026-03-24 20:54:52',0),(308,2,2,16,'10:00:00',8.000,'Enrichment hide inside enclosure','2026-03-24 20:54:52','2026-03-28 10:01:27',0),(309,3,1,18,'07:30:00',40.000,'Split across three feeding stations','2026-03-24 20:54:52','2026-03-28 10:01:35',0),(310,3,99,19,'07:40:00',40.000,NULL,'2026-03-24 20:54:52','2026-03-28 10:01:35',0);
/*!40000 ALTER TABLE `feeding_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food` (
  `food_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `expiry_date` date DEFAULT NULL,
  `costPerUnit` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`food_id`),
  UNIQUE KEY `uq_food_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
INSERT INTO `food` VALUES (16,'Tropical Fruit Mix',500,'2026-06-08',80.00),(17,'Whole Mackerel',300,'2026-04-21',25.00),(18,'Beef Haunch',200,'2026-03-11',150.00),(19,'Mixed Greens',1000,'2026-05-30',34.00),(20,'Zooplankton Blend',1000,'2026-02-21',22.00),(21,'Banana',400,'2026-12-31',5.00),(25,'African banana',400,'2026-12-31',20.00);
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitat`
--

DROP TABLE IF EXISTS `habitat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitat` (
  `habitat_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `location` varchar(150) NOT NULL,
  `habitat_type` enum('FOREST','OCEAN','SAVANNAH') NOT NULL,
  `capacity` smallint NOT NULL DEFAULT '10' COMMENT 'Max number of animals this habitat can hold',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `food` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`habitat_id`),
  UNIQUE KEY `uq_habitat_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitat`
--

LOCK TABLES `habitat` WRITE;
/*!40000 ALTER TABLE `habitat` DISABLE KEYS */;
INSERT INTO `habitat` VALUES (1,'Borneo Rainforest','East Wing, Block A','FOREST',8,'2026-03-23 03:44:51','Beef Haunch'),(2,'Pacific Reef','West Wing, Block B','OCEAN',15,'2026-03-23 03:44:51','Zooplankton Blend'),(3,'East African Plains','South Wing, Block C','SAVANNAH',12,'2026-03-23 03:44:51','Tropical Fruit Mix'),(4,'Pacific Reef ll','Unknown','OCEAN',13,'2026-03-30 04:19:33','Tropical Fruit Mix');
/*!40000 ALTER TABLE `habitat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitat_forest`
--

DROP TABLE IF EXISTS `habitat_forest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitat_forest` (
  `habitat_id` int NOT NULL,
  `dominant_tree_species` varchar(100) NOT NULL,
  `canopy_coverage_pct` decimal(5,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`habitat_id`),
  CONSTRAINT `fk_forest_habitat` FOREIGN KEY (`habitat_id`) REFERENCES `habitat` (`habitat_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `habitat_forest_chk_1` CHECK ((`canopy_coverage_pct` between 0 and 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitat_forest`
--

LOCK TABLES `habitat_forest` WRITE;
/*!40000 ALTER TABLE `habitat_forest` DISABLE KEYS */;
INSERT INTO `habitat_forest` VALUES (1,'Dipterocarpus',85.50);
/*!40000 ALTER TABLE `habitat_forest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitat_ocean`
--

DROP TABLE IF EXISTS `habitat_ocean`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitat_ocean` (
  `habitat_id` int NOT NULL,
  `salinity_ppt` decimal(6,3) NOT NULL COMMENT 'Salinity in parts per thousand',
  `depth_meters` decimal(7,2) NOT NULL,
  `ocean_zone` enum('SUNLIGHT','TWILIGHT','MIDNIGHT') NOT NULL,
  PRIMARY KEY (`habitat_id`),
  CONSTRAINT `fk_ocean_habitat` FOREIGN KEY (`habitat_id`) REFERENCES `habitat` (`habitat_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitat_ocean`
--

LOCK TABLES `habitat_ocean` WRITE;
/*!40000 ALTER TABLE `habitat_ocean` DISABLE KEYS */;
INSERT INTO `habitat_ocean` VALUES (2,34.500,12.00,'SUNLIGHT');
/*!40000 ALTER TABLE `habitat_ocean` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitat_savannah`
--

DROP TABLE IF EXISTS `habitat_savannah`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitat_savannah` (
  `habitat_id` int NOT NULL,
  `grassland_area_sqm` decimal(10,2) NOT NULL,
  `dry_season_months` varchar(50) NOT NULL COMMENT 'E.g. "June,July,August"',
  PRIMARY KEY (`habitat_id`),
  CONSTRAINT `fk_savannah_habitat` FOREIGN KEY (`habitat_id`) REFERENCES `habitat` (`habitat_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitat_savannah`
--

LOCK TABLES `habitat_savannah` WRITE;
/*!40000 ALTER TABLE `habitat_savannah` DISABLE KEYS */;
INSERT INTO `habitat_savannah` VALUES (3,15000.00,'June,July,August,September');
/*!40000 ALTER TABLE `habitat_savannah` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staff_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(80) NOT NULL,
  `last_name` varchar(80) NOT NULL,
  `email` varchar(150) NOT NULL,
  `staff_type` enum('KEEPER','MANAGER') NOT NULL,
  `hire_date` date NOT NULL DEFAULT (curdate()),
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `password` int NOT NULL,
  `salary` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`staff_id`),
  UNIQUE KEY `uq_staff_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,'Amara','Diallo','a.diallo@zoo.com','KEEPER','2019-06-01',1,111111111,500.00),(2,'Tom','Reeves','t.reeves@zoo.com','KEEPER','2021-03-15',1,111111111,500.00),(99,'ChannorakPitou','Sor','keeper@zoo.com','KEEPER','2026-03-24',1,12345678,1200.00),(103,'Kim','','kim@zoo.com','KEEPER','2026-03-25',1,12345678,1000.00),(104,'sok','','sok@zoo.com','MANAGER','2026-03-25',1,12345678,1000.00),(105,'Sok','Sav','sav@zoo.com','MANAGER','2026-03-25',1,12345678,800.00);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff_keeper`
--

DROP TABLE IF EXISTS `staff_keeper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_keeper` (
  `staff_id` int NOT NULL,
  `certification_level` enum('TRAINEE','JUNIOR','SENIOR','LEAD') NOT NULL DEFAULT 'JUNIOR',
  `assigned_habitat_id` int NOT NULL,
  PRIMARY KEY (`staff_id`),
  KEY `fk_keeper_habitat` (`assigned_habitat_id`),
  CONSTRAINT `fk_keeper_habitat` FOREIGN KEY (`assigned_habitat_id`) REFERENCES `habitat` (`habitat_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_keeper_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_keeper`
--

LOCK TABLES `staff_keeper` WRITE;
/*!40000 ALTER TABLE `staff_keeper` DISABLE KEYS */;
INSERT INTO `staff_keeper` VALUES (1,'SENIOR',1),(2,'JUNIOR',3);
/*!40000 ALTER TABLE `staff_keeper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff_manager`
--

DROP TABLE IF EXISTS `staff_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_manager` (
  `staff_id` int NOT NULL,
  `department` varchar(100) NOT NULL,
  `managed_staff_count` smallint NOT NULL DEFAULT '0',
  PRIMARY KEY (`staff_id`),
  CONSTRAINT `fk_manager_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_manager`
--

LOCK TABLES `staff_manager` WRITE;
/*!40000 ALTER TABLE `staff_manager` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff_manager` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-30 15:03:27
