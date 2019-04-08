package com.syswin.pipeline.app.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.RecommendInput;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.service.PiperRecommendContentService;
import com.syswin.pipeline.service.PiperRecommendPublisherService;
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
@RequestMapping("/recommend")
@Api(value = "recommend", tags = "recommend")
public class RecommendController {

	@Autowired
	PiperRecommendContentService piperRecommendContentService;

	@Autowired
	PiperRecommendPublisherService piperRecommendPublisherService;

	@PostMapping("/publisherList")
	@ApiOperation(
					value = "获取出版社列表"
	)
	public ResponseEntity publisherList(@RequestBody RecommendInput recommendInput) {
		PageInfo<ReCommendContent> pageInfo = piperRecommendContentService.list(null, recommendInput.getPageNo(), recommendInput.getPageSize());
		//TODO 处理京交会
		return new ResponseEntity(pageInfo);
	}

	@PostMapping("/contentList")
	@ApiOperation(
					value = "获取推荐内容列表"
	)
	public ResponseEntity contentList(@RequestBody RecommendInput recommendInput) {

		//TODO 处理京交会
		PageInfo<ReCommendPublisher> pageInfo = piperRecommendPublisherService.list(null,recommendInput.getPageNo(), recommendInput.getPageSize());

		return new ResponseEntity(pageInfo);

	}
}
