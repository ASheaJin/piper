use temail_subengine;

CREATE TABLE `contentout` (
  `content_id` varchar(20) NOT NULL,
  `listdesc` varchar(5000) DEFAULT NULL,
  `allcontent` varchar(15838) DEFAULT NULL,
  `publisher_id` varchar(20) DEFAULT NULL COMMENT '历史消息查询表',
  `create_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章输出';



use temail_piper;

##consumer重构了，以前数据清空重新生成
drop table consumer;


CREATE TABLE `consumer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户id(秘邮账号)',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `ptemail` varchar(64) DEFAULT NULL,
  `curversion` varchar(20) DEFAULT '1',
  `role` varchar(5) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户版本控制';


CREATE TABLE `device_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(45) DEFAULT NULL,
  `language` varchar(10) DEFAULT NULL,
  `platform` varchar(45) DEFAULT NULL,
  `moduleVersion` varchar(20) DEFAULT NULL,
  `os_version` varchar(10) DEFAULT NULL,
  `appversion` varchar(20) DEFAULT NULL,
  `build` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_UNIQUE` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `recommendcontent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` varchar(20) NOT NULL COMMENT '推荐文章Id',
  `user_id` varchar(45) DEFAULT NULL COMMENT '推荐人秘邮号',
  `create_time` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_id_UNIQUE` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='内容推荐';

CREATE TABLE `recommendpublisher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publisher_id` varchar(20) NOT NULL COMMENT '出版社Id',
  `user_id` varchar(45) DEFAULT NULL COMMENT '推荐人秘邮号',
  `create_time` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_id_UNIQUE` (`publisher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='出版社推荐';


CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '菜单id',
  `parent_id` bigint(20) NOT NULL DEFAULT '-1',
  `name` varchar(45) NOT NULL DEFAULT '',
  `url` varchar(200) NOT NULL DEFAULT '' COMMENT '菜单的url',
  `is_leaf` tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否叶子节点 1是 2否',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态1有效0无效',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '角色id',
  `role_name` varchar(45) NOT NULL DEFAULT '',
  `remark` varchar(100) NOT NULL DEFAULT '',
  `status` tinyint(2) NOT NULL DEFAULT '0',
  `create_time` int(11) NOT NULL DEFAULT '1',
  `creator_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建者用户id',
  `role_type` tinyint(2) NOT NULL DEFAULT '2' COMMENT '角色类型 1管理员 2普通',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '用户id',
  `login_name` varchar(45) NOT NULL DEFAULT '' COMMENT '登录名',
  `user_name` varchar(45) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮件',
  `remark` varchar(100) NOT NULL DEFAULT '' COMMENT '备注',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态 1有效0无效',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `creator_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建者用户id',
  `is_sys` tinyint(2) NOT NULL DEFAULT '0' COMMENT '系统管理员 1是 0否',
  `salt` varchar(45) NOT NULL DEFAULT '',
  PRIMARY KEY (`user_id`),
  KEY `login_name` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

