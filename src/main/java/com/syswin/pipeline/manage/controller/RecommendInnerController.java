package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.RecommendInput;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.manage.service.HeaderService;
import com.syswin.pipeline.manage.dto.input.AddRecommendContent;
import com.syswin.pipeline.manage.dto.input.AddRecommendPublisher;
import com.syswin.pipeline.manage.dto.input.DelReCommend;
import com.syswin.pipeline.service.PiperRecommendContentService;
import com.syswin.pipeline.service.PiperRecommendPublisherService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
	private HeaderService headerService;
	@Autowired
	PiperRecommendContentService piperRecommendContentService;

	@Autowired
	PiperRecommendPublisherService piperRecommendPublisherService;

	@PostMapping("/publisherList")
	@ApiOperation(
					value = "获取推荐出版社列表"
	)
	public ResponseEntity publisherList(@RequestBody RecommendInput recommendInput, HttpServletRequest request) {

		String manageId = headerService.getUserId(request);
		PageInfo<ReCommendPublisher> pageInfo = piperRecommendPublisherService.list(manageId, recommendInput.getPageNo(), recommendInput.getPageSize());

		return new ResponseEntity(pageInfo);
	}

	@PostMapping("/publisherAdd")
	@ApiOperation(
					value = "添加推荐出版社"
	)
	public ResponseEntity publisherAdd(@RequestBody AddRecommendPublisher acp, HttpServletRequest request) {
		ReCommendPublisher reCommendPublisher = piperRecommendPublisherService.add(null, acp.getPublisherId());

		return new ResponseEntity(reCommendPublisher);
	}

	@PostMapping("/publisherDelete")
	@ApiOperation(
					value = "删除推荐出版社"
	)
	public ResponseEntity publisherDelete(@RequestBody DelReCommend dc, HttpServletRequest request) {
		piperRecommendPublisherService.delete(dc.getId());

		return new ResponseEntity();
	}

	@PostMapping("/contentList")
	@ApiOperation(
					value = "获取推荐内容列表"
	)
	public ResponseEntity contentList(@RequestBody RecommendInput recommendInput, HttpServletRequest request) {
		String manageId = headerService.getUserId(request);
		PageInfo<ReCommendContent> pageInfo = piperRecommendContentService.list(manageId, recommendInput.getPageNo(), recommendInput.getPageSize());

		return new ResponseEntity(pageInfo);

	}

	@PostMapping("/contentAdd")
	@ApiOperation(
					value = "添加推荐内容"
	)
	public ResponseEntity contentAdd(@RequestBody AddRecommendContent acc, HttpServletRequest request) {
		String manageId = headerService.getUserId(request);
		ReCommendContent reCommendContent = piperRecommendContentService.add(manageId, acc.getContentId());

		return new ResponseEntity(reCommendContent);
	}

	@PostMapping("/contentDelete")
	@ApiOperation(
					value = "删除推荐内容"
	)
	public ResponseEntity contentDelete(@RequestBody DelReCommend dc, HttpServletRequest request) {
		String manageId = headerService.getUserId(request);
		piperRecommendContentService.delete(manageId, dc.getId());

		return new ResponseEntity();
	}
}
