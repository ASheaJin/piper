package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.PsSubOrgListParam;
import com.syswin.pipeline.app.dto.PublishMessageParam;
import com.syswin.pipeline.app.dto.SubOrgListParam;
import com.syswin.pipeline.psservice.PsServerService;
import com.syswin.pipeline.psservice.response.ResponeResultData;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.org.IOrgService;
import com.syswin.pipeline.service.org.OrgOut;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.PubKey;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.response.SubResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 115477 on 2018/12/18.
 */
@CrossOrigin
@RestController
@RequestMapping("/ps")
@Api(value = "ps", tags = "ps")
public class PSClientController {
	private static final Logger logger = LoggerFactory.getLogger(PSClientController.class);

	@Autowired
	private PSClientService psClientService;

	@Autowired
	SendMessegeService sendMessegeService;

	@Autowired
	private IOrgService orgService;

	@Autowired
	com.syswin.sub.api.SubscriptionService scriptionService;

	@Autowired
	private com.syswin.sub.api.PublisherService subPublisherService;

	@Autowired
	private PsServerService psServerService;

	@PostMapping("/sendMsg")
	@ApiOperation(
					value = "获取消息详情"
	)
	public boolean sendMsg(@RequestBody ChatMsg msg, @RequestParam String senderTemail,
	                       @RequestParam String senderPK, HttpServletRequest request) {

		return psClientService.sendChatMessage(msg, senderTemail, senderPK);
	}


	@PostMapping("/getPubKey")
	@ApiOperation(
					value = "获取pubKey"
	)
	public PubKey getPubKey(@RequestBody String senderTemail, HttpServletRequest request) {
		return new PubKey(psClientService.getTemailPublicKey(senderTemail));
	}


	@PostMapping("/registerTemail")
	@ApiOperation(
					value = "注册Temail, 返回公钥"
	)
	public PubKey registerTemail(@RequestBody String senderTemail, HttpServletRequest request) {
		return new PubKey(psClientService.registerTemail(senderTemail));
	}


	@GetMapping("/getTemailPublicKey")
	@ApiOperation(
					value = "获取公钥"
	)
	public PubKey getTemailPublicKey(String senderTemail) {
		String pub = psClientService.getTemailPublicKey(senderTemail);
		if (StringUtils.isEmpty(pub)) {
			pub = psClientService.registerPub(senderTemail);
		}
		return new PubKey(pub);
	}

	@GetMapping("/createPublicKey")
	@ApiOperation(
					value = "批量生成密机"
	)
	public List createPublicKey() {

		List<String> createUserList = new ArrayList<>();
		String senderTemail = null;
		senderTemail = ("a.piper@support2technical.me").trim();
//			String pub = psClientService.getTemailPublicKey(senderTemail);
		String pk = psClientService.registerPub(senderTemail);
		String sendertt = "INSERT INTO `user_temail` (temail, user_id, ALGORITHM, TYPE, domain, create_time, update_time) VALUES ('%s', '%s', 1, 4, 'support2technical.me', UNIX_TIMESTAMP(NOW())*1000, UNIX_TIMESTAMP(NOW())*1000);";
//			String pub = psClientService.getTemailPublicKey(senderTemail);
		String tt = String.format(sendertt, "a.piper@support2technical.me", pk);

		createUserList.add(tt);
		for (int i = 1; i < 301; i++) {
//			senderTemail="p."+(10000000+i)+"@systoontest.com";
			senderTemail = ("p." + (10000000 + i) + "@support2technical.me").trim();
//			String pub = psClientService.getTemailPublicKey(senderTemail);
			pk = psClientService.registerPub(senderTemail);
			sendertt = "INSERT INTO `user_temail` (temail, user_id, ALGORITHM, TYPE, domain, create_time, update_time) VALUES ('%s', '%s', 1, 4, 'support2technical.me', UNIX_TIMESTAMP(NOW())*1000, UNIX_TIMESTAMP(NOW())*1000);";
//			String pub = psClientService.getTemailPublicKey(senderTemail);
			tt = String.format(sendertt, senderTemail, pk);
			createUserList.add(tt);
		}

		return createUserList;
	}


	@GetMapping("/createTest")
	@ApiOperation(
					value = "批量生成密机测试"
	)
	public List createTest() {

		List<String> createUserList = new ArrayList<>();
		String sendertt = "INSERT INTO `user_temail` (temail, user_id, ALGORITHM, TYPE, domain, create_time, update_time) VALUES ('%s', '%s', 1, 4, \"support2technical.me\", UNIX_TIMESTAMP(NOW())*1000, UNIX_TIMESTAMP(NOW())*1000);";
//			String pub = psClientService.getTemailPublicKey(senderTemail);
		String tt = String.format(sendertt, "a.piper@support2technical.me", "111");
		createUserList.add(tt);
		for (int i = 1; i < 301; i++) {
//			senderTemail="p."+(10000000+i)+"@systoontest.com";
//			String pub = psClientService.getTemailPublicKey(senderTemail);
			tt = String.format(sendertt, ("p." + (10000000 + i) + "@support2technical.me").trim(), "111");
			createUserList.add(tt);
		}

		return createUserList;
	}

	class CreateUser {
		public String mail;
		public String pk;
	}

	@PostMapping("/sendOthermessage")
	@ApiOperation(
					value = "测试接口。发送消息"
	)
	public String sendOthermessage(@RequestBody PublishMessageParam message) {
		String content = "{\"w\":540,\"h\":960,\"isOriginal\":0,\"suffix\":\".png\",\"url\":\"http:\\/\\/temail-test.cn-bj.ufileos.com\\/mediabank%2Fdd8da1b9f51444be842411fd79cbfd8a.zip\",\"size\":28419,\"pwd\":\"7B13B225-10BC-4141-87D9-FD1139FCCF52\"}";
//		String content = "{\"text\":\"vhh\"}";
		Boolean result = sendMessegeService.sendOthermessage(message.getContent(), Integer.parseInt(message.getBodyType()), message.getPtemail(), message.getFromTemail());
		return String.valueOf(result);
	}

	@GetMapping("/setCard")
	@ApiOperation(
					value = "发送名片"
	)
	public void setCard(String temail, String to, String nick) {

		sendMessegeService.sendCard(temail, to, nick);
	}

	@PostMapping("/sendCards")
	@ApiOperation(
					value = "批量发名片"
	)
	public ResponseEntity sendCards(@RequestBody PsSubOrgListParam modify) {
		Publisher publisher = subPublisherService.getPubLisherById(modify.getPublisherId());
		if (publisher == null) {
			return new ResponseEntity("500", "该出版社不存在");
		}
		List<String> userList = scriptionService.getSubscribers(publisher.getPtemail(), null);
		for (String userId : userList) {
			if (com.syswin.pipeline.utils.StringUtils.isNullOrEmpty(psClientService.getTemailPublicKey(userId))) {
				continue;
			}
			//判断是否自己订阅自己
			if (userId.equals(publisher.getUserId())) {
				sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + modify.getName(), modify.getIconUrl());
			} else {
				sendMessegeService.sendCard(publisher.getPtemail(), userId, modify.getName(), modify.getIconUrl());
			}

		}
		return new ResponseEntity();

	}

	@GetMapping("/loginuser")
	@ApiOperation(
					value = "登陆出版社监控"
	)
	public ResponseEntity loginuser(String temail) {
		psClientService.loginTemail(temail);
		return new ResponseEntity();
	}

	@GetMapping("/login")
	@ApiOperation(
					value = "登陆出版社监控"
	)
	public ResponseEntity login(HttpServletRequest request) {
		for (int i = 10; i < 100; i++) {
			psClientService.loginTemail("p.100000" + i + "@systoontest.com");
		}
		return new ResponseEntity();
	}


	@GetMapping("/getOrg")
	@ApiOperation(
					value = "登陆出版社监控"
	)
	public OrgOut getOrg(HttpServletRequest request, String temail) {
		OrgOut orgOut = orgService.getOrgByVersion(temail, 0);
		return orgOut;
	}


	@GetMapping("/registerAccount")
	@ApiOperation(
					value = "注册账号"
	)
	public ResponseEntity registerAccount(String temail) {
		ResponeResultData rt = psServerService.registerAccount(temail);
		return new ResponseEntity(rt);
	}
}
