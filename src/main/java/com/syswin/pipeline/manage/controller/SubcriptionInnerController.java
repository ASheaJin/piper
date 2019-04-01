package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.vo.*;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.db.model.Subscription;
import com.syswin.sub.api.utils.EnumsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 115477 on 2019/1/8.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/subcription")
@Api(value = "subcription", tags = "订阅相关")
public class SubcriptionInnerController {

	@Autowired
	private PiperSubscriptionService subscriptionService;

	@PostMapping("/list")
	@ApiOperation(
					value = "出版社列表"
	)
	public ResponseEntity<PageInfo> list(@RequestBody SubcriptionListParam slp) {
		Integer pageNo = StringUtils.isNullOrEmpty(slp.getPageNo()) ? 1 : Integer.parseInt(slp.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(slp.getPageSize()) ? 20 : Integer.parseInt(slp.getPageSize());

		return new ResponseEntity(subscriptionService.list(pageNo, pageSize, slp.getUserId(), slp.getKeyword(), slp.getPublisherId()));
	}


	@PostMapping("/subscribe")
	@ApiOperation(
					value = "添加出版社"
	)
	public ResponseEntity<Subscription> subscribe(@RequestBody SubcriptionAddParam sap) {
		Subscription subscription = subscriptionService.subscribe(sap.getUserId(), sap.getPublisherId(), null);
		return new ResponseEntity(subscription);
	}


	@PostMapping("/unsubscribe")
	@ApiOperation(
					value = "取消订阅"
	)
	public ResponseEntity unsubscribe(@RequestBody DelPublisherParam publisherParam) {
		subscriptionService.unsubscribe(publisherParam.getUserId(), publisherParam.getPublusherId());
		return new ResponseEntity();
	}
}
