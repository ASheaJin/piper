package com.syswin.pipeline.h5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理h5页面的router请求路径
 * Created by 115477 on 2019/2/28.
 */
@Controller
public class VueController {

    @RequestMapping(value={
            "/web",
            "/web/home",
            "/web/manage-list"
    })
    public String fowardRouter(){
        return "forward:/index.html";
    }

    @RequestMapping(value={
            "/web/static/**"
    })
    public void fowardStatic(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getRequestURL().toString().replace("/web/", "/"));
    }
}
