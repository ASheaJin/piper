package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.*;
import com.syswin.pipeline.psservice.APPPublisherService;
import com.syswin.pipeline.psservice.PsServerService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.org.IOrgService;
import com.syswin.pipeline.service.org.OrgOut;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.PubKey;
import com.syswin.pipeline.utils.SwithUtil;
import com.syswin.ps.sdk.admin.constant.AdminException;
import com.syswin.ps.sdk.admin.constant.FastJsonUtil;
import com.syswin.ps.sdk.admin.constant.ResponseResult;
import com.syswin.ps.sdk.admin.controller.in.AccountIn;
import com.syswin.ps.sdk.admin.platform.entity.AccountInfo;
import com.syswin.ps.sdk.admin.valid.ParamValid;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.service.PsClientService;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.sub.api.db.model.Publisher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by 115477 on 2018/12/18.
 */
@CrossOrigin
@RestController
@RequestMapping("/pssdk")
@Api(value = "pssdk", tags = "pssdk")
public class PSSDKController {
	private static final Logger logger = LoggerFactory.getLogger(PSSDKController.class);

	@Autowired
	APPPublisherService appPublisherService;

	@Autowired
	PsClientService psClientService;

	@PostMapping({"/add"})
	@ApiOperation(
					value = "添加账户"
	)
	public boolean add(@RequestBody @ParamValid AccountIn accountIn) {
		return appPublisherService.addAccount(accountIn);
	}

	@PostMapping("/sendMsg")
	@ApiOperation(
					value = "发送复杂消息体"
	)
	public String sendMsg(@RequestParam String from,
	                      @RequestParam String to) {
//		MsgHeader msgHeader= PsClientKeeper.msgHeader();
		Map<String, Object> map = new HashMap<>();
		map.put("title", "文章标题");
		map.put("imageUrl", "https://www.baidu.com/img/bd_logo1.png");
//		map.put("text", "hello");

		List<ActionItem> infoList = Stream.of(new ActionItem("简要描述", "http://www.baidu.com")
		).collect(Collectors.toList());

		TextShow show = new TextShow(1, map, infoList);
		PsClientKeeper.newInstance().sendMsg(from, to, show);
		return "success";
	}

	@PostMapping({"/login"})
	@ApiOperation(
					value = "登录"
	)
	public void login(String userId) {
		psClientService.login(m -> {
			logger.info("login: " + userId + m.toString());
		}, userId);
	}


}
