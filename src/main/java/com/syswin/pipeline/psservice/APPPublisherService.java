package com.syswin.pipeline.psservice;

import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.ps.sdk.admin.constant.AdminException;
import com.syswin.ps.sdk.admin.platform.entity.AccountInfo;
import com.syswin.ps.sdk.admin.platform.entity.FunctionItem;
import com.syswin.ps.sdk.admin.platform.entity.VCardInfo;
import com.syswin.ps.sdk.admin.service.AccountService;
import com.syswin.ps.sdk.admin.service.FunctionItemService;
import com.syswin.ps.sdk.admin.service.VCardInfoService;
import com.syswin.ps.sdk.admin.service.impl.PSAccountService;
import com.syswin.ps.sdk.service.NickNameService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:lhz
 * @date:2019/5/23 17:30
 */
@Service
public class APPPublisherService {

	private static final Logger logger = LoggerFactory.getLogger(APPPublisherService.class);

	@Autowired
	private AccountService accountService;

	@Autowired
	private PSAccountService psAccountService;

	@Autowired
	private FunctionItemService functionItemService;

	@Autowired
	private VCardInfoService vCardInfoService;
	@Autowired
	private NickNameService nickNameService;

	@Autowired
	private RegisterServerService psServerService;


	@Value("${app.pipeline.userId}")
	private String from;


	@Transactional
	public boolean registerAndLoginPiperAcount(String accountNo) {

//		accountService.findAccount(accountNo)
		psServerService.registerAccount(accountNo);
		//添加并激活账号
		addAccount(accountNo, "1234", "欢迎使用");
		//添加名片
		addCard(accountNo);
		//添加角色菜单
		addRoleMenu(accountNo);
		//登录账号
		psAccountService.login(accountNo);
		return true;
	}

	@Transactional
	public boolean addAcount() {

		//添加并激活账号
		addAccount(from, "1234", "请使用小助手");
		//添加名片
		addCard(from);
		//添加角色菜单
		addPiperRoleMenu(from);
		//登录账号
		psAccountService.login(from);
		return true;
	}


	@Transactional
	public boolean addPiperAcount(String accountNo) {

		//添加并激活账号
		addAccount(accountNo, "1234", "欢迎使用");
		//添加名片
		addCard(accountNo);
		//添加角色菜单
		addRoleMenu(accountNo);
		//登录账号
		psAccountService.login(accountNo);
		return true;
	}

	private void addPiperRoleMenu(String accountNo) {
		//添加中文菜单
		addMenuIte(accountNo, "test", "http://www.baidu.com", 1, "1");
		addMenuIte(accountNo, "test22", "http://www.baidu.com", 3, "1");
		//添加英文菜单
	}



	private void addRoleMenu(String accountNo) {
		//添加中文菜单
		addMenuIte(accountNo, "test", "http://www.baidu.com", 1, "1");
		addMenuIte(accountNo, "test22", "http://www.baidu.com", 3, "1");
		//添加英文菜单


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

		} catch (AdminException e) {
			logger.error(" APPPublisherService addAccount", e);
			throw new BusinessException("账号已经注册过了");

		}
		return result != null;
	}


	public boolean addMenuIte(String accountNo, String itemName, String url, int displayOrder, String roleType) {
		return addMenuIte(accountNo, itemName, url, 2, displayOrder, 1, roleType, "121212", "http://pic34.nipic.com/20131026/9422601_213844930000_2.jpg");
	}

	public boolean addMenuIte(String accountNo, String itemName, String url, int itemType, int displayOrder, int useStatus, String roleType, String roleDesc, String imgUrl) {
		FunctionItem functionItem = new FunctionItem();
		functionItem.setAccountNo(accountNo);
		functionItem.setFunctionName(itemName);
		functionItem.setFunctionUrl(url);
		functionItem.setItemType(itemType);
		functionItem.setDisplayOrder(displayOrder);
		functionItem.setUseStatus(useStatus);
		functionItem.setFunctionImage(imgUrl);
		functionItem.setRoleType(roleType);
		functionItem.setRoleDesc(roleDesc);
		functionItem.setFunctionKey("input_write");
		functionItem.setTaipHost("1");
		functionItem.setTaipPort("1");
		functionItem.setTaipCommand(1);
		functionItem.setTaipCommandSpace(1);
//		functionItem.set
		FunctionItem r = functionItemService.addItem(functionItem);
		return r != null;
	}

	public boolean addCard(String accountNo) {
		VCardInfo vi = new VCardInfo();
		vi.setAccountNo(accountNo);
		vi.setAccountDesc("名片测试下");
		vi.setNickname("我的名片");
		vi.setAccountImage("http://pic34.nipic.com/20131026/9422601_213844930000_2.jpg");
		VCardInfo result = this.vCardInfoService.addVCardInfo(vi);
		if (StringUtils.isNotEmpty(vi.getNickname())) {
			this.nickNameService.resetNickName(vi.getAccountNo(), vi.getNickname());
		}
		return result != null;
	}
}
