package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.vo.PublisherListParam;
import com.syswin.pipeline.manage.vo.PublisherParam;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.utils.EnumsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2019/1/8.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/publisher")
@Api(value = "publisher", tags = "publisher")
public class PublisherInnerController {

	@Autowired
	private PiperPublisherService publisherService;
	@Autowired
	private PiperSubscriptionService subscriptionService;

	@PostMapping("/list")
	@ApiOperation(
					value = "出版社列表"
	)
	public ResponseEntity<PageInfo> list(@RequestBody PublisherListParam plb) {
		Integer pageNo = StringUtils.isNullOrEmpty(plb.getPageNo()) ? 1 : Integer.parseInt(plb.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(plb.getPageSize()) ? 20 : Integer.parseInt(plb.getPageSize());

		return new ResponseEntity(publisherService.list(pageNo,pageSize,plb.getKeyword(),plb.getUserId()));
	}


	@PostMapping("/getPiperType")
	public ResponseEntity getPiperType() {

		return new ResponseEntity(EnumsUtil.toList());
	}

	@PostMapping("/add")
	@ApiOperation(
					value = "添加出版社"
	)
	public ResponseEntity add(@RequestBody PublisherParam publisherParam) {
		Publisher publisher = publisherService.addPublisher(publisherParam.getUserId(), publisherParam.getPublishName(), publisherParam.getPublishMail(), Integer.parseInt(publisherParam.getPiperType()));
		return new ResponseEntity(publisher);
	}

}
