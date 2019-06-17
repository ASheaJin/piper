package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.*;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 出版社的创建 查看 订阅 搜索
 * Created by 115477 on 2018/11/27.
 */
@CrossOrigin
@RestController
@RequestMapping("/subcribe")
@Api(value = "subcribe", tags = "subcribe")
public class PiperSubcribeController {

	//定义各个任务的指令周期

	//help
	// 1、help消息组装，轮询处理
	// 2、指令进度控制（指令执行管理表）。超时判断
	// 3、处理指令

	//发布文章
	//1、判断用户是否注册了出版社
	//2、获取订阅该用户的读者列表
	//3、逐个发文章

	//创建出版社接口
	// 1、解析指令,获取创建指令x.x
	// 2、创建指令，存储创建前数据
	// 3、接受创建结果，存储公私钥
	// 4、返回自构建的创建结果
	@Autowired
	PiperPublisherService publisherService;

	@Autowired
	PiperSubscriptionService subscriptionService;


	@PostMapping("/checkSub")
	public ResponseEntity checkSub(@RequestBody CheckSubInput cis) {
		//京交会根据内容判断是否订阅
		return new ResponseEntity(subscriptionService.getsubscribeByUidCid(cis.getUserId(), cis.getPublisherId()));
	}


	@PostMapping("/getmysubsion")
	public ResponseEntity getmysubsion(@RequestBody RecomListParam upm) {
		Integer pageNo = StringUtils.isNullOrEmpty(upm.getPageNo()) ? 1 : Integer.parseInt(upm.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(upm.getPageSize()) ? 20 : Integer.parseInt(upm.getPageSize());


		return new ResponseEntity(subscriptionService.getPersonSubscribtions(upm.getUserId(),pageNo,pageSize));
	}

	@PostMapping("/getPersonSubsions")
	public ResponseEntity getPersonSubsions(@RequestBody RecomListParam upm) {
		Integer pageNo = StringUtils.isNullOrEmpty(upm.getPageNo()) ? 1 : Integer.parseInt(upm.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(upm.getPageSize()) ? 20 : Integer.parseInt(upm.getPageSize());


		return new ResponseEntity(subscriptionService.getPersonSubscribtions(upm.getUserId(),pageNo,pageSize));
	}

	@PostMapping("/getOrgSubsions")
	public ResponseEntity getOrgSubsions(@RequestBody RecomListParam upm) {
		Integer pageNo = StringUtils.isNullOrEmpty(upm.getPageNo()) ? 1 : Integer.parseInt(upm.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(upm.getPageSize()) ? 20 : Integer.parseInt(upm.getPageSize());


		return new ResponseEntity(subscriptionService.getOrgSubscribtions(upm.getUserId(),pageNo,pageSize));
	}

	@RequestMapping(value = "/getPubSubsions", method = RequestMethod.POST)
	public ResponseEntity getPublisersubsions(@RequestBody SubSearchParam subSearchParam) {
		int pageno = StringUtils.getInteger(subSearchParam.getPageNo()) == 0 ? 1 : StringUtils.getInteger(subSearchParam.getPageNo());
		int pagesize = StringUtils.getInteger(subSearchParam.getPageSize()) == 0 ? 20 : StringUtils.getInteger(subSearchParam.getPageSize());

		return new ResponseEntity(subscriptionService.getSubscribersByUserId(subSearchParam.getKeyword(), subSearchParam.getUserId(), subSearchParam.getPublisherId(), PublisherTypeEnums.organize, pageno, pagesize));
	}
}
