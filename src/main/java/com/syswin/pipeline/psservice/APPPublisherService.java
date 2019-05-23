package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.admin.constant.FastJsonUtil;
import com.syswin.ps.sdk.admin.controller.in.AccountIn;
import com.syswin.ps.sdk.admin.platform.entity.AccountInfo;
import com.syswin.ps.sdk.admin.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author:lhz
 * @date:2019/5/23 17:30
 */
@Service
public class APPPublisherService {

	private static final Logger logger = LoggerFactory.getLogger(APPPublisherService.class);
	public AccountService accountService;

	public boolean addAccount(AccountIn accountIn) {
		AccountInfo result = null;
		AccountInfo accountInfo = FastJsonUtil.convertObj(accountIn, AccountInfo.class);
		try {
			result = this.accountService.addAccount(accountInfo);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" APPPublisherService addAccount",e);
		}
		return result != null;
	}
}
