package com.syswin.pipeline.h5.controller;

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



    @GetMapping("/info")
    public String create(Model model, HttpServletRequest request) {

        return "h5/help";
    }


}
