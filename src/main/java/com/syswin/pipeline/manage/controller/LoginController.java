package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.db.model.User;
import com.syswin.pipeline.manage.dto.LoginOut;
import com.syswin.pipeline.manage.dto.LoginParam;
import com.syswin.pipeline.manage.service.UserService;
import com.syswin.pipeline.manage.shiro.StatelessToken;
import com.syswin.pipeline.manage.shiro.TokenManager;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2019/3/29.
 */
@RestController
@RequestMapping("/manage")
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseEntity<LoginOut> login(@RequestBody LoginParam loginParam) {

        try {
            Boolean b = userService.login(loginParam);
            StatelessToken createToken = tokenManager.createToken(loginParam.getLoginName());

            return new ResponseEntity<>(new LoginOut(createToken.getLoginName(), createToken.getToken()));
        } catch (AuthenticationException ae) {
            return new ResponseEntity("500", ae.getMessage());
        }
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
