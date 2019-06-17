package com.syswin.pipeline.h5.controller;

import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2019/1/11.
 */
@Controller
@RequestMapping("/h5/subcribe")
public class SubcribeH5Controller {

	@Autowired
	private PiperSubscriptionService subscriptionService;

	@RequestMapping("/add")
	public String add(Model model, HttpServletRequest request) {
		String userId = StringUtils.getParam(request, "userId", null);
		model.addAttribute("userId", userId);
		return "h5/addSubcribe";
	}

	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request) {
		int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
		int pageSize = StringUtils.getParam(request, "pageSize", 10);
		String userId = StringUtils.getParam(request, "userId", null);

		model.addAttribute("data", subscriptionService.getPersonSubscribtions(userId, pageIndex, pageSize));
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("userId", userId);
		return "h5/listSubcribe";
	}
}
