package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.RecommendInput;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.service.PiperReCommendContentService;
import com.syswin.pipeline.service.PiperReCommendPublisherService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author:lhz
 * @date:2019/4/1 14:36
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/recommend")
@Api(value = "recommend", tags = "推荐")
public class RecommendInnerController {

	@Autowired
	PiperReCommendContentService piperReCommendContentService;

	@Autowired
	PiperReCommendPublisherService piperReCommendPublisherService;

	@PostMapping("/publisherList")
	@ApiOperation(
					value = "获取出版社列表"
	)
	public ResponseEntity publisherList(@RequestBody RecommendInput recommendInput) {
		PageInfo<ReCommendContent> pageInfo = piperReCommendContentService.list(recommendInput.getPageNo(), recommendInput.getPageSize());

		return new ResponseEntity(pageInfo);
	}

	@PostMapping("/contentList")
	@ApiOperation(
					value = "获取推荐内容列表"
	)
	public ResponseEntity contentList(@RequestBody RecommendInput recommendInput) {

		PageInfo<ReCommendPublisher> pageInfo = piperReCommendPublisherService.list(recommendInput.getPageNo(), recommendInput.getPageSize());

		return new ResponseEntity(pageInfo);

	}
}
