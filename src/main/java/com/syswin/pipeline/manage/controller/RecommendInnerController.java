package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.RecommendInput;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.manage.vo.input.AddRecommendContent;
import com.syswin.pipeline.manage.vo.input.AddRecommendPublisher;
import com.syswin.pipeline.manage.vo.input.DelReCommend;
import com.syswin.pipeline.service.PiperRecommendContentService;
import com.syswin.pipeline.service.PiperRecommendPublisherService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.HeaderUtil;
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
	PiperRecommendContentService piperRecommendContentService;

	@Autowired
	PiperRecommendPublisherService piperRecommendPublisherService;

	@PostMapping("/publisherList")
	@ApiOperation(
					value = "获取推荐出版社列表"
	)
	public ResponseEntity publisherList(@RequestBody RecommendInput recommendInput, HttpServletRequest request) {
		PageInfo<ReCommendPublisher> pageInfo = piperRecommendPublisherService.list(recommendInput.getPageNo(), recommendInput.getPageSize());

		return new ResponseEntity(pageInfo);
	}

	@PostMapping("/publisherAdd")
	@ApiOperation(
					value = "添加推荐出版社"
	)
	public ResponseEntity publisherAdd(@RequestBody AddRecommendPublisher acp, HttpServletRequest request) {
		ReCommendPublisher reCommendPublisher = piperRecommendPublisherService.add(HeaderUtil.getUserId(request), acp.getPublisherId());

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
		PageInfo<ReCommendContent> pageInfo = piperRecommendContentService.list(recommendInput.getPageNo(), recommendInput.getPageSize());

		return new ResponseEntity(pageInfo);

	}

	@PostMapping("/contentAdd")
	@ApiOperation(
					value = "添加推荐出版社"
	)
	public ResponseEntity contentAdd(@RequestBody AddRecommendContent acc, HttpServletRequest request) {
		ReCommendContent reCommendContent = piperRecommendContentService.add(HeaderUtil.getUserId(request), acc.getContentId());

		return new ResponseEntity(reCommendContent);
	}

	@PostMapping("/contentDelete")
	@ApiOperation(
					value = "删除推荐内容"
	)
	public ResponseEntity contentDelete(@RequestBody DelReCommend dc, HttpServletRequest request) {
		piperRecommendContentService.delete(HeaderUtil.getUserId(request), dc.getId());

		return new ResponseEntity();
	}
}
