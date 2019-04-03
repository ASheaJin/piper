package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.SendParam;
import com.syswin.pipeline.service.SpiderTokenService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.SubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 115477 on 2018/12/18.
 */
@CrossOrigin
@RestController
@RequestMapping("/spider")
@Api(value = "spider", tags = "spider")
public class SpiderController {

	@Autowired
	SendMessegeService sendMessegeService;
	@Autowired
	SpiderTokenService spiderTokenService;
	@Autowired
	SubscriptionService scriptionService;

	@PostMapping("/send")
	@ApiOperation(
					value = "发送消息"
	)
	public ResponseEntity send(@RequestBody SendParam msg) {
		if (spiderTokenService.getSpiderToken(msg.getToken(), msg.getPiperTemail()) == null) {
			throw new BusinessException("Token错误");
		}
		List<String> userIds = scriptionService.getSubscribers(msg.getPiperTemail(), null);
		for (String userId : userIds) {
			sendMessegeService.sendTextmessage(msg.getContent(), userId, msg.getPiperTemail());
		}
		return new ResponseEntity();
	}

}
