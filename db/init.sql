-- --------------------------------------------------------
-- MariaDB Database Initialization Script for Card Memory App
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Create database
CREATE DATABASE IF NOT EXISTS `card_memory_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;

-- Create user and grant privileges
-- Note: In Docker, we use '%' instead of 'localhost' to allow connections from any host within the Docker network
CREATE USER IF NOT EXISTS 'appuser'@'%' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE ON card_memory_db.* TO 'appuser'@'%';
FLUSH PRIVILEGES;

USE `card_memory_db`;

-- Create tables
CREATE TABLE IF NOT EXISTS `app_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` enum('teacher','student','admin') DEFAULT 'student',
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `decks` (
  `deck_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `deck_name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `version` int(11) DEFAULT 1,
  `visibility` enum('private','public','assigned') DEFAULT 'private',
  `is_deleted` tinyint(1) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`deck_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `decks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `cards` (
  `card_id` int(11) NOT NULL AUTO_INCREMENT,
  `deck_id` int(11) NOT NULL,
  `front_text` text NOT NULL,
  `back_text` text NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `extra_info` text DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`card_id`),
  KEY `deck_id` (`deck_id`),
  CONSTRAINT `cards_ibfk_1` FOREIGN KEY (`deck_id`) REFERENCES `decks` (`deck_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `cardhistory` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `card_id` int(11) NOT NULL,
  `deck_version` int(11) NOT NULL,
  `front_text` text DEFAULT NULL,
  `back_text` text DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `extra_info` text DEFAULT NULL,
  `modified_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`history_id`),
  KEY `card_id` (`card_id`),
  CONSTRAINT `cardhistory_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `cards` (`card_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `deckassignments` (
  `assignment_id` int(11) NOT NULL AUTO_INCREMENT,
  `deck_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `assigned_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`assignment_id`),
  KEY `deck_id` (`deck_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `deckassignments_ibfk_1` FOREIGN KEY (`deck_id`) REFERENCES `decks` (`deck_id`),
  CONSTRAINT `deckassignments_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `app_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `deckhistory` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `deck_id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `deck_name` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `modified_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`history_id`),
  KEY `deck_id` (`deck_id`),
  CONSTRAINT `deckhistory_ibfk_1` FOREIGN KEY (`deck_id`) REFERENCES `decks` (`deck_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `gamesessions` (
  `session_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `deck_id` int(11) NOT NULL,
  `start_time` timestamp NULL DEFAULT current_timestamp(),
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`session_id`),
  KEY `user_id` (`user_id`),
  KEY `deck_id` (`deck_id`),
  CONSTRAINT `gamesessions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `gamesessions_ibfk_2` FOREIGN KEY (`deck_id`) REFERENCES `decks` (`deck_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `localization` (
  `translation_id` int(11) NOT NULL AUTO_INCREMENT,
  `entity_type` enum('deck','card') NOT NULL,
  `entity_id` int(11) NOT NULL,
  `language_code` enum('en','fi','ru') NOT NULL,
  `translated_text` text NOT NULL,
  PRIMARY KEY (`translation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS `sessionresults` (
  `result_id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` int(11) NOT NULL,
  `card_id` int(11) NOT NULL,
  `is_correct` tinyint(1) NOT NULL,
  `response_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`result_id`),
  KEY `session_id` (`session_id`),
  KEY `card_id` (`card_id`),
  CONSTRAINT `sessionresults_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `gamesessions` (`session_id`) ON DELETE CASCADE,
  CONSTRAINT `sessionresults_ibfk_2` FOREIGN KEY (`card_id`) REFERENCES `cards` (`card_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Insert sample data
INSERT INTO `app_users` (`user_id`, `username`, `email`, `password_hash`, `role`, `is_active`, `created_at`) VALUES
	(1, 'alice', 'alice@example.com', 'hashed_password_1', 'student', 1, '2025-09-04 11:49:45'),
	(2, 'bob', 'bob@example.com', 'hashed_password_2', 'teacher', 1, '2025-09-04 11:49:45');

INSERT INTO `decks` (`deck_id`, `user_id`, `deck_name`, `description`, `version`, `visibility`, `is_deleted`, `created_at`) VALUES
	(1, 2, 'Biology Basics', 'Basic biology concepts', 1, 'assigned', 0, '2025-09-04 11:49:45'),
	(2, 2, 'Math Flashcards', 'Simple math formulas', 1, 'private', 0, '2025-09-04 11:49:45');

INSERT INTO `cards` (`card_id`, `deck_id`, `front_text`, `back_text`, `image_url`, `extra_info`, `is_deleted`) VALUES
	(1, 1, 'What is the powerhouse of the cell?', 'Mitochondria', NULL, 'Important cell organelle', 0),
	(2, 1, 'What carries genetic information?', 'DNA', NULL, 'Double helix structure', 0),
	(3, 2, '2 + 2', '4', NULL, NULL, 0),
	(4, 2, 'Derivative of x^2', '2x', NULL, NULL, 0);

INSERT INTO `deckassignments` (`assignment_id`, `deck_id`, `student_id`, `assigned_at`) VALUES
	(1, 1, 1, '2025-09-04 11:49:45');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;