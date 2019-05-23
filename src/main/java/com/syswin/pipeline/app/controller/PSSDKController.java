package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.*;
import com.syswin.pipeline.psservice.PsServerService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.org.IOrgService;
import com.syswin.pipeline.service.org.OrgOut;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.PubKey;
import com.syswin.pipeline.utils.SwithUtil;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
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


	@PostMapping("/sendMsg")
	@ApiOperation(
					value = "获取消息详情"
	)
	public String sendMsg( @RequestParam String from,
	                       @RequestParam String to) {
//		MsgHeader msgHeader= PsClientKeeper.msgHeader();
		Map<String, Object> map = new HashMap<>();
		map.put("title", "qiding");
		map.put("imageUrl", "https://www.baidu.com/img/bd_logo1.png");
		map.put("text", "hello");

		List<ActionItem> infoList = Stream.of(new ActionItem("前进", "http://www.baidu.com")
						, new ActionItem("后退", "http://www.google.com")).collect(Collectors.toList());

		TextShow show = new TextShow(1, map, infoList);
		PsClientKeeper.newInstance().sendMsg(from, to, show);
		return "success";
	}


}
