-- 执墨数据库初始化脚本

CREATE DATABASE IF NOT EXISTS zhi_mo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zhi_mo;

-- 文章表
CREATE TABLE IF NOT EXISTS `article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '文章标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要',
  `content` text COMMENT '文章正文(HTML)',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面图URL',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `category` varchar(20) DEFAULT NULL COMMENT '分类',
  `status` tinyint DEFAULT '0' COMMENT '状态: 0-草稿, 1-已发布',
  `source_url` varchar(500) DEFAULT NULL COMMENT '来源URL',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 提示词模板表
CREATE TABLE IF NOT EXISTS `prompt_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `description` varchar(500) DEFAULT NULL COMMENT '模板描述',
  `content` text NOT NULL COMMENT '模板内容',
  `type` varchar(20) DEFAULT NULL COMMENT '模板类型: persona-人设, topic-话题',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提示词模板表';

-- 爆款文章表
CREATE TABLE IF NOT EXISTS `hot_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '文章标题',
  `url` varchar(500) NOT NULL COMMENT '文章链接',
  `source` varchar(50) DEFAULT NULL COMMENT '来源平台',
  `category` varchar(20) DEFAULT NULL COMMENT '赛道分类',
  `read_count` int DEFAULT '0' COMMENT '阅读量',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `crawl_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '抓取时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_crawl_time` (`crawl_time`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爆款文章表';

-- 素材表
CREATE TABLE IF NOT EXISTS `material` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '素材标题',
  `content` text COMMENT '素材内容',
  `source_url` varchar(500) DEFAULT NULL COMMENT '来源链接',
  `source_name` varchar(100) DEFAULT NULL COMMENT '来源名称',
  `category` varchar(20) DEFAULT NULL COMMENT '分类',
  `tags` varchar(500) DEFAULT NULL COMMENT '标签, 逗号分隔',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='素材表';
