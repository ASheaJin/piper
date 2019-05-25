package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.AdminInputParam;
import com.syswin.pipeline.app.dto.MulCreateParam;
import com.syswin.pipeline.app.dto.SearchParam;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.exceptions.SubException;
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
public class PiperAdminController {
	private static final Logger logger = LoggerFactory.getLogger(PiperAdminController.class);
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
	@Autowired
	LanguageChange languageChange;

	@PostMapping("createAdminList")
	@ApiOperation(
					value = "批量创建管理者"
	)
	public ResponseEntity<Admin> createAdminList(@RequestBody MulCreateParam mulCreateParam) {
		if (checkNotAdmin(mulCreateParam.getUserId())) {
			throw new BusinessException("msg.nopermission");
		}
		Admin admin = adminService.getAdmin(mulCreateParam.getUserId(), PublisherTypeEnums.organize);
		if (admin == null || admin.getStatus() == 0) {
			throw new BusinessException("ex.needorganizer");
		}
		List<String> userList = PatternUtils.tranStrstoList(mulCreateParam.getTmails());

		for (String u : userList) {
			if (StringUtils.isNullOrEmpty(psClientService.getTemailPublicKey(u))) {
				throw new BusinessException("msg.noemail");
			}
			adminService.add(mulCreateParam.getUserId(), u, PublisherTypeEnums.organize, false);

			try {

				sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.beoranger", new String[]{u}, u), u);
				sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.beoranger", new String[]{u}, u), mulCreateParam.getUserId());
				//回执创建完成消息
			} catch (Exception e) {
				logger.error("发送消息失败", e);
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
			throw new BusinessException("msg.nopermission");
		}
		Admin admin = adminService.add(adminParam.getUserId(), adminParam.getTmail(), PublisherTypeEnums.organize, true);

		sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.beoranger", new String[]{adminParam.getTmail()}, adminParam.getTmail()), adminParam.getTmail());
		//回执创建完成消息
		return new ResponseEntity();

	}

	@PostMapping("deleteAdmin")
	@ApiOperation(
					value = "删除管理员"
	)
	public ResponseEntity deleteAdmin(@RequestBody AdminInputParam adminParam) {
		if (checkNotAdmin(adminParam.getUserId())) {
			throw new BusinessException("msg.nopermission");
		}
		Admin admin = adminService.getAdmin(adminParam.getUserId(), PublisherTypeEnums.organize);
		if (admin == null || admin.getStatus() == 0) {
			throw new SubException("ex.needorganizer");
		}
		adminService.delete(adminParam.getUserId(), adminParam.getTmail(), PublisherTypeEnums.organize);
		sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.canceladmin", new String[]{adminParam.getUserId()}, adminParam.getTmail()), adminParam.getTmail());
		sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.becanceled", new String[]{adminParam.getTmail()}, adminParam.getUserId()), adminParam.getUserId());

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
		List<Admin> sub = adminService.getAdmins(adminParam.getKeyword(), adminParam.getUserId(), PublisherTypeEnums.organize, pageno, pagesize);

		return new ResponseEntity(sub);
	}

	public boolean checkNotAdmin(String userId) {
		return !("luohongzhou1@syswin.com".equals(userId) || "weihongyi@syswin.com".equals(userId));
	}
}
