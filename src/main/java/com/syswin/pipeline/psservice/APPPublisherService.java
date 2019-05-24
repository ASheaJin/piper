package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.admin.constant.FastJsonUtil;
import com.syswin.ps.sdk.admin.controller.in.AccountIn;
import com.syswin.ps.sdk.admin.controller.inout.BaseVCardInfo;
import com.syswin.ps.sdk.admin.platform.entity.AccountInfo;
import com.syswin.ps.sdk.admin.platform.entity.FunctionItem;
import com.syswin.ps.sdk.admin.platform.entity.VCardInfo;
import com.syswin.ps.sdk.admin.service.AccountService;
import com.syswin.ps.sdk.admin.service.FunctionItemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:lhz
 * @date:2019/5/23 17:30
 */
@Service
public class APPPublisherService {

	private static final Logger logger = LoggerFactory.getLogger(APPPublisherService.class);

	@Autowired
	public AccountService accountService;

	@Autowired
	FunctionItemService functionItemService;


	public boolean addPiperAcount(String accountNo) {
		addAccount(accountNo, "1234", "欢迎使用");
//		addCard(accountNo,);
		addMenuIte(accountNo, "test", "http://www.baidu.com", 2, 1, 1, "1", "2222");
		addMenuIte(accountNo, "test22", "http://www.baidu.com", 2, 3, 1, "1", "2222");
		return true;
	}

	//添加激活账号
	public boolean addAccount(String accountNo, String code, String msg) {

		AccountInfo result = null;
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setAccountNo(accountNo);
		accountInfo.setActiveCode(code);
		accountInfo.setWelcomeMsg(msg);
		try {
			result = this.accountService.addAccount(accountInfo);
			accountService.startAccount(result.getAccountId(), result.getAccountNo());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" APPPublisherService addAccount", e);
		}
		return result != null;
	}


	public boolean addMenuIte(String accountNo, String itemName, String url, int displayOrder, String roleType) {
		return addMenuIte(accountNo, itemName, url, 2, displayOrder, 1, roleType, "");
	}

	public boolean addMenuIte(String accountNo, String itemName, String url, int itemType, int displayOrder, int useStatus, String roleType, String roleDesc) {
		FunctionItem functionItem = new FunctionItem();
		functionItem.setAccountNo(accountNo);
		functionItem.setFunctionName(itemName);
		functionItem.setFunctionUrl(url);
		functionItem.setItemType(itemType);
		functionItem.setDisplayOrder(displayOrder);
		functionItem.setUseStatus(useStatus);
		functionItem.setRoleType(roleType);
		functionItem.setRoleDesc(roleDesc);
		FunctionItem r = functionItemService.addItem(functionItem);
		return r != null;
	}

//	public boolean addCard() {
//		BaseVCardInfo
//		VCardInfo vCardInfo = (VCardInfo) FastJsonUtil.convertObj(vCardInfoIn, VCardInfo.class);
//		VCardInfo result = this.vCardInfoService.addVCardInfo(vCardInfo);
//		if (StringUtils.isNotEmpty(vCardInfoIn.getNickname())) {
//			this.nickNameService.resetNickName(vCardInfoIn.getAccountNo(), vCardInfoIn.getNickname());
//		}
//	}
}
