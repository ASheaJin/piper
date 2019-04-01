package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.service.PiperContentService;
import com.syswin.pipeline.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 内容举报操作、删帖
 * Created by 115477 on 2018/11/28.
 */
@Controller
@RequestMapping("/manage/content")
@Api(value = "content", tags = "内容")
public class ContentInnerController {

	@Autowired
	private PiperContentService contentService;

	@PostMapping("/index")
	public String getAllPerson(Model model) {
//		ModelAndView mav = new ModelAndView("/index");
//		List list = new ArrayList();
//		Map map =  new HashMap();
//		PageInfo<Person> pageInfo = new PageInfo<Person>(list);
//		model.addAttribute("pageInfo",pageInfo);
		return "index";
	}


	@PostMapping("/list")
	public String list(HttpServletRequest request, Model model) {
		int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
		int pageSize = StringUtils.getParam(request, "pageSize", 10);
		String publisherId = StringUtils.getParam(request, "publisherId", null);

		model.addAllAttributes(contentService.listByExample(pageIndex, pageSize, publisherId));
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("publisherId", publisherId);
		return "piper/publisher/listContent";
	}
}
