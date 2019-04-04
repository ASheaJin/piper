package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.SendParam;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.SpiderTokenService;
import com.syswin.pipeline.service.bussiness.PublisherSecService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.Publisher;
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
	PublisherSecService publisherSecService;
	@Autowired
	SpiderTokenService spiderTokenService;
	@Autowired
	private PublisherService publisherService;

	@PostMapping("/send")
	@ApiOperation(
					value = "发送消息"
	)
	public ResponseEntity send(@RequestBody SendParam msg) {
		if (spiderTokenService.getSpiderToken(msg.getToken(), msg.getPiperTemail()) == null) {
			throw new BusinessException("Token错误");
		}
		Publisher publisher = publisherService.getPubLisherByPublishTmail(msg.getPiperTemail(), null);
		if (publisher == null) {
			throw new BusinessException("该出版社不存在");
		}
		Integer num = publisherSecService.dealpusharticle(publisher, 1, msg.getContent(), publisher.getPtype());
		return new ResponseEntity(num);
	}

}
