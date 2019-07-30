use temail_piper;

CREATE TABLE `censor_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `obj_id` varchar(45) NOT NULL DEFAULT '' COMMENT '审核内容主键id',
  `label` varchar(45) NOT NULL COMMENT '内容分类',
  `score` decimal(20,18) NOT NULL DEFAULT '0.000000000000000000' COMMENT '得分',
  `type` varchar(45) NOT NULL DEFAULT '' COMMENT '类型 内容/出版社等',
  `create_time` bigint(18) NOT NULL DEFAULT '1' COMMENT '开始时间',
  `time_cost` int(11) NOT NULL DEFAULT '0' COMMENT '审核耗时',
  PRIMARY KEY (`id`),
  KEY `obj_id` (`obj_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='审核结果表';