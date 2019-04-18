package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.SendParam;
import com.syswin.pipeline.service.SpiderTokenService;
import com.syswin.pipeline.service.bussiness.PublisherSecService;
import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Publisher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
			throw new BusinessException("ex.tokenInvalid");
		}
		Publisher publisher = publisherService.getPubLisherByPublishTmail(msg.getPiperTemail(), null);
		if (publisher == null) {
			throw new BusinessException("ex.publisher.null");
		}
		String txt = "{\"text\":\"" + msg.getContent() + "\"}";
		Integer num = publisherSecService.dealpusharticle(publisher, 1, txt, publisher.getPtype());
		return new ResponseEntity(num);
	}

}
