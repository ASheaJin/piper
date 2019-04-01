package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.manage.dto.PasswordParam;
import com.syswin.pipeline.manage.service.UserService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 115477 on 2019/3/29.
 */
@RestController
@RequestMapping("/manage/user")
@Api(tags = "用户")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("updatePassword")
    public ResponseEntity updatePassword(@RequestBody PasswordParam passwordParam) {
        userService.updatePassword(passwordParam);
        return new ResponseEntity();
    }

    @PostMapping("resetPassword")
    public ResponseEntity resetPassword(@RequestBody PasswordParam passwordParam) {
        userService.resetPassword(passwordParam.getUserId());
        return new ResponseEntity();
    }
}
