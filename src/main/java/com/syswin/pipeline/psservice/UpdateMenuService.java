package com.syswin.pipeline.psservice;

import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.admin.platform.entity.FunctionItem;
import com.syswin.ps.sdk.admin.service.FunctionItemService;
import com.syswin.ps.sdk.admin.service.impl.PSConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:lhz
 * @date:2019/5/30 15:31
 */
@Service
public class UpdateMenuService {

	@Autowired
	private PSConfigService psConfigService;

	@Autowired
	private FunctionItemService functionItemService;

	public boolean updateMenu(String account) {
		if (StringUtil.isEmpty(account)) {
			throw new BusinessException("ex.userid.null");
		}
		//更新账号时间搓
		List<FunctionItem> functionItemList = functionItemService.findFunctionItem();
		functionItemList.stream().forEach(f -> {
			if (account.equals(f.getAccountNo())) {
				functionItemService.updateFunctionItem(f);
			}
		});

		//清理缓存
		psConfigService.initFunctionList();
		return true;
	}
}
