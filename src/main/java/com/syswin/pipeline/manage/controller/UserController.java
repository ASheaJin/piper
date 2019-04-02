package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.manage.dto.PasswordInput;
import com.syswin.pipeline.manage.service.UserService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 115477 on 2019/3/29.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/user")
@Api(tags = "用户")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("list")
    public ResponseEntity list() {
        return null;
    }

    @PostMapping("updatePassword")
    public ResponseEntity updatePassword(@RequestBody PasswordInput passwordParam) {
        userService.updatePassword(passwordParam);
        return new ResponseEntity();
    }

    @PostMapping("resetPassword")
    public ResponseEntity resetPassword(@RequestBody PasswordInput passwordParam) {
        userService.resetPassword(passwordParam.getUserId());
        return new ResponseEntity();
    }
}
