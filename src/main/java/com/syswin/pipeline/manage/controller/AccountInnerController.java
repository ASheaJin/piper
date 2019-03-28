package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.db.model.Account;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 发券
 * Created by 115477 on 2018/11/28.
 */
@Controller
@RequestMapping("/manage/account")
@Api(value = "account", tags = "account")
public class AccountInnerController extends BaseAction<Account> {

}
