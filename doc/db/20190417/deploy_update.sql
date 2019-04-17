use temail_subengine;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) NOT NULL,
  `create_time` int(11) DEFAULT '0',
  `status` tinyint(2) DEFAULT '1' COMMENT '1、有效 0 无效',
  `creater` varchar(45) DEFAULT '',
  `ptype` varchar(20) NOT NULL DEFAULT 'organize',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='用户可创建类型管理表';

CREATE TABLE `blacksub` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户Id（秘邮邮箱）',
  `publisher_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '出版社Id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_publisher` (`user_id`,`publisher_id`),
  KEY `user_id` (`user_id`),
  KEY `publisher_id` (`publisher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='黑名单';

CREATE TABLE `content` (
  `content_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '文章Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `publisher_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '发布者Id',
  `title` varchar(100) DEFAULT '' COMMENT '标题',
  `subtitle` varchar(200) DEFAULT '' COMMENT '概要',
  `content` varchar(15838) DEFAULT '' COMMENT '文章内容',
  `url` varchar(200) DEFAULT '' COMMENT '文章url',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态（0,无效 1 有效）',
  `body_type` int(11) DEFAULT '1',
  PRIMARY KEY (`content_id`),
  KEY `publisher_id` (`publisher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章详情';

CREATE TABLE `contentout` (
  `content_id` varchar(20) NOT NULL,
  `listdesc` varchar(5000) DEFAULT '',
  `allcontent` varchar(15838) DEFAULT '',
  `publisher_id` varchar(20) DEFAULT '' COMMENT '历史消息查询表',
  `create_time` int(11) DEFAULT '0',
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章输出';

CREATE TABLE `contribute_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '投稿Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `userId` varchar(100) NOT NULL DEFAULT '' COMMENT '投稿者',
  `publisher_id` varchar(20) DEFAULT '0' COMMENT '出版社Id',
  `content` varchar(5000) DEFAULT '' COMMENT '投稿内容',
  `url` varchar(200) DEFAULT '' COMMENT '投稿url',
  `hasread` varchar(6) DEFAULT 'NO',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投稿记录';

CREATE TABLE `logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) NOT NULL,
  `toemail` varchar(45) DEFAULT '',
  `body_type` int(11) DEFAULT '0' COMMENT '1、文本 2、语音3、图片4、视频5、文件',
  `text` text,
  `create_time` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=353 DEFAULT CHARSET=utf8 COMMENT='接受单聊消息记录';

CREATE TABLE `operating_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) DEFAULT '',
  `opinfo` varchar(100) DEFAULT '',
  `create_time` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=213 DEFAULT CHARSET=utf8 COMMENT='管理员操作记录表';

CREATE TABLE `publisher` (
  `publisher_id` varchar(20) NOT NULL DEFAULT '0' COMMENT '主键',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户id',
  `ptemail` varchar(64) NOT NULL DEFAULT '' COMMENT '出版社以p打头的邮箱',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '名称',
  `avatar` varchar(256) DEFAULT '' COMMENT '头像',
  `introduce` varchar(256) DEFAULT '' COMMENT '介绍',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `ptype` varchar(20) NOT NULL DEFAULT 'person' COMMENT '生产源类型 枚举（个人、组织、其他）',
  `status` tinyint(2) DEFAULT '1',
  PRIMARY KEY (`publisher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出版社';

CREATE TABLE `send_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '推送Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户Id （秘邮号）',
  `content_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '文章Id',
  `sendnum` int(11) NOT NULL DEFAULT '0' COMMENT '推送次数',
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='推送文章记录';

CREATE TABLE `subscription` (
  `subscription_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '订阅号Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户Id（秘邮邮箱）',
  `publisher_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '出版社Id',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态（0,无效 1 有效）',
  PRIMARY KEY (`subscription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅';



use temail_piper;
CREATE TABLE `account` (
  `account_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '主键',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户id',
  `balance` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '余额（秘票）',
  `update_time` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`account_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户';

CREATE TABLE `browse_record` (
  `browse_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '浏览Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户Id （秘邮号）',
  `content_id` bigint(18) NOT NULL DEFAULT '1' COMMENT '文章Id',
  `browse_time` int(11) NOT NULL DEFAULT '1' COMMENT '浏览时间',
  `fromtemail` varchar(64) DEFAULT '',
  PRIMARY KEY (`browse_id`),
  KEY `content_id` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送文章记录';

CREATE TABLE `card` (
  `card_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '银行卡Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `user_id` varchar(64) NOT NULL DEFAULT '1' COMMENT '用户Id （秘邮号）',
  `card_no` varchar(20) NOT NULL DEFAULT '1' COMMENT '银行卡账号',
  PRIMARY KEY (`card_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='银行卡';

CREATE TABLE `consumer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户id(秘邮账号)',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `ptemail` varchar(64) DEFAULT '',
  `curversion` varchar(20) DEFAULT '1',
  `role` varchar(5) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='用户版本控制';

CREATE TABLE `device_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(45) DEFAULT '',
  `language` varchar(10) DEFAULT '',
  `platform` varchar(45) DEFAULT '',
  `moduleVersion` varchar(20) DEFAULT '',
  `os_version` varchar(10) DEFAULT '',
  `appversion` varchar(20) DEFAULT '',
  `build` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_UNIQUE` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `evaluation` (
  `evaluation_id` varchar(20) NOT NULL DEFAULT '0' COMMENT '评星Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `level` tinyint(3) NOT NULL DEFAULT '1' COMMENT '评星等级',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户id',
  `content_id` varchar(20) NOT NULL DEFAULT '0' COMMENT '内容id',
  PRIMARY KEY (`evaluation_id`),
  KEY `content_id` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价 1~5星';

CREATE TABLE `recommendcontent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` varchar(20) NOT NULL COMMENT '推荐文章Id',
  `user_id` varchar(45) DEFAULT '' COMMENT '推荐人秘邮号',
  `create_time` int(10) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_id_UNIQUE` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8 COMMENT='内容推荐';

CREATE TABLE `recommendpublisher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publisher_id` varchar(20) NOT NULL COMMENT '出版社Id',
  `user_id` varchar(45) DEFAULT '' COMMENT '推荐人秘邮号',
  `create_time` int(10) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_id_UNIQUE` (`publisher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='出版社推荐';

CREATE TABLE `server_logs` (
  `id` varchar(20) NOT NULL DEFAULT '0',
  `user_id` varchar(45) NOT NULL,
  `command` int(11) DEFAULT '0',
  `request_data` varchar(5000) DEFAULT '',
  `create_time` varchar(20) NOT NULL,
  `response_data` varchar(5000) DEFAULT '',
  `end_time` varchar(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `spidertoken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ptemail` varchar(45) NOT NULL,
  `token` varchar(45) DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ptemail_UNIQUE` (`ptemail`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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

CREATE TABLE `token` (
  `token_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL DEFAULT '0' COMMENT 'userid',
  `token` varchar(50) NOT NULL DEFAULT '' COMMENT 'token用户身份标识',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`token_id`),
  KEY `user_id` (`user_id`),
  KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `transaction_record` (
  `transaction_id` varchar(20) NOT NULL DEFAULT '1' COMMENT '交易记录Id',
  `create_time` int(11) NOT NULL DEFAULT '1' COMMENT '创建时间',
  `user_id` varchar(64) NOT NULL DEFAULT '1' COMMENT '账户Id',
  `money` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '交易状态：0未完成，1已完成',
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '交易类型 1充值 2扣款',
  PRIMARY KEY (`transaction_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录';


INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`is_leaf`,`sort`,`status`,`create_time`) VALUES (1,-1,'系统设置','system',2,100,1,1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`is_leaf`,`sort`,`status`,`create_time`) VALUES (2,1,'角色管理','role',1,10,1,1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`is_leaf`,`sort`,`status`,`create_time`) VALUES (3,1,'菜单管理','menu',1,20,1,1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`is_leaf`,`sort`,`status`,`create_time`) VALUES (4,1,'用户管理','user',1,30,1,1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`is_leaf`,`sort`,`status`,`create_time`) VALUES (100,-1,'piper管理','mgr',2,200,1,1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`is_leaf`,`sort`,`status`,`create_time`) VALUES (101,100,'邮件组管理员','group_mgr',1,10,1,1);
INSERT INTO `sys_menu` (`menu_id`,`parent_id`,`name`,`url`,`is_leaf`,`sort`,`status`,`create_time`) VALUES (102,100,'出版社管理','publisher_mgr',1,20,1,1);

INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`status`,`create_time`,`creator_id`,`role_type`) VALUES (1,'系统管理员','',1,1554348270,1,1);
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`status`,`create_time`,`creator_id`,`role_type`) VALUES (2,'普通用户','',1,1,1,2);
INSERT INTO `sys_role` (`role_id`,`role_name`,`remark`,`status`,`create_time`,`creator_id`,`role_type`) VALUES (3,'出版社管理','',1,1,1,2);

INSERT INTO `sys_user` (`user_id`,`login_name`,`user_name`,`password`,`email`,`remark`,`status`,`create_time`,`creator_id`,`is_sys`,`salt`) VALUES (1,'admin','admin','28f57e49f0ae02b99665726ad3a7aa98','weihongyi@syswin.com','333',1,1554286347,1,1,'');

INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,1);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,2);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,3);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,4);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,100);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,101);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,102);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,103);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,104);
INSERT INTO `sys_role_menu` (`role_id`,`menu_id`) VALUES (1,152);

insert into `sys_user_role` VALUES (1, 1);
