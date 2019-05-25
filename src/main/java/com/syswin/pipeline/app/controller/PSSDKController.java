package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.psservice.APPPublisherService;
import com.syswin.pipeline.psservice.MessegerSenderService;
import com.syswin.ps.sdk.admin.controller.in.AccountIn;
import com.syswin.ps.sdk.admin.service.impl.PSAccountService;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.sender.AbstractMsgSender;
import com.syswin.ps.sdk.showType.TextShow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
	PSAccountService psAccountService;

	@Autowired
	MessegerSenderService messegerSenderService;

	@PostMapping({"/add"})
	@ApiOperation(
					value = "添加账户"
	)
	public boolean add(@RequestBody AccountIn accountIn) {
		logger.info("accountIn" + accountIn);
		return appPublisherService.addPiperAcount(accountIn.getAccountNo());
	}

	@PostMapping("/sendMsg")
	@ApiOperation(
					value = "发送复杂消息体"
	)
	public String sendMsg(@RequestParam String from,
	                      @RequestParam String to) {
//		MsgHeader msgHeader= PsClientKeeper.msgHeader();
		String title = "《有效提升你的谈判能力》";
		String url = null;
		String infoTitle = "价格类谈判：怎样谈出好价格";
		String infoUrl = "http://t.cn/E9BjssG";
		messegerSenderService.sendSpiderMsg(from, to, title, url, infoTitle, infoUrl);
		return "success";
	}

	@PostMapping({"/login"})
	@ApiOperation(
					value = "登录"
	)
	public void login(String userId) {
		psAccountService.login(userId);
	}


	@PostMapping({"/sendText"})
	@ApiOperation(
					value = "发送文本"
	)
	public void sendText(String from, String to, String txt) throws IOException {

		messegerSenderService.sendText(from, to, txt);
	}

	@PostMapping({"/sendImage"})
	@ApiOperation(
					value = "发送图片"
	)
	public void sendImage(String from, String to, String url, String fileName) throws IOException {

		messegerSenderService.sendImage(from, to, url, fileName);
	}


}
