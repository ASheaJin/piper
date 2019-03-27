package com.syswin.pipeline.h5.controller;

import com.syswin.pipeline.app.dto.UnSubOrgParam;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.security.TokenGenerator;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2019/1/14.
 */
@Controller
@RequestMapping("/h5/help")
//@Api(value = "account", tags = "account")
public class HelpH5Controller {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    PiperPublisherService publisherService;

    @Value("${url.piper}")
    private String URL_PIPER;

    private static final String H5_UPLOAD = "/h5/publisher/upload";

    @GetMapping("/upload")
    public String upload(Model model, HttpServletRequest request) {
        String publisherId = StringUtils.getParam(request, "publisherId", null);
        String userId = StringUtils.getParam(request, "userId", null);
        String token = tokenGenerator.generator(publisherId, userId);
        token = token != null ? token : "error";
        String url = URL_PIPER + H5_UPLOAD + "?t=" + token;

        Publisher publisher = publisherService.getPubLisherById(publisherId);
        String ptemail = publisher.getPtemail();

        model.addAttribute("ptemail", ptemail);
        model.addAttribute("pname", publisher.getName());
        model.addAttribute("url", url);
        return "h5/helpupload";
    }

    @GetMapping("/info")
    public String create(Model model, HttpServletRequest request) {

        return "h5/help";
    }


}
