-- ----------------------------
--  Table structure for `app_config`
--  公司序号和个人需要就对应各自的ID，
--  为了未来分库，会根据id号来自动分配所属的库
-- ----------------------------
DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `name` varchar(100) NOT NULL COMMENT '配置名',
  `data` varchar(255) DEFAULT NULL COMMENT '配置值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';
-- ----------------------------
--  Records of `app_config`
-- ----------------------------
BEGIN;
INSERT INTO app_config(id,name,data) VALUES ('1', 'orgId','100000');
INSERT INTO app_config(id,name,data) VALUES ('2', 'personId','100000');
COMMIT;

-- ----------------------------
--  Table structure for `app_person`
-- ----------------------------
DROP TABLE IF EXISTS `app_person`;
CREATE TABLE `app_person` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `user_type` char(1) DEFAULT NULL COMMENT '用户类型',
  `photo` varchar(1000) DEFAULT NULL COMMENT '用户头像',
  `login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `login_flag` varchar(64) DEFAULT NULL COMMENT '是否可登录',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `db_url`varchar(100) DEFAULT NULL COMMENT '所属数据库',
  `register_from` smallint DEFAULT NULL COMMENT '注册来源',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_person_mobile` (`mobile`),
  UNIQUE KEY `app_person_login_name` (`login_name`),
  KEY `app_person_update_date` (`update_date`),
  KEY `app_person_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人表';

-- ----------------------------
--  Table structure for `app_config`
--  公司序号和个人需要就对应各自的ID，
--  为了未来分库，会根据id号来自动分配所属的库
-- ----------------------------
DROP TABLE IF EXISTS `app_person_token`;
CREATE TABLE `app_person_token` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `person_id` varchar(64) NOT NULL COMMENT '个人序号',
  `app_login_token` char(32) DEFAULT NULL,
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `expired_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_person_token` (`person_id`,`app_login_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人登录令牌';


-- ----------------------------
--  Table structure for `app_admin`
-- ----------------------------
DROP TABLE IF EXISTS `app_admin`;
CREATE TABLE `app_admin` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `login_flag` varchar(64) DEFAULT NULL COMMENT '是否可登录',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `org_id` varchar(64) NOT NULL COMMENT '管理组织' ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_admin_mobile` (`mobile`),
  UNIQUE KEY `app_admin_login_name` (`login_name`),
  KEY `app_admin_update_date` (`update_date`),
  KEY `app_admin_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员账户表';
-- ----------------------------
--  Records of `app_admin`
-- ----------------------------
BEGIN;
INSERT INTO app_admin(id,login_name,password,name,create_date,update_date,org_id) VALUES ('1', 'sysadmin','sysadmin','超级管理员',now(),now(),'1');
COMMIT;

-- ----------------------------
--  Table structure for `app_config`
--  公司序号和个人需要就对应各自的ID，
--  为了未来分库，会根据id号来自动分配所属的库
-- ----------------------------
DROP TABLE IF EXISTS `app_admin_token`;
CREATE TABLE `app_admin_token` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `admin_id` varchar(64) NOT NULL COMMENT '组织序号',
  `app_login_token` char(32) DEFAULT NULL,
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `expired_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_admin_token` (`admin_id`,`app_login_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织登录令牌';

-- ----------------------------
--  Table structure for `app_org`
-- ----------------------------
DROP TABLE IF EXISTS `app_org`;
CREATE TABLE `app_org` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `org_type` smallint DEFAULT 0 NOT NULL COMMENT '机构类型' ,
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `db_url`varchar(100) DEFAULT NULL COMMENT '所属数据库',
  PRIMARY KEY (`id`),
  KEY `app_org_del_flag` (`del_flag`),
  KEY `app_org_type` (`org_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';

BEGIN;
INSERT INTO app_org(id,name,org_type,create_date,update_date) VALUES ('1', '缺省组织',0,now(),now());
COMMIT;

-- ----------------------------
--  Table structure for `app_org_type`
-- ----------------------------
DROP TABLE IF EXISTS `app_org_type`;
CREATE TABLE `app_org_type` (
  `type` smallint NOT NULL COMMENT '序号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  PRIMARY KEY (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构类型表';
-- ----------------------------
--  Records of `app_org_type`
-- ----------------------------
BEGIN;
INSERT INTO app_org_type(type,name) VALUES (0,'团队');
INSERT INTO app_org_type(type,name) VALUES (1,'国有');
INSERT INTO app_org_type(type,name) VALUES (2,'外资');
INSERT INTO app_org_type(type,name) VALUES (3,'民营');
INSERT INTO app_org_type(type,name) VALUES (4,'个体');
INSERT INTO app_org_type(type,name) VALUES (5,'事业');
INSERT INTO app_org_type(type,name) VALUES (6,'政府');
INSERT INTO app_org_type(type,name) VALUES (7,'NGO');
COMMIT;

-- ----------------------------
--  Table structure for `app_sms`
--  个人或管理员账户验证用的短信记录
-- ----------------------------
DROP TABLE IF EXISTS `app_sms`;
CREATE TABLE `app_sms` (
  `id` varchar(64) NOT NULL  COMMENT '编号',
  `mobile` char(11) DEFAULT NULL,
  `msg` varchar(1000) DEFAULT NULL,
  `type` char(1) DEFAULT NULL,
  `expired_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  `is_received` char(1) DEFAULT '0',
  `sync_return_result` varchar(1000) DEFAULT NULL,
  `code` char(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_sms_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '短信表';

