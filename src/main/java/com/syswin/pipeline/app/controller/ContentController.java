package com.syswin.pipeline.app.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.ContentIdInput;
import com.syswin.pipeline.app.dto.ContentListParam;
import com.syswin.pipeline.app.dto.SubUserListParam;
import com.syswin.pipeline.service.content.ContentHandleJobManager;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.DateUtil;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.sub.api.ContentOutService;
import com.syswin.sub.api.ContentService;
import com.syswin.sub.api.db.model.ContentOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 发布内容、查看、评价、搜索
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@RestController
@RequestMapping("/content")
@Api(value = "content", tags = "content")
public class ContentController {

	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentOutService contentOutService;

	@PostMapping("/list")
	@ApiOperation(
					value = "出版社的历史内容列表"
	)
	public ResponseEntity<PageInfo<ContentEntity>> list(@RequestBody ContentListParam input) {
		int pageNo = !StringUtils.isEmpty(input.getPageNo()) ? Integer.parseInt(input.getPageNo()) : 1;
		int pageSize = !StringUtils.isEmpty(input.getPageSize()) ? Integer.parseInt(input.getPageSize()) : 30;

		PageInfo contentOutPageInfo = contentOutService.listByPublisherId(input.getPublisherId(), pageNo, pageSize);
		List<ContentOut> contents = contentOutPageInfo.getList();
		List<ContentEntity> contentEntities = contents.stream().map((out) -> {
			ContentEntity entity = JacksonJsonUtil.fromJson(out.getListdesc(), ContentEntity.class);
			return entity;
		}).collect(Collectors.toList());
		contentOutPageInfo.setList(contentEntities);

		return new ResponseEntity(contentOutPageInfo);

	}
	@GetMapping("/detail")
	@ApiOperation(
			value = "内容详情"
	)
	public ResponseEntity<ContentEntity> detail(@ModelAttribute ContentIdInput input) {
		String contentId = input.getContentId();
		ContentOut contentOut = contentOutService.getContentOutById(Long.parseLong(contentId));
		ContentEntity contentEntity = JacksonJsonUtil.fromJson(contentOut.getAllcontent(), ContentEntity.class);

		return new ResponseEntity(contentEntity);
	}


	/**
	 * 按contentId重新解析内容
	 * @param input
	 * @return
	 */
	@PostMapping("/p")
	public ResponseEntity parseContent(@RequestBody ContentIdInput input) {

//		contentService.activeContent()
		return new ResponseEntity();
	}

	/**
	 * 重新解析所有内容
	 * @return
	 */
	@PostMapping("/pa")
	public ResponseEntity parseAllContent() {
//		contentService.activeContent()
		return new ResponseEntity();
	}
}
