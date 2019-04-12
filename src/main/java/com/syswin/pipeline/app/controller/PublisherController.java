package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.*;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.service.security.TokenGenerator;
import com.syswin.pipeline.utils.ExcelUtil;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 出版社的创建 查看 订阅 搜索
 * Created by 115477 on 2018/11/27.
 */
@CrossOrigin
@RestController
@RequestMapping("/publish")
@Api(value = "publish", tags = "publish")
public class PublisherController {

	@Autowired
	private TokenGenerator tokenGenerator;
	@Autowired
	PiperPublisherService publisherService;
	@Autowired
	PiperSubscriptionService subscriptionService;

	@Autowired
	LanguageChange languageChange;

	@PostMapping("create")
	@ApiOperation(
					value = "创建出版社"
	)
	public ResponseEntity create(@RequestBody CreatePublisherParam createPublisher) {


		Publisher publisher = publisherService.addPublisher(createPublisher.getUserId(), createPublisher.getName(), null, createPublisher.getPtype() == null ? 0 : createPublisher.getPtype());
		//回执创建完成消息
		return new ResponseEntity(publisher);

	}

	@PostMapping("createOrg")
	@ApiOperation(
					value = "创建组织出版社"
	)
	public ResponseEntity createOrg(@RequestBody CreateOrgPublisher createPublisher) {

		Publisher publisher = publisherService.addPublisher(createPublisher.getUserId(), createPublisher.getName(), null, 2);
		//回执创建完成消息
		return new ResponseEntity(publisher);

	}

	@PostMapping("deleteOrg")
	@ApiOperation(
					value = "删除组织出版社"
	)
	public ResponseEntity deleteOrg(@RequestBody DeleteParam unSubParam) {

		publisherService.delete(unSubParam.getPublisherId());

		//回执删除消息
		return new ResponseEntity();


	}

	//订阅出版社
	//1、获取该用户与该出版社的关系（已订阅、未订阅）
	//2、处理订阅出版社的指令
	//3、返回处理结果
	@PostMapping("subscribe")
	@ApiOperation(
					value = "订阅提交"
	)
	public ResponseEntity subscribe(@RequestBody SubParam sub) {

		subscriptionService.subscribeNOOrg(sub.getUserId(), sub.getPublishTemail());
		return new ResponseEntity();
	}


	@PostMapping("subscribebyOrgList")
	@ApiOperation(
					value = "批量组织名称订阅组织号"
	)
	public ResponseEntity subscribebyOrgList(@RequestBody SubOrgListParam subList) {
		List<String> sendList = subscriptionService.subscribeOrgList(subList.getUserIdList(), subList.getPublisherId(), subList.getPublishTemail());
		return new ResponseEntity(sendList);

	}

	@PostMapping("uploadExcel")
	@ApiOperation(
					value = "Excel批量上传订阅组织出版社"
	)
	public ResponseEntity uploadExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) {

		String[] param = tokenGenerator.getIdsByToken(request.getParameter("t"));
		if (param == null) {
			throw new BusinessException("ex.tokenInvalid");
		}
		List<String> listString = ExcelUtil.getExcelData(file);
		String error = "";
		for (String tmail : listString) {
			if (!PatternUtils.orEmail(tmail)) {
				error = tmail + " , " + error;
			}
		}
		if (!StringUtils.isNullOrEmpty(error)) {
			return new ResponseEntity("500", languageChange.getLangByStr("ex.email.invalid", request.getHeader("lang")));
		}
		if (listString.size() == 0) {
			throw new BusinessException("ex.userid.null");
		}
		List<String> sendList = subscriptionService.subscribeList(listString, param[0], param[1]);

		return new ResponseEntity(sendList);
	}


	@PostMapping("subscribeByList")
	@ApiOperation(
					value = "通过UserId订阅组织号"
	)
	public ResponseEntity subscribeByList(@RequestBody SubUserListParam subUserList) {

		List<String> sendList = subscriptionService.subscribeOrgList(subUserList.getTmails(), subUserList.getPublisherId(), subUserList.getUserId());
		return new ResponseEntity(sendList);

	}

	//取消订阅出版社
	//1、获取该用户与该出版社的关系（已订阅、未订阅）
	//2、处理取消订阅出版社的指令
	//3、返回处理结果
	@PostMapping("unsubscribe")
	@ApiOperation(
					value = "取消订阅"
	)
	public ResponseEntity unsubscribe(@RequestBody DeleteParam unSub) {
		subscriptionService.unsubscribe(unSub.getUserId(), unSub.getPublisherId());

		return new ResponseEntity();
	}

	@PostMapping("unOrgsubscribe")
	@ApiOperation(
					value = "取消组织订阅"
	)
	public ResponseEntity unOrgsubscribe(@RequestBody UnSubOrgParam unSubOrg) {
		subscriptionService.unsubscribeByOwnerId(unSubOrg.getSubTmail(), unSubOrg.getUserId(), unSubOrg.getPublisherId(), PublisherTypeEnums.organize);
		return new ResponseEntity();

	}

	@PostMapping("getMyPublisher")
	@ApiOperation(
					value = "获得我的个人出版社"
	)
	public ResponseEntity getMyPublisher(@RequestBody UserIdParam userIdParam) {
		Publisher publisher = publisherService.getPubLisherByuserId(userIdParam.getUserId());
		return new ResponseEntity(publisher);
	}

	@PostMapping("getMyOrgPublisher")
	@ApiOperation(
					value = "获得我的出版社"
	)
	public ResponseEntity getMyOrgPublisher(@RequestBody SearchParam searchParam) {
		int pageno = StringUtils.getInteger(searchParam.getPageNo()) == 0 ? 1 : StringUtils.getInteger(searchParam.getPageNo());
		int pagesize = StringUtils.getInteger(searchParam.getPageSize()) == 0 ? 20 : StringUtils.getInteger(searchParam.getPageSize());

		List<Publisher> publisherList = publisherService.getMyOrgPublisherList(searchParam.getKeyword(), searchParam.getUserId(), pageno, pagesize);
		return new ResponseEntity(publisherList);
	}
}
