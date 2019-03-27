package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.*;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.service.security.TokenGenerator;
import com.syswin.pipeline.utils.ExcelUtil;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.response.SubResponseEntity;
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


	@PostMapping("create")
	@ApiOperation(
					value = "创建出版社"
	)
	public ResponseEntity create(@RequestBody CreatePublisherParam createPublisher) {


		SubResponseEntity subResponseEntity = publisherService.addPublisher(createPublisher.getUserId(), createPublisher.getName(), createPublisher.getPtype() == null ? 0 : createPublisher.getPtype());
		if (subResponseEntity.isSuc()) {
			//回执创建完成消息
			return new ResponseEntity();
		}
		return new ResponseEntity("500", subResponseEntity.getMsg());

	}

	@PostMapping("createOrg")
	@ApiOperation(
					value = "创建组织出版社"
	)
	public ResponseEntity createOrg(@RequestBody CreateOrgPublisher createPublisher) {

		SubResponseEntity subResponseEntity = publisherService.addPublisher(createPublisher.getUserId(), createPublisher.getName(), 2);
		if (subResponseEntity.isSuc()) {
			//回执创建完成消息
			return new ResponseEntity();
		}
		return new ResponseEntity("500", subResponseEntity.getMsg());

	}

	@PostMapping("deleteOrg")
	@ApiOperation(
					value = "删除组织出版社"
	)
	public ResponseEntity deleteOrg(@RequestBody DeleteParam unSubParam) {

		SubResponseEntity subResponseEntity = publisherService.deleteOrg(unSubParam.getUserId(), unSubParam.getPublisherId());
		if (subResponseEntity.isSuc()) {
			//回执删除消息
			return new ResponseEntity();
		}
		return new ResponseEntity("500", subResponseEntity.getMsg());

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


		SubResponseEntity subResponseEntity = subscriptionService.subscribe(sub.getUserId(), sub.getPublishTemail());
		if (subResponseEntity.isSuc()) {
			return new ResponseEntity();
		}
		return new ResponseEntity("500", subResponseEntity.getMsg());

	}


	@PostMapping("subscribebyOrgList")
	@ApiOperation(
					value = "批量组织名称订阅组织号"
	)
	public ResponseEntity subscribebyOrgList(@RequestBody SubOrgListParam subList) {
		SubResponseEntity subResponseEntity = subscriptionService.subscribeOrgList(subList.getUserIdList(), subList.getPublisherId(), subList.getPublishTemail());
		if (subResponseEntity.isSuc()) {
			return new ResponseEntity();
		}
		return new ResponseEntity("500", subResponseEntity.getMsg());
	}

	@PostMapping("uploadExcel")
	@ApiOperation(
					value = "Excel批量上传订阅组织出版社"
	)
	public ResponseEntity uploadExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) {
		List<String> listString = ExcelUtil.getExcelData(file);
		if (listString.size() == 0) {
			throw new BusinessException("名单导入为空");
		}
		String[] param = tokenGenerator.getIdsByToken(request.getParameter("token"));
		if (param == null) {
			throw new BusinessException("token已经失效");
		}
		SubResponseEntity subResponseEntity = subscriptionService.subscribeList(listString, param[0], param[1]);

		if (subResponseEntity.isSuc()) {
			return new ResponseEntity();
		}
		return new ResponseEntity("500", subResponseEntity.getMsg());
	}


	@PostMapping("subscribeByList")
	@ApiOperation(
					value = "通过UserId订阅组织号"
	)
	public ResponseEntity subscribeByList(@RequestBody SubUserListParam subUserList) {

		SubResponseEntity subResponseEntity = subscriptionService.subscribeOrgList(subUserList.getTmails(), subUserList.getPublisherId(), subUserList.getUserId());
		if (subResponseEntity.isSuc()) {
			return new ResponseEntity();
		}
		return new ResponseEntity("500", subResponseEntity.getMsg());

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
		SubResponseEntity sub = subscriptionService.unsubscribe(unSub.getUserId(), unSub.getPublisherId());

		return sub.isSuc() ? new ResponseEntity() : new ResponseEntity("500", "取消订阅失败");
	}

	@PostMapping("unOrgsubscribe")
	@ApiOperation(
					value = "取消组织订阅"
	)
	public ResponseEntity unOrgsubscribe(@RequestBody UnSubOrgParam unSubOrg) {
		SubResponseEntity sub = subscriptionService.unsubscribeByOwnerId(unSubOrg.getSubTmail(), unSubOrg.getUserId(), StringUtils.getLong(unSubOrg.getPublisherId()), PublisherTypeEnums.organize);
		return sub.isSuc() ? new ResponseEntity() : new ResponseEntity("500", sub.getMsg());

	}

	@PostMapping("getMyPublisher")
	@ApiOperation(
					value = "获得我的个人出版社"
	)
	public ResponseEntity getMyPublisher(@RequestBody String userId) {
		Publisher publisher = publisherService.getPubLisherByuserId(userId);
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
