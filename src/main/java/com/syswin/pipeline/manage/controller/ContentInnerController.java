package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.repository.ReCommendContentRepository;
import com.syswin.pipeline.manage.dto.input.ContentDelnput;
import com.syswin.pipeline.manage.dto.input.ContentListInput;
import com.syswin.pipeline.service.PiperContentService;
import com.syswin.pipeline.service.PiperRecommendContentService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.ContentOutService;
import com.syswin.sub.api.utils.PageUtil;
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
	private PiperContentService piperContentService;


	@PostMapping("/list")
	public ResponseEntity<PageInfo> list(@RequestBody ContentListInput ci, HttpServletRequest request) {
		PageInfo pageInfo = piperContentService.listManage(ci.getPubliserId(), ci.getHasRecommend(), Integer.parseInt(ci.getPageNo()), Integer.parseInt(ci.getPageSize()));
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
