package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.service.HeaderService;
import com.syswin.pipeline.manage.dto.input.AddAdmin;
import com.syswin.pipeline.manage.dto.input.AdminList;
import com.syswin.pipeline.service.PiperAdminService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.PermissionUtil;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Admin;
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
@RequestMapping("/manage/admin")
@Api(value = "admin", tags = "管理员")
public class AdminInnerController {

	@Autowired
	private PiperAdminService piperAdminService;
	@Autowired
	private HeaderService headerService;
	@Value("${pipertype.promission}")
	private String piperPro;

	@PostMapping("/list")
	@ApiOperation(
					value = "管理员列表"
	)
	public ResponseEntity<PageInfo> list(@RequestBody AdminList adminList, HttpServletRequest request) {
		Integer pageNo = StringUtils.isNullOrEmpty(adminList.getPageNo()) ? 1 : Integer.parseInt(adminList.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(adminList.getPageSize()) ? 20 : Integer.parseInt(adminList.getPageSize());

		return new ResponseEntity(piperAdminService.list(adminList.getKeyword(), adminList.getPiperType(), pageNo, pageSize));
	}

	@PostMapping("/getPiperType")
	@ApiOperation(
					value = "获取出版社管理类型"
	)
	public ResponseEntity getPiperType(HttpServletRequest request) {

		String manageId = headerService.getUserId(request);

		List list = new ArrayList();
		//目前支持两种 组织，京交会，其他的都可以创建
		if (PermissionUtil.getPipterPromission(piperPro, "organize")) {
			Map<String, Object> map = new HashMap();
			map.put("code", PublisherTypeEnums.organize.getCode());
			map.put("name", PublisherTypeEnums.organize.getName());
			list.add(map);
		}
		if (PermissionUtil.getPipterPromission(piperPro, "ciftis")) {
			//非超级管理员不能创京交会的
			if (StringUtils.isNullOrEmpty(manageId)) {
				Map<String, Object> map1 = new HashMap();
				map1.put("code", PublisherTypeEnums.ciftis.getCode());
				map1.put("name", PublisherTypeEnums.ciftis.getName());
				list.add(map1);
			}
		}

		return new ResponseEntity(list);
	}

	@PostMapping("/add")
	@ApiOperation(
					value = "添加管理员"
	)
	public ResponseEntity<Admin> add(@RequestBody AddAdmin addAdmin, HttpServletRequest request) {
		String manageId = headerService.getUserId(request);
		PublisherTypeEnums p = EnumsUtil.getPubliserTypeEnums(Integer.parseInt(addAdmin.getPiperType()));
		Admin admin = piperAdminService.add(manageId, addAdmin.getUserId(), p, false);
		return new ResponseEntity(admin);
	}


	@PostMapping("/delete")
	@ApiOperation(
					value = "删除管理员"
	)
	public ResponseEntity delete(@RequestBody AddAdmin delAdmin, HttpServletRequest request) {
		String manageId = headerService.getUserId(request);
		piperAdminService.delete(manageId, delAdmin.getUserId());
		return new ResponseEntity();
	}
}
