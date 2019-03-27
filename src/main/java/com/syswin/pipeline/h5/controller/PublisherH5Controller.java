package com.syswin.pipeline.h5.controller;

import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2019/1/11.
 */
@Controller
@RequestMapping("/h5/publisher")
public class PublisherH5Controller {

	@Value("${url.piper}")
	private String URL_PIPER;

	@Autowired
	private com.syswin.sub.api.PublisherService subPublisherService;

	@RequestMapping("/create")
	public String create(Model model, HttpServletRequest request) {
		String userId = StringUtils.getParam(request, "userId", null);
		Publisher publisher = subPublisherService.getPubLisherByuserId(userId, PublisherTypeEnums.person);

		model.addAttribute("data", publisher);
		model.addAttribute("userId", userId);
		return "h5/createPublisher";
	}

	@RequestMapping("/manage")
	public String manage(Model model, HttpServletRequest request) {
		String userId = StringUtils.getParam(request, "userId", null);
		//判断是否有出版社
		Publisher publisher = subPublisherService.getPubLisherByuserId(userId,PublisherTypeEnums.person);
//	    if(publisher !=null){
		model.addAttribute("data", publisher);
		model.addAttribute("userId", userId);
		return "h5/managePublisher";
//	    }
//	    model.addAttribute("userId", userId);
//	    return "h5/createPublisher";
	}



	@GetMapping("/upload")
	public String upload(Model model, HttpServletRequest request) {
		String token = StringUtils.getParam(request, "token", null);

		model.addAttribute("token", token);
		model.addAttribute("uploadurl", URL_PIPER+"/publish/uploadExcel");
		model.addAttribute("downUrl", URL_PIPER+"/web/static/file/upload.xlsx");
		return "h5/uploadpage";
	}
}
