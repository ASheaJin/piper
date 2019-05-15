package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.manage.dto.output.LoginOutput;
import com.syswin.pipeline.manage.dto.input.LoginInput;
import com.syswin.pipeline.manage.service.UserService;
import com.syswin.pipeline.manage.shiro.StatelessToken;
import com.syswin.pipeline.manage.shiro.TokenManager;
import com.syswin.pipeline.app.dto.ResponseEntity;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2019/3/29.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage")
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseEntity<LoginOutput> login(@RequestBody LoginInput loginParam) {

        try {
            LoginOutput l = userService.login(loginParam);
            if (l.getSuccess()) {
                StatelessToken createToken = tokenManager.createToken(loginParam.getLoginName());
                l.setToken(createToken.getToken());
                return new ResponseEntity<>(l);
            }
        } catch (AuthenticationException ae) {
            return new ResponseEntity("500", ae.getMessage());
        }
        return new ResponseEntity("500", "登录失败：" + loginParam.getLoginName());
    }


    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String authorization = request.getHeader("authorization");
        StatelessToken token = tokenManager.getToken(authorization);
        if(token!= null){
            tokenManager.deleteToken(token.getLoginName());
        }
        SecurityUtils.getSubject().logout();
        return new ResponseEntity();
    }
}
