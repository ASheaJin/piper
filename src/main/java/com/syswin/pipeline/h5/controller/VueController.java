package com.syswin.pipeline.h5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理h5页面的router请求路径
 * Created by 115477 on 2019/2/28.
 */
@Controller
public class VueController {

    @RequestMapping(value = {
            "/web/**"
    })
    public String web(HttpServletRequest req, HttpServletResponse resp)throws IOException{
        //如果匹配到了静态文件
        if (!parnStatic(req.getRequestURL().toString())) {
            resp.sendRedirect(req.getRequestURL().toString().replace("/web/", "/"));
            return null;
        }

        return "forward:/web.html";
    }


    @RequestMapping(value = {
            "/webmg/**"
    })
    public String webmg(HttpServletRequest req, HttpServletResponse resp)throws IOException{
        //如果匹配到了静态文件
        if (!parnStatic(req.getRequestURL().toString())) {
            resp.sendRedirect(req.getRequestURL().toString().replace("/webmg/", "/"));
            return null;
        }

        return "forward:/webmg.html";
    }
//    @RequestMapping(value={
//            "/web/static/**"
//    })
//    public void fowardStatic(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.sendRedirect(req.getRequestURL().toString().replace("/web/", "/"));
//    }

    public boolean parnStatic(String url) {
        String str = "https://application.t.email/bbs/web/home?userId=luohongzhou1@syswin.com";
        String pattern = "^((?!/static/).)*$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        return m.matches();
    }
}
