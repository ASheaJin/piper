package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.output.ResEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志管理
 */

public class BaseController {

    public String getUserId(HttpServletRequest request) {
        return request.getHeader("userId");
    }

    public ResEntity suc() {
        return suc(null,"success");
    }

    public ResEntity suc(String msg) {
        return suc(null,msg);
    }

    public ResEntity suc(Object object,String msg) {
        ResEntity res= new ResEntity();
        res.setCode(ResEntity.SUCCESS);
        res.setMsg(msg);
        res.setData(object);
        return res;
    }

    public ResEntity fail() {
        return fail(null,"fail");
    }

    public ResEntity fail(String msg) {
        return fail(null,msg);
    }


    public ResEntity fail(Object object,String msg) {
        ResEntity res= new ResEntity();
        res.setCode(ResEntity.Fail);
        res.setMsg(msg);
        res.setData(object);
        return res;
    }
}
