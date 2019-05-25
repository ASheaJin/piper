package com.syswin.pipeline.manage.service.impl;

import com.syswin.pipeline.db.model.Account;
import com.syswin.pipeline.db.model.Transaction;
import com.syswin.pipeline.db.repository.TransactionRepository;
import com.syswin.pipeline.manage.service.ManageService;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.pipeline.utils.DateUtil;
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
public class TransactionManageServiceImpl implements ManageService<Account> {

	private static final Logger logger = LoggerFactory.getLogger(TransactionManageServiceImpl.class);


	@Autowired
	TransactionRepository transactionRepository;

	public Map<String, Object> list(HttpServletRequest request, Model model) {

		String userId = StringUtils.getParam(request, "userId", "");

		int type = StringUtils.getParam(request, "type", 0);
		String createTime = StringUtils.getParam(request, "createTime", null);
		model.addAttribute("userId", userId);
		model.addAttribute("type", type);
		model.addAttribute("createTime", createTime);
		int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
		int pageSize = StringUtils.getParam(request, "pageSize", 10);
		pageIndex = pageIndex < 1 ? 1 : pageIndex;
		pageSize = pageSize > 30 || pageSize < 1 ? 30 : pageSize;
		int start = pageSize * (pageIndex - 1);
		Map<String, Object> map = new HashMap<>();
		int startTime = 0 , endTime = 0;
		if (StringUtil.isNotEmpty(createTime)){
			String[] times = createTime.split(" 至 ");
			 startTime = DateUtil.switchDate(times[0]);
			 endTime = DateUtil.switchDate(times[1]);
		}
		List<Transaction> transactionList = transactionRepository.selectByIdTypeTime(userId,type,startTime,endTime,start, pageSize);
		long count = transactionRepository.getCount();
		logger.info(transactionList.toString());
		map.put("total", count);
		map.put("data", transactionList ==null ? new ArrayList<Transaction>() :transactionList );

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
