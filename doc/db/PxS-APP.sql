use temail_ps_app_demo;
drop table if exists admin_account_info;
CREATE TABLE `admin_account_info` (
  `account_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账号地址',
  `active_code` varchar(20) NOT NULL DEFAULT '' COMMENT '激活码',
  `active_status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0 未激活 1激活',
  `use_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0 停用 1启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `welcome_msg` varchar(50) NOT NULL DEFAULT '' COMMENT '欢迎信息',
  PRIMARY KEY (`account_id`),
  KEY `idx_account_no` (`account_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='账号表';


drop table if exists admin_app_admin;
CREATE TABLE `admin_app_admin` (
  `admin_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '管理员账号',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '管理员密码',
  `nickname` varchar(20) NOT NULL DEFAULT '' COMMENT '管理员姓名',
  `mobile` varchar(11) NOT NULL DEFAULT '' COMMENT '管理员手机号',
  `use_status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0 删除 1 激活',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `idx_user` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='管理员表';


drop table if exists admin_auto_replay_config;
CREATE TABLE `admin_auto_replay_config` (
  `replay_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账号地址',
  `in_type` varchar(10) NOT NULL DEFAULT '1,1' COMMENT 'in消息类型',
  `in_keyword` varchar(20) NOT NULL DEFAULT '' COMMENT '账号头像',
  `out_type` varchar(10) NOT NULL DEFAULT '1,1' COMMENT 'out消息类型',
  `out_message` varchar(30) NOT NULL DEFAULT '' COMMENT '输出消息类型',
  `use_status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0 停用 1启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`replay_id`),
  KEY `idx_account_no` (`account_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='自动回复消息';




drop table if exists admin_function_item;
CREATE TABLE `admin_function_item` (
  `function_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账号地址',
  `function_name` varchar(50) NOT NULL DEFAULT '' COMMENT '配置名称',
  `function_image` varchar(1024) NOT NULL DEFAULT '' COMMENT '配置图标',
  `function_url` varchar(1024) NOT NULL DEFAULT '' COMMENT '配置地址',
  `taip_host` varchar(20) NOT NULL DEFAULT '---' COMMENT 'taip服务器地址',
  `taip_port` varchar(20) NOT NULL DEFAULT '---' COMMENT 'taip服务器端口',
  `taip_command` varchar(20) NOT NULL DEFAULT '---' COMMENT '指令id',
  `taip_command_space` varchar(20) NOT NULL DEFAULT '---' COMMENT '指令space',
  `item_type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0-其他 1-头部 2-快捷方式  3-功能区',
  `display_order` tinyint(3) NOT NULL DEFAULT '0' COMMENT '排序',
  `use_status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0 停用 1启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `role_type` varchar(50) NOT NULL DEFAULT '0' COMMENT '角色类型',
  `role_desc` varchar(50) NOT NULL DEFAULT '' COMMENT '角色描述',
  PRIMARY KEY (`function_id`),
  KEY `idx_account_no` (`account_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='快捷入口配置表';



drop table if exists admin_method_invoke;
CREATE TABLE `admin_method_invoke` (
  `invoke_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账号地址',
  `method` varchar(20) NOT NULL DEFAULT '' COMMENT '账号地址',
  `invoke_date` date NOT NULL COMMENT '调用日期',
  `use_status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效 1 有效 0 无效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`invoke_id`),
  KEY `idx_account_no` (`account_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='方法调用';


drop table if exists admin_user_add_info;
CREATE TABLE `admin_user_add_info` (
  `invoke_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账号地址',
  `user_account` varchar(50) NOT NULL DEFAULT '' COMMENT '访问者邮箱地址',
  `add_time` date NOT NULL COMMENT '添加日期',
  `use_status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0 停用 1启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`invoke_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户新增表';




drop table if exists admin_user_visit_info;
CREATE TABLE `admin_user_visit_info` (
  `invoke_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账号地址',
  `user_account` varchar(50) NOT NULL DEFAULT '' COMMENT '用户账号',
  `visit_time` date NOT NULL COMMENT '访问时间',
  `use_status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0 停用 1启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`invoke_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户访问表';




drop table if exists  admin_v_card_info;
CREATE TABLE `admin_v_card_info` (
  `v_card_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库主键',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账号地址',
  `nickname` varchar(20) NOT NULL DEFAULT '' COMMENT '账号昵称',
  `account_image` varchar(1024) NOT NULL DEFAULT '' COMMENT '账号头像',
  `account_desc` varchar(20) NOT NULL DEFAULT '' COMMENT '账号信息',
  `use_status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0 停用 1启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`v_card_id`),
  KEY `idx_account_no` (`account_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='vCard信息表';