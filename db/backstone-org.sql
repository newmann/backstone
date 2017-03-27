-- ----------------------------
--  Table structure for `org_user`
-- ----------------------------
DROP TABLE IF EXISTS `org_user`;
CREATE TABLE `org_user` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `org_id` varchar(64) NOT NULL COMMENT '归属公司',
  `office_id` varchar(64) NOT NULL COMMENT '归属部门',
  `no` varchar(100) DEFAULT NULL COMMENT '工号',  
  `login_name` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `user_type` char(1) DEFAULT NULL COMMENT '用户类型',
  `photo` varchar(1000) DEFAULT NULL COMMENT '用户头像',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `latest_pay_type` char(1) DEFAULT NULL,
  `person_id` varchar(64) DEFAULT NULL  COMMENT '关联个人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_user_mobile` (`org_id`,`mobile`),
  UNIQUE KEY `org_user_login_name` (`org_id`,`login_name`),
  KEY `org_user_update_date` (`update_date`),
  KEY `org_user_del_flag` (`del_flag`),
  KEY `org_user_person_id` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业用户表';

-- ----------------------------
--  Table structure for `org_office`
--  定义部门表
--  区域对应的是app_area表
-- ----------------------------
DROP TABLE IF EXISTS `org_office`;
CREATE TABLE `org_office` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `org_id` varchar(64) NOT NULL COMMENT '组织编号',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `area_id` varchar(64) NOT NULL COMMENT '归属区域',
  `area_code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `type` char(1) NOT NULL COMMENT '机构类型',
  `grade` char(1) NOT NULL COMMENT '机构等级',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `useable` varchar(64) DEFAULT NULL COMMENT '是否启用',
  `primary_person` varchar(64) DEFAULT NULL COMMENT '主负责人',
  `deputy_person` varchar(64) DEFAULT NULL COMMENT '副负责人',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `org_office_parent_id` (`parent_id`),
  KEY `org_office_del_flag` (`del_flag`),
  KEY `org_office_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';

-- ----------------------------
--  Table structure for `org_sms`
--  每个组织管理自己的短信发送情况
-- ----------------------------
DROP TABLE IF EXISTS `org_sms`;
CREATE TABLE `org_sms` (
  `id` varchar(64) NOT NULL  COMMENT '编号',
  `org_id` varchar(64) DEFAULT NULL COMMENT '所属组织',
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '短信表';

-- ----------------------------
--  Table structure for `org_role`
--  角色数据，在一个公司内统一使用一套角色模型
-- ----------------------------
DROP TABLE IF EXISTS `org_role`;
CREATE TABLE `org_role` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `org_id` varchar(64) DEFAULT NULL COMMENT '归属组织',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enname` varchar(255) DEFAULT NULL COMMENT '英文名称',
  `role_type` varchar(255) DEFAULT NULL COMMENT '角色类型',
  `data_scope` char(1) DEFAULT NULL COMMENT '数据范围',
  `is_sys` varchar(64) DEFAULT NULL COMMENT '是否系统数据',
  `useable` varchar(64) DEFAULT NULL COMMENT '是否可用',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_role_name` (`org_id`,`name`),
  KEY `org_role_del_flag` (`del_flag`),
  KEY `org_role_enname` (`enname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
--  Table structure for `org_menu`
-- ----------------------------
DROP TABLE IF EXISTS `org_menu`;
CREATE TABLE `org_menu` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `org_id` varchar(64) DEFAULT NULL COMMENT '归属组织',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `href` varchar(2000) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_separator` char(1) NOT NULL COMMENT '是否为分隔符',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_menu_permission` (`org_id`,`permission`),
  KEY `org_menu_parent_id` (`parent_id`),
  KEY `org_menu_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
--  Table structure for `org_permit`
--  定义权限资源
--  每个模块是单独一条记录
--  权限资源是一个树形架构
--  典型模式如下：
--    用户管理模块
--          新增
--          修改
--          删除
--          修改口令
-- ----------------------------
DROP TABLE IF EXISTS `org_permit`;
CREATE TABLE `org_permit` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `org_id` varchar(64) DEFAULT NULL COMMENT '归属组织',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `is_module` char(1) NOT NULL COMMENT '是否为模块',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_permit_permission` (`org_id`,`permission`),
  KEY `org_permit_parent_id` (`parent_id`),
  KEY `org_permit_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限定义表';

-- ----------------------------
--  Table structure for `org_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `org_role_menu`;
CREATE TABLE `org_role_menu` (
  `org_id` varchar(64) DEFAULT NULL COMMENT '归属组织',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `org_role_menu_org` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单';

-- ----------------------------
--  Table structure for `org_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `org_role_permit`;
CREATE TABLE `org_role_permit` (
  `org_id` varchar(64) DEFAULT NULL COMMENT '归属组织',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `permit_id` varchar(64) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_id`,`permit_id`),
  KEY `org_role_permit_org` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单';

-- ----------------------------
--  Table structure for `org_role_office`
-- ----------------------------
DROP TABLE IF EXISTS `org_role_office`;
CREATE TABLE `org_role_office` (
  `org_id` varchar(64) DEFAULT NULL COMMENT '归属组织',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `office_id` varchar(64) NOT NULL COMMENT '机构编号',
  PRIMARY KEY (`role_id`,`office_id`),
  KEY `org_role_office_org` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-机构';

-- ----------------------------
--  Table structure for `org_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `org_user_role`;
CREATE TABLE `org_user_role` (
  `org_id` varchar(64) DEFAULT NULL COMMENT '归属组织',
  `user_id` varchar(64) NOT NULL COMMENT '用户编号',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `org_user_role_org` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色';

