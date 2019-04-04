package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.vo.input.AddAdmin;
import com.syswin.pipeline.manage.vo.input.AdminList;
import com.syswin.pipeline.service.PiperAdminService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Admin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

	@PostMapping("/list")
	@ApiOperation(
					value = "管理员列表"
	)
	public ResponseEntity<PageInfo> list(@RequestBody AdminList adminList, HttpServletRequest request) {
		Integer pageNo = StringUtils.isNullOrEmpty(adminList.getPageNo()) ? 1 : Integer.parseInt(adminList.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(adminList.getPageSize()) ? 20 : Integer.parseInt(adminList.getPageSize());

		return new ResponseEntity(piperAdminService.list(pageNo, pageSize, adminList.getKeyword()));
	}

	@PostMapping("/add")
	@ApiOperation(
					value = "添加管理员"
	)
	public ResponseEntity<Admin> add(@RequestBody AddAdmin addAdmin, HttpServletRequest request) {
		Admin admin = piperAdminService.add(HeaderUtil.getUserId(request), addAdmin.getUserId(), false);
		return new ResponseEntity(admin);
	}


	@PostMapping("/delete")
	@ApiOperation(
					value = "删除管理员"
	)
	public ResponseEntity delete(@RequestBody AddAdmin delAdmin, HttpServletRequest request) {
		piperAdminService.delete(HeaderUtil.getUserId(request), delAdmin.getUserId());
		return new ResponseEntity();
	}
}
