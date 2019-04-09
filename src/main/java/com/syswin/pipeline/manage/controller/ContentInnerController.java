package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.dto.input.ContentDelnput;
import com.syswin.pipeline.manage.dto.input.ContentListInput;
import com.syswin.pipeline.service.PiperContentService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 内容举报操作、删帖
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@Controller
@RequestMapping("/manage/content")
@Api(value = "content", tags = "内容")
public class ContentInnerController {

	@Autowired
	private PiperContentService contentService;


	@PostMapping("/list")
	public ResponseEntity<PageInfo> list(@RequestBody ContentListInput ci, HttpServletRequest request) {
		Integer pageNo = StringUtils.isNullOrEmpty(ci.getPageNo()) ? 1 : Integer.parseInt(ci.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(ci.getPageSize()) ? 20 : Integer.parseInt(ci.getPageSize());

		PageInfo pageInfo = contentService.list(ci.getPubliserId(), ci.getKeyword(), pageNo, pageSize);
		return new ResponseEntity();
	}

	@PostMapping("/delete")
	public ResponseEntity delete(@RequestBody ContentDelnput ci, HttpServletRequest request) {
		contentService.delete(ci.getContentId());
		return new ResponseEntity();
	}

	@PostMapping("/active")
	public ResponseEntity active(@RequestBody ContentDelnput ci, HttpServletRequest request) {
		contentService.active(ci.getContentId());
		return new ResponseEntity();
	}
}
