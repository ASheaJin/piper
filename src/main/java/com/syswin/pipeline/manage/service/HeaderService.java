package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.*;
import com.syswin.pipeline.db.repository.MenuRepository;
import com.syswin.pipeline.db.repository.RoleMenuRepository;
import com.syswin.pipeline.db.repository.RoleRepository;
import com.syswin.pipeline.db.repository.UserRoleRepository;
import com.syswin.pipeline.manage.dto.MenuOutput;
import com.syswin.pipeline.manage.dto.RoleInput;
import com.syswin.pipeline.manage.dto.RoleOutput;
import com.syswin.pipeline.manage.shiro.StatelessToken;
import com.syswin.pipeline.manage.shiro.TokenManager;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.exceptions.SubException;
import com.syswin.sub.api.utils.BeanConvertUtil;
import com.syswin.sub.api.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2019/3/29.
 */
@Service
public class HeaderService {

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private UserService userService;


	public String getUserId(HttpServletRequest request) {

		String authorization = request.getHeader("authorization");
		if (StringUtils.isNullOrEmpty(authorization)) {
			throw new SubException("authorization 为空");
		}

		// 获取无状态Token
		StatelessToken accessToken = tokenManager.getToken(authorization);
		User user = userService.getUserByLoginName(accessToken.getLoginName());
		if (user == null) {
			throw new SubException("用户验证失败");
		}
		String manageId = null;
		//不是超级管理员，只能获取到自己的出版社
		if (user.getIsSys() != 1) {
			manageId = user.getEmail();
		}
		return manageId;
	}
}
