package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.AdminInputParam;
import com.syswin.pipeline.app.dto.MulCreateParam;
import com.syswin.pipeline.app.dto.SearchParam;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.exceptions.SubException;
import com.syswin.sub.api.response.SubResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 出版社的创建 查看 订阅 搜索
 * Created by 115477 on 2018/11/27.
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
@Api(value = "admin", tags = "admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	AdminService adminService;
	@Autowired
	PiperSubscriptionService subscriptionService;

	@Autowired
	PSClientService psClientService;

	@Autowired
	PublisherService publisherService;
	@Autowired
	SendMessegeService sendMessegeService;

	@PostMapping("createAdminList")
	@ApiOperation(
					value = "批量创建管理者"
	)
	public ResponseEntity<Admin> createAdminList(@RequestBody MulCreateParam mulCreateParam) {
		if (checkNotAdmin(mulCreateParam.getUserId())) {
			return new ResponseEntity("500", "你无权操作");
		}
		Admin admin = adminService.getAdmin(mulCreateParam.getUserId());
		if (admin == null || admin.getStatus() == 0) {
			return new ResponseEntity(" 你不是组织管理员，不能创建");
		}
		List<String> userList = PatternUtils.tranStrstoList(mulCreateParam.getTmails());

		for (String u : userList) {
			if (StringUtils.isNullOrEmpty(psClientService.getTemailPublicKey(u))) {
				return new ResponseEntity("500", u + "邮箱不存在");
			}
			adminService.add(mulCreateParam.getUserId(), u, false);

			List<Publisher> publisherList = publisherService.getPubLisherByType(PublisherTypeEnums.organize);
			try {
//					for (Publisher publisher : publisherList) {
//						//创建成功了，给用户推名片
//						sendMessegeService.sendCard(publisher.getPtemail(), u, "* " + publisher.getName());
//						sendMessegeService.sendTextmessage("恭喜成为组织管理员", u, 0, publisher.getPtemail());
//					}
				sendMessegeService.sendTextmessage(u + "成为组织管理员", u);
				sendMessegeService.sendTextmessage(u + "成为组织管理员", mulCreateParam.getUserId());
				//回执创建完成消息
			} catch (Exception e) {
				logger.error("发送消息失败");
			}
		}
		return new ResponseEntity(userList);

	}

	@PostMapping("createFirstAdmin")
	@ApiOperation(
					value = "首次创建管理员,提供给dm使用"
	)
	public ResponseEntity createFirstAdmin(@RequestBody AdminInputParam adminParam) {
		if (checkNotAdmin(adminParam.getUserId())) {
			return new ResponseEntity("500", "你无权操作");
		}
		Admin admin = adminService.add(adminParam.getUserId(), adminParam.getTmail(), true);

		List<Publisher> publisherList = publisherService.getPubLisherByType(PublisherTypeEnums.organize);
		for (Publisher publisher : publisherList) {
			//创建成功了，给用户推名片
			sendMessegeService.sendCard(publisher.getPtemail(), adminParam.getTmail(), "* " + publisher.getName());
			sendMessegeService.sendTextmessage("成为组织管理员", adminParam.getTmail(), 0, publisher.getPtemail());
		}
		sendMessegeService.sendTextmessage(adminParam.getTmail() + "成为组织管理员", adminParam.getUserId());
		//回执创建完成消息
		return new ResponseEntity();

	}

	@PostMapping("deleteAdmin")
	@ApiOperation(
					value = "删除管理员"
	)
	public ResponseEntity deleteAdmin(@RequestBody AdminInputParam adminParam) {
		if (checkNotAdmin(adminParam.getUserId())) {
			return new ResponseEntity("500", "你无权操作");
		}
		Admin admin = adminService.getAdmin(adminParam.getUserId());
		if (admin == null || admin.getStatus() == 0) {
			throw new SubException("删除失败，你不是管理员");
		}
		adminService.delete(adminParam.getUserId(), adminParam.getTmail());
		sendMessegeService.sendTextmessage("你被" + adminParam.getUserId() + "取消了组织管理员", adminParam.getTmail());
		sendMessegeService.sendTextmessage(adminParam.getTmail() + "被取消了组织管理员", adminParam.getUserId());

		return new ResponseEntity();

	}

	@PostMapping("getAdminList")
	@ApiOperation(
					value = "获取所有管理员名单"
	)
	public ResponseEntity getAdminList(@RequestBody SearchParam adminParam) {
		if (checkNotAdmin(adminParam.getUserId())) {
			return new ResponseEntity(new ArrayList<>());
		}
		int pageno = StringUtils.getInteger(adminParam.getPageNo()) == 0 ? 1 : StringUtils.getInteger(adminParam.getPageNo());
		int pagesize = StringUtils.getInteger(adminParam.getPageSize()) == 0 ? 20 : StringUtils.getInteger(adminParam.getPageSize());
		List<Admin> sub = adminService.getAdmins(adminParam.getKeyword(), adminParam.getUserId(), pageno, pagesize);

		return new ResponseEntity(sub);
	}

	public boolean checkNotAdmin(String userId) {
		return !("luohongzhou1@syswin.com".equals(userId) || "weihongyi@syswin.com".equals(userId));
	}
}
