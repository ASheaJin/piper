package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.dto.output.AdminManageVO;
import com.syswin.pipeline.psservice.UpdateMenuService;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.ps.sdk.admin.service.impl.PSConfigService;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.utils.BeanConvertUtil;
import com.syswin.sub.api.utils.EnumsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class ManagePiperAdminServiceTest {
	@Autowired
	private AdminService adminService;
	@Autowired
	private PSConfigService psConfigService;

	@Autowired
	private UpdateMenuService updateMenuService;
	@Value("${app.pipeline.userId}")
	private String piper;

	public Admin add(String adminUserId, String userId, PublisherTypeEnums ptype, boolean isFirst) {
		//TODO 此处需要更新 A.Piper的菜单
		updateMenuService.updateMenu(piper, userId);
		return adminService.add(adminUserId, userId, ptype, isFirst);
	}

	public void delete(String adminUserId, String userId) {
		//TODO 此处需要更新 A.Piper的菜单
		updateMenuService.updateMenu(piper, userId);
		adminService.delete(adminUserId, userId, PublisherTypeEnums.organize);
	}

	public PageInfo list(String keyword, String piperType, Integer pageNo, Integer pageSize) {
		PublisherTypeEnums pType = null;
		if (!StringUtils.isNullOrEmpty(piperType) && !"0".equals(piperType)) {
			pType = EnumsUtil.getPubliserTypeEnums(Integer.parseInt(piperType));
		}
		List<Admin> adminList = adminService.list(keyword, pType, pageNo, pageSize);
//		List<AdminManageVO> adminMangeVOList = BeanConvertUtil.mapList(adminList, AdminManageVO.class);
		List<AdminManageVO> adminMangeVOList = new ArrayList<>();
		for (Admin ad : adminList) {
			AdminManageVO avo = BeanConvertUtil.map(ad, AdminManageVO.class);
			avo.setPtype(EnumsUtil.getPubliserTypeEnums(ad.getPtype().getCode()).getName());
			adminMangeVOList.add(avo);
		}

		PageInfo pageInfo = new PageInfo<>(adminList);
		pageInfo.setList(adminMangeVOList);
		return pageInfo;
	}

	public Admin getAdmin(String userId, PublisherTypeEnums organize) {
		return adminService.getAdmin(userId, organize);
	}

	public List<Admin> getAdmins(String keyword, String userId, PublisherTypeEnums organize, int pageno, int pagesize) {
		return adminService.getAdmins(keyword, userId, organize, pageno, pagesize);
	}
}


