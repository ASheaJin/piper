package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.manage.dto.LoginParam;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 115477 on 2019/3/29.
 */
@RestController
@RequestMapping("/manage")
@Api(value = "登录")
public class LoginController {

    public ResponseEntity login(@RequestBody LoginParam loginParam) {

        return null;
    }
}
