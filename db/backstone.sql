-- ----------------------------
--  Table structure for `sys_person`
-- ----------------------------
DROP TABLE IF EXISTS `sys_person`;
CREATE TABLE `sys_person` (
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
  `register_from` char(1) DEFAULT NULL,
  `app_login_token` char(32) DEFAULT NULL,
  `latest_pay_type` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_person_mobile` (`mobile`),
  KEY `sys_person_login_name` (`login_name`),
  KEY `sys_person_update_date` (`update_date`),
  KEY `sys_person_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人表';

-- ----------------------------
--  Table structure for `sys_office_person`
-- ----------------------------
DROP TABLE IF EXISTS `sys_office_person`;
CREATE TABLE `sys_office_person` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `company_id` varchar(64) NOT NULL COMMENT '归属公司',
  `office_id` varchar(64) NOT NULL COMMENT '归属部门',
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `user_type` char(1) DEFAULT NULL COMMENT '用户类型',
  `photo` varchar(1000) DEFAULT NULL COMMENT '用户头像',
  `login_flag` varchar(64) DEFAULT NULL COMMENT '是否可登录',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `latest_pay_type` char(1) DEFAULT NULL,
  `person_id` varchar(64) DEFAULT NULL  COMMENT '关联个人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_office_person_mobile` (`company_id`,`mobile`),
  UNIQUE KEY `sys_office_person_login_name` (`company_id`,`login_name`),
  KEY `sys_office_person_update_date` (`update_date`),
  KEY `sys_office_person_del_flag` (`del_flag`),
  KEY `sys_office_person_person_id` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业用户表';
