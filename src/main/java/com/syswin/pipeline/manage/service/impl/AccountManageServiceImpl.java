package com.syswin.pipeline.manage.service.impl;

import com.syswin.pipeline.db.model.Account;
import com.syswin.pipeline.db.repository.AccountRepository;
import com.syswin.pipeline.manage.service.ManageService;
import com.syswin.pipeline.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class AccountManageServiceImpl implements ManageService<Account> {

	private static final Logger logger = LoggerFactory.getLogger(AccountManageServiceImpl.class);


	@Autowired
	AccountRepository accountRepository;

	public Map<String, Object> list(HttpServletRequest request, Model model) {

		String userId = StringUtils.getParam(request, "userId", "");
		model.addAttribute("userId", userId);
		int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
		int pageSize = StringUtils.getParam(request, "pageSize", 10);
		pageIndex = pageIndex < 1 ? 1 : pageIndex;
		pageSize = pageSize > 30 || pageSize < 1 ? 30 : pageSize;
		int start = pageSize * (pageIndex - 1);
		Map<String, Object> map = new HashMap<>();

		long count = accountRepository.getCount();
		//如果userId 不为空 条件查询
		List<Account> accountList = accountRepository.selectByUserIdlike(userId,start, pageSize);
		logger.info(accountList.toString());
		map.put("total", count);
		map.put("data", accountList == null ? new ArrayList<Account>() : accountList);

		return map;
	}

	@Override
	public Account edit(Account account) {
		return null;
	}

	@Override
	public boolean add(Account account) {
		return false;
	}

	@Override
	public boolean remove(Account account) {
		return false;
	}
}
