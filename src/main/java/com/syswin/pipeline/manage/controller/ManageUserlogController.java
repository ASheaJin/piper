package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.db.model.Userlog;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 使用日志
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@Controller
@RequestMapping("/manage/userlog")
@Api(value = "userlog", tags = "用户日志")
public class ManageUserlogController {

}
