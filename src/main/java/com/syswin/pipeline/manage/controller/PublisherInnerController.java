package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2019/1/8.
 */
@Controller
@RequestMapping("/manage/publisher")
//@Api(value = "publisher", tags = "publisher")
public class PublisherInnerController {

    @Autowired
    private PiperPublisherService publisherService;
    @Autowired
    private PiperSubscriptionService subscriptionService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
        int pageSize = StringUtils.getParam(request, "pageSize", 10);
        String name = StringUtils.getParam(request, "name", null);
        String userId = StringUtils.getParam(request, "userId", null);
        String ptmail = StringUtils.getParam(request, "ptmail", null);

        model.addAllAttributes(publisherService.list(pageIndex, pageSize, name, userId, ptmail));
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("name", name);
        model.addAttribute("userId", userId);
        model.addAttribute("ptmail", ptmail);
        return "piper/publisher/list";
    }

    @RequestMapping("/listSub")
    public String listSub(HttpServletRequest request, Model model) {
        int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
        int pageSize = StringUtils.getParam(request, "pageSize", 10);
        String publisherId = StringUtils.getParam(request, "publisherId", null);

        model.addAllAttributes(subscriptionService.listByExample(pageIndex, pageSize, publisherId));
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("publisherId", publisherId);
        return "piper/publisher/listSub";
    }

}
