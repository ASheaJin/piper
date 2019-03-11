package com.syswin.pipeline.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发布内容、查看、评价、搜索
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@RestController
@RequestMapping("/content")
public class ContentController {

	//help
	// 1、help消息组装，轮询处理
	// 2、指令进度控制（指令执行管理表）。超时判断
  // 3、处理指令

	//发布内容
	//查看内容详情 记录最近的出版社Id，内容Id。
	//内容评分
	//获取当前内容列表
	//查看历史内容列表 传入已拉取的Id
}
