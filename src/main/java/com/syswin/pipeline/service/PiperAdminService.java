package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.db.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

	public PageInfo<Admin> list(Integer pageNo, Integer pageSize, String keyword, String userId) {
		Admin admin = adminService.getAdmin(userId);
		if(admin == null){
			throw new BusinessException("你不是管理员，无权操作");
		}

		return adminService.list(keyword, pageNo, pageSize);
	}
}


