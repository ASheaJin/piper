package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.vo.output.AdminManageVO;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.utils.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class PiperAdminService {
	@Autowired
	AdminService adminService;

	public Admin add(String adminUserId, String userId, boolean isFirst) {

		return adminService.add(adminUserId, userId, isFirst);
	}

	public void delete(String adminUserId, String userId) {
		adminService.delete(adminUserId, userId);
	}

	public PageInfo list(Integer pageNo, Integer pageSize, String keyword) {

		List<Admin> adminList = adminService.list(keyword, pageNo, pageSize);
		List<AdminManageVO> adminMangeVOList = BeanConvertUtil.mapList(adminList, AdminManageVO.class);

		PageInfo pageInfo = new PageInfo<>(adminList);
		pageInfo.setList(adminMangeVOList);
		return pageInfo;
	}
}


