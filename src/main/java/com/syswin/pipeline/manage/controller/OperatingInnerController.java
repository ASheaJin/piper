package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.db.model.Userlog;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 操作记录
 * Created by 115477 on 2018/11/28.
 */
@Controller
@RequestMapping("/manage/userlog")
@Api(value = "operating", tags = "operating")
public class OperatingInnerController extends BaseAction<Userlog> {

}
