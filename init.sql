CREATE DATABASE IF NOT EXISTS `card_memory_db`
  /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `card_memory_db`;

-- ======================
-- USERS
-- ======================
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

DELETE FROM `app_users`;
INSERT INTO `app_users` (`user_id`, `username`, `email`, `password_hash`, `role`, `is_active`, `created_at`) VALUES
                                                                                                                 (1, 'user', 'user@example.com', '1234', 'student', 1, NOW()),
                                                                                                                 (2, 'teacher1', 'teacher1@example.com', 't1234', 'teacher', 1, NOW()),
                                                                                                                 (3, 'admin', 'admin@example.com', 'admin1234', 'admin', 1, NOW()),
                                                                                                                 (4, 'student2', 'student2@example.com', 'pass2', 'student', 1, NOW()),
                                                                                                                 (5, 'student3', 'student3@example.com', 'pass3', 'student', 1, NOW());


-- ======================
-- DECKS
-- ======================
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

DELETE FROM `decks`;
INSERT INTO `decks` (`deck_id`, `user_id`, `deck_name`, `description`, `version`, `visibility`, `is_deleted`, `created_at`) VALUES
                                                                                                                                (1, 1, 'Physics Basics', 'Intro to Physics', 1, 'private', 0, NOW()),
                                                                                                                                (2, 1, 'Biology 101', 'Basic biology flashcards', 1, 'private', 0, NOW()),
                                                                                                                                (3, 2, 'Math for Beginners', 'Simple math operations', 1, 'public', 0, NOW()),
                                                                                                                                (4, 3, 'History of Europe', 'Key dates and events', 1, 'assigned', 0, NOW()),
                                                                                                                                (5, 1, 'Chemistry Intro', 'Basic chemical reactions', 1, 'private', 0, NOW());


-- ======================
-- CARDS
-- ======================
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

DELETE FROM `cards`;
INSERT INTO `cards` (`deck_id`, `front_text`, `back_text`, `extra_info`) VALUES
                                                                             (1, 'What is force?', 'Mass times acceleration', 'Newton''s Second Law'),
                                                                             (1, 'Speed formula?', 'Distance over time', NULL),
                                                                             (2, 'Main cell organelle?', 'Nucleus', NULL),
                                                                             (3, '2 + 3 = ?', '5', NULL),
                                                                             (5, 'H2O is?', 'Water', 'Molecule of life');


-- ======================
-- DECK HISTORY
-- ======================
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

DELETE FROM `deckhistory`;
INSERT INTO `deckhistory` (`deck_id`, `version`, `deck_name`, `description`) VALUES
                                                                                 (1, 1, 'Physics Basics', 'Initial version'),
                                                                                 (2, 1, 'Biology 101', 'Initial version'),
                                                                                 (3, 1, 'Math for Beginners', 'Initial version'),
                                                                                 (4, 1, 'History of Europe', 'Initial version'),
                                                                                 (5, 1, 'Chemistry Intro', 'Initial version');


-- ======================
-- CARD HISTORY
-- ======================
CREATE TABLE IF NOT EXISTS `cardhistory` (
                                             `history_id` int(11) NOT NULL AUTO_INCREMENT,
    `card_id` int(11) NOT NULL,
    `deck_version` int(11) NOT NULL,
    `front_text` text DEFAULT NULL,
    `back_text` text DEFAULT NULL,
    `modified_at` timestamp NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`history_id`),
    KEY `card_id` (`card_id`),
    CONSTRAINT `cardhistory_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `cards` (`card_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

DELETE FROM `cardhistory`;
INSERT INTO `cardhistory` (`card_id`, `deck_version`, `front_text`, `back_text`) VALUES
                                                                                     (1, 1, 'What is force?', 'Mass times acceleration'),
                                                                                     (2, 1, 'Speed formula?', 'Distance/time'),
                                                                                     (3, 1, 'Main cell organelle?', 'Nucleus'),
                                                                                     (4, 1, '2 + 3', '5'),
                                                                                     (5, 1, 'H2O', 'Water');


-- ======================
-- DECK ASSIGNMENTS
-- ======================
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

DELETE FROM `deckassignments`;
INSERT INTO `deckassignments` (`deck_id`, `student_id`) VALUES
                                                            (1, 1), (2, 1), (3, 4), (4, 5), (5, 1);


-- ======================
-- GAME SESSIONS
-- ======================
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

DELETE FROM `gamesessions`;
INSERT INTO `gamesessions` (`user_id`, `deck_id`, `start_time`) VALUES
                                                                    (1, 1, NOW()), (1, 2, NOW()), (2, 3, NOW()), (4, 4, NOW()), (1, 5, NOW());


-- ======================
-- SESSION RESULTS
-- ======================
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

DELETE FROM `sessionresults`;
INSERT INTO `sessionresults` (`session_id`, `card_id`, `is_correct`, `response_time`) VALUES
                                                                                          (1, 1, 1, 5),
                                                                                          (1, 2, 0, 8),
                                                                                          (2, 3, 1, 6),
                                                                                          (3, 4, 1, 4),
                                                                                          (5, 5, 1, 7);


-- ======================
-- LOCALIZATION
-- ======================
CREATE TABLE IF NOT EXISTS `localization` (
                                              `translation_id` int(11) NOT NULL AUTO_INCREMENT,
    `entity_type` enum('deck','card') NOT NULL,
    `entity_id` int(11) NOT NULL,
    `language_code` enum('en','fi','ru') NOT NULL,
    `translated_text` text NOT NULL,
    PRIMARY KEY (`translation_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

DELETE FROM `localization`;
INSERT INTO `localization` (`entity_type`, `entity_id`, `language_code`, `translated_text`) VALUES
                                                                                                ('deck', 1, 'fi', 'Fysiikan perusteet'),
                                                                                                ('deck', 2, 'fi', 'Biologia 101'),
                                                                                                ('deck', 3, 'fi', 'Matematiikan alkeet'),
                                                                                                ('card', 1, 'fi', 'Mikä on voima?'),
                                                                                                ('card', 5, 'fi', 'H2O on vettä');

GRANT ALL PRIVILEGES ON card_memory_db.* TO 'app_user'@'%';
FLUSH PRIVILEGES;
