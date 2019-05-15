package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.service.HeaderService;
import com.syswin.pipeline.manage.dto.input.DelPublisherParam;
import com.syswin.pipeline.manage.dto.input.PublisherListParam;
import com.syswin.pipeline.manage.dto.input.AddPublisherParam;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.utils.PermissionUtil;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.utils.EnumsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 115477 on 2019/1/8.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/publisher")
@Api(value = "publisher", tags = "出版社")
public class PublisherInnerController {

	@Autowired
	private PiperPublisherService publisherService;
	@Autowired
	private HeaderService headerService;
	@Autowired
	private AdminService adminService;

	@Value("${pipertype.promission}")
	private String piperPro;

	@PostMapping("/list")
	@ApiOperation(
					value = "出版社列表"
	)
	public ResponseEntity<PageInfo> list(@RequestBody PublisherListParam plb, HttpServletRequest request) {
		Integer pageNo = StringUtils.isNullOrEmpty(plb.getPageNo()) ? 1 : Integer.parseInt(plb.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(plb.getPageSize()) ? 20 : Integer.parseInt(plb.getPageSize());
		// 前段token授权信息放在请求头中传入

		String manageId = headerService.getUserId(request);

		return new ResponseEntity(publisherService.list(pageNo, pageSize, plb.getKeyword(), plb.getHasRecommend(), plb.getPiperType(), manageId));
	}


	@PostMapping("/getPiperType")
	@ApiOperation(
					value = "获取出版社类型"
	)
	public ResponseEntity getPiperType(HttpServletRequest request) {
		String manageId = headerService.getUserId(request);

		if (manageId == null) {
			//用户不是系统管理员，只能创建自己的出版社
			return new ResponseEntity(EnumsUtil.toList());
		} else {
			List list = new ArrayList();
			if (PermissionUtil.getPipterPromission(piperPro, "person")) {
				Map<String, Object> map = new HashMap();
				map.put("code", PublisherTypeEnums.person.getCode());
				map.put("name", PublisherTypeEnums.person.getName());
				list.add(map);
			}
			if (PermissionUtil.getPipterPromission(piperPro, "feedpublish")) {
				Map<String, Object> map1 = new HashMap();
				map1.put("code", PublisherTypeEnums.feedpublish.getCode());
				map1.put("name", PublisherTypeEnums.feedpublish.getName());
				list.add(map1);
			}
			if (PermissionUtil.getPipterPromission(piperPro, "organize")) {
				Admin admin = adminService.getAdmin(manageId, PublisherTypeEnums.organize);
				if (admin != null) {
					Map<String, Object> map2 = new HashMap();
					map2.put("code", PublisherTypeEnums.organize.getCode());
					map2.put("name", PublisherTypeEnums.organize.getName());
					list.add(map2);
				}
			}
			if (PermissionUtil.getPipterPromission(piperPro, "ciftis")) {
				Admin admin1 = adminService.getAdmin(manageId, PublisherTypeEnums.ciftis);
				if (admin1 != null) {
					Map<String, Object> map2 = new HashMap();
					map2.put("code", PublisherTypeEnums.ciftis.getCode());
					map2.put("name", PublisherTypeEnums.ciftis.getName());
					list.add(map2);
				}
			}
			return new ResponseEntity(list);
		}

	}

	@PostMapping("/add")
	@ApiOperation(
					value = "添加出版社"
	)
	public ResponseEntity add(@RequestBody AddPublisherParam publisherParam, HttpServletRequest request) {
		publisherParam.setPublishMail(null);
		String manageId = headerService.getUserId(request);
		if (manageId != null && !manageId.equals(publisherParam.getUserId())) {
			//用户不是系统管理员，只能创建自己的出版社
			publisherParam.setUserId(manageId);
		}

		Publisher publisher = publisherService.addPublisher(publisherParam.getUserId(), publisherParam.getPublishName(), publisherParam.getPublishMail(), Integer.parseInt(publisherParam.getPiperType()));
		return new ResponseEntity(publisher);
	}


	@PostMapping("/delete")
	@ApiOperation(
					value = "删除出版社,同时删除所有订阅者，所有的推荐"
	)
	public ResponseEntity delete(@RequestBody DelPublisherParam publisherParam, HttpServletRequest request) {
		publisherService.delete(publisherParam.getPublisherId());
		return new ResponseEntity();
	}
}
