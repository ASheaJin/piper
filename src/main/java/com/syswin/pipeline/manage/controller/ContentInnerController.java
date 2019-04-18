package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.dto.input.ContentDelnput;
import com.syswin.pipeline.manage.dto.input.ContentListInput;
import com.syswin.pipeline.service.PiperContentService;
import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 内容举报操作、删帖
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/content")
@Api(value = "content", tags = "内容")
public class ContentInnerController {

	@Autowired
	private PiperContentService piperContentService;


	@PostMapping("/list")
	public ResponseEntity<PageInfo> list(@RequestBody ContentListInput ci, HttpServletRequest request) {
		Integer pageNo = StringUtils.isNullOrEmpty(ci.getPageNo()) ? 1 : Integer.parseInt(ci.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(ci.getPageSize()) ? 20 : Integer.parseInt(ci.getPageSize());

		PageInfo pageInfo = piperContentService.listManage(ci.getPubliserId(), ci.getHasRecommend(), pageNo, pageSize);
		return new ResponseEntity(pageInfo);
	}

	@PostMapping("/delete")
	public ResponseEntity delete(@RequestBody ContentDelnput ci, HttpServletRequest request) {
		//TODO 是否支持消息测回
		piperContentService.delete(ci.getContentId());
		return new ResponseEntity();
	}

	@PostMapping("/active")
	public ResponseEntity active(@RequestBody ContentDelnput ci, HttpServletRequest request) {
		piperContentService.active(ci.getContentId());
		return new ResponseEntity();
	}
}
