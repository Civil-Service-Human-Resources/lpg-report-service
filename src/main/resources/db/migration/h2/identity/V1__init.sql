CREATE SCHEMA identity;
USE identity;

CREATE TABLE `client` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `uid` char(36) DEFAULT NULL UNIQUE,
  `password` varchar(100) DEFAULT NULL,
  `redirect_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `identity` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `locked` bit(1) NOT NULL,
  `deletion_notification_sent` bit(1) DEFAULT FALSE,
  `uid` char(36) NOT NULL UNIQUE,
  `last_logged_in` datetime NOT NULL,
  `email` varchar(150) NOT NULL UNIQUE,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `role` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL UNIQUE,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `identity_role` (
  `identity_id` mediumint(8) unsigned NOT NULL,
  `role_id` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`identity_id`,`role_id`),
  CONSTRAINT `FK_identity_role_identity` FOREIGN KEY (`identity_id`) REFERENCES `identity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_identity_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `invite` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `inviter_id` mediumint(8) unsigned NOT NULL,
  `code` char(40) NOT NULL UNIQUE,
  `status` varchar(10) NOT NULL,
  `for_email` varchar(150) NOT NULL,
  `invited_at` datetime NOT NULL,
  `accepted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_invite_identity` FOREIGN KEY (`inviter_id`) REFERENCES `identity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `invite_role` (
  `invite_id` mediumint(8) unsigned NOT NULL,
  `role_id` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`invite_id`,`role_id`),
  CONSTRAINT `FK_invite_role_invite` FOREIGN KEY (`invite_id`) REFERENCES `invite` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_invite_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `reset` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` char(40) NOT NULL UNIQUE,
  `email` varchar(150) NOT NULL,
  `reset_status` varchar(10) NOT NULL,
  `requested_at` datetime NOT NULL,
  `reset_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `token` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status` int(11) unsigned DEFAULT NULL,
  `token_id` varchar(255) DEFAULT NULL UNIQUE,
  `authentication` longblob,
  `authentication_id` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `token` longblob,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
