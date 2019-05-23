package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.admin.constant.FastJsonUtil;
import com.syswin.ps.sdk.admin.controller.in.AccountIn;
import com.syswin.ps.sdk.admin.platform.entity.AccountInfo;
import com.syswin.ps.sdk.admin.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * @author:lhz
 * @date:2019/5/23 17:30
 */
@Service
public class APPPublisherService {

	public AccountService accountService;

	public boolean addAccount(AccountIn accountIn) {
		AccountInfo result = null;
		AccountInfo accountInfo = FastJsonUtil.convertObj(accountIn, AccountInfo.class);
		try {
			result = this.accountService.addAccount(accountInfo);

		} catch (Exception e) {
		}
		return result != null;
	}
}
