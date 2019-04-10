package com.syswin.pipeline.app.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.RecommendInput;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.service.PiperRecommendContentService;
import com.syswin.pipeline.service.PiperRecommendPublisherService;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.ContentOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
	public ResponseEntity publisherList(@RequestBody RecommendInput ri) {
		Integer pageNo = StringUtils.isNullOrEmpty(ri.getPageNo()) ? 1 : Integer.parseInt(ri.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(ri.getPageSize()) ? 20 : Integer.parseInt(ri.getPageSize());


		PageInfo<ReCommendContent> pageInfo = piperRecommendContentService.list(null, pageNo, pageSize);
		//TODO 处理京交会
		return new ResponseEntity(pageInfo);
	}

	@PostMapping("/contentList")
	@ApiOperation(
					value = "获取推荐内容列表"
	)
	public ResponseEntity contentList(@RequestBody RecommendInput ri) {
		Integer pageNo = StringUtils.isNullOrEmpty(ri.getPageNo()) ? 1 : Integer.parseInt(ri.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(ri.getPageSize()) ? 20 : Integer.parseInt(ri.getPageSize());

		PageInfo contentOutPageInfo = piperRecommendContentService.listCIFTISAPI(pageNo, pageSize);
		return new ResponseEntity(contentOutPageInfo);

	}
}
