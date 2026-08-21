-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.21.0.7344
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for band_manager
CREATE DATABASE IF NOT EXISTS `band_manager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `band_manager`;

-- Dumping structure for table band_manager.attendances
CREATE TABLE IF NOT EXISTS `attendances` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `present` bit(1) DEFAULT NULL,
  `musician_id` bigint(20) DEFAULT NULL,
  `rehearsal_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnjaf1c7x09pnkjwocsg9ok2c1` (`musician_id`),
  KEY `FKkplagil09mm86eesahb8l6pr6` (`rehearsal_id`),
  CONSTRAINT `FKkplagil09mm86eesahb8l6pr6` FOREIGN KEY (`rehearsal_id`) REFERENCES `rehearsals` (`id`),
  CONSTRAINT `FKnjaf1c7x09pnkjwocsg9ok2c1` FOREIGN KEY (`musician_id`) REFERENCES `musicians` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table band_manager.attendances: ~3 rows (approximately)
INSERT INTO `attendances` (`id`, `present`, `musician_id`, `rehearsal_id`) VALUES
	(1, b'1', 1, 1),
	(3, b'0', 2, 3),
	(4, b'0', 2, 4);

-- Dumping structure for table band_manager.band_musicians
CREATE TABLE IF NOT EXISTS `band_musicians` (
  `musician_id` bigint(20) NOT NULL,
  `band_id` bigint(20) NOT NULL,
  KEY `FKmpg5i85yhgl1k6diid99u99kv` (`band_id`),
  KEY `FKpx2u401m9mlv5079bsb25ymld` (`musician_id`),
  CONSTRAINT `FKmpg5i85yhgl1k6diid99u99kv` FOREIGN KEY (`band_id`) REFERENCES `bands` (`id`),
  CONSTRAINT `FKpx2u401m9mlv5079bsb25ymld` FOREIGN KEY (`musician_id`) REFERENCES `musicians` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table band_manager.band_musicians: ~3 rows (approximately)
INSERT INTO `band_musicians` (`musician_id`, `band_id`) VALUES
	(2, 4),
	(1, 8),
	(1, 1);

-- Dumping structure for table band_manager.bands
CREATE TABLE IF NOT EXISTS `bands` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `founded_year` int(11) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table band_manager.bands: ~4 rows (approximately)
INSERT INTO `bands` (`id`, `founded_year`, `genre`, `name`) VALUES
	(1, 2020, 'Ambient', 'Nocturnal Paroxysms'),
	(4, 2012, 'Gothic Turbo Folk', 'Plastic Elastic Fantastic'),
	(8, 2023, 'Free Jazz', 'Updated Band');

-- Dumping structure for table band_manager.musicians
CREATE TABLE IF NOT EXISTS `musicians` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `instrument` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table band_manager.musicians: ~2 rows (approximately)
INSERT INTO `musicians` (`id`, `instrument`, `name`) VALUES
	(1, 'Bassoon', 'Mike Muscala'),
	(2, 'Harp', 'Johnny Jackal');

-- Dumping structure for table band_manager.rehearsal_rooms
CREATE TABLE IF NOT EXISTS `rehearsal_rooms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table band_manager.rehearsal_rooms: ~4 rows (approximately)
INSERT INTO `rehearsal_rooms` (`id`, `address`, `capacity`, `name`) VALUES
	(1, 'Lasagna Street 10', 8, 'Garfield the Pirate'),
	(2, 'Spider Street 8', 8, 'Spiderweb Studio'),
	(3, 'Mechanic Street 11', 6, 'Tom\'s Garage'),
	(5, 'Dizzy Street 23', 10, 'Jimmy Jazz Studio');

-- Dumping structure for table band_manager.rehearsals
CREATE TABLE IF NOT EXISTS `rehearsals` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `duration_minutes` int(11) DEFAULT NULL,
  `band_id` bigint(20) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKek3myin47a4h2wneken9frpo7` (`band_id`),
  KEY `FKjwqpwfyafh8nr45ss3k96d26b` (`room_id`),
  CONSTRAINT `FKek3myin47a4h2wneken9frpo7` FOREIGN KEY (`band_id`) REFERENCES `bands` (`id`),
  CONSTRAINT `FKjwqpwfyafh8nr45ss3k96d26b` FOREIGN KEY (`room_id`) REFERENCES `rehearsal_rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table band_manager.rehearsals: ~3 rows (approximately)
INSERT INTO `rehearsals` (`id`, `date`, `duration_minutes`, `band_id`, `room_id`) VALUES
	(1, '2025-10-12', 90, 1, 3),
	(3, '2023-11-30', 120, 4, 1),
	(4, '2026-05-15', 150, 8, 2);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
