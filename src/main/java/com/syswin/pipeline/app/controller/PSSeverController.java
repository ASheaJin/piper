package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.psserver.SendMsgService;
import com.syswin.pipeline.service.psserver.bean.SendMsgEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 115477 on 2018/12/18.
 */
@CrossOrigin
@RestController
@RequestMapping("/psserver")
@Api(value = "psserver", tags = "psserver")
public class PSSeverController {
	private static final Logger logger = LoggerFactory.getLogger(PSSeverController.class);

	@Autowired
	SendMsgService pendMsgServer;


	@GetMapping("send")
	@ApiOperation(
					value = "發送消息"
	)
	public String send() {
		ChatMsg chatMsg = new ChatMsg("1112112",1, "a.piper@t.email", "luohongzhou@t.email");
		SendMsgEntity sendMsgEntity = new SendMsgEntity();
		sendMsgEntity.setExtrData(null);
		sendMsgEntity.setMsg(chatMsg);
		sendMsgEntity.setReceiver("luohongzhou@t.email");
		sendMsgEntity.setReceiverPK("MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc");
		sendMsgEntity.setSender("a.piper@t.email");
		sendMsgEntity.setSenderPK("MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc");
		pendMsgServer.sendChatTestMessage(sendMsgEntity,(short)1);
		logger.info("send Message");
		return"success";
	}

}
