package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.TokenOutParam;
import com.syswin.pipeline.db.model.Token;
import com.syswin.pipeline.service.PiperTokenService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@RestController
@RequestMapping("/token")
//@Api(value = "token", tags = "token")
public class PiperTokenController {
    private static final Logger logger = LoggerFactory.getLogger(PiperTokenController.class);

    @Autowired
    private PiperTokenService tokenService;

    @GetMapping("getToken")
    @ApiOperation(
            value = "获取Token"
    )
    public TokenOutParam getToken(@RequestParam String userId, HttpServletRequest request) {
        Token token = tokenService.getToken(userId);
        return new TokenOutParam(token.getToken());
    }
}
