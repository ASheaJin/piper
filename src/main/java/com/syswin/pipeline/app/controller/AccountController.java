package com.syswin.pipeline.app.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 秘票 交易 充值 银行卡
 * Created by 115477 on 2018/11/28.
 */
@RestController
@RequestMapping("/account")
@Api(value = "account", tags = "account")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


}
