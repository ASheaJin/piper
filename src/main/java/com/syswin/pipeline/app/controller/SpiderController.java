package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.PsSubOrgListParam;
import com.syswin.pipeline.app.dto.PublishMessageParam;
import com.syswin.pipeline.app.dto.SendParam;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.org.IOrgService;
import com.syswin.pipeline.service.org.OrgOut;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.PubKey;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.db.model.Subscription;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
	PublisherService publisherService;
	@Autowired
	SubscriptionService scriptionService;

	@PostMapping("/send")
	@ApiOperation(
					value = "发送消息"
	)
	public String sendOthermessage(@RequestBody SendParam msg) {
		List<String> userIds = scriptionService.getSubscribers(msg.getPiperTemail(), null);
		for (String userId : userIds) {
			sendMessegeService.sendTextmessage(msg.getContent(), msg.getPiperTemail(), 0, userId);
		}
		return "success";
	}

}
