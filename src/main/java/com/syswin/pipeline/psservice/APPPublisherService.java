package com.syswin.pipeline.psservice;

import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.admin.constant.AdminException;
import com.syswin.ps.sdk.admin.platform.entity.AccountInfo;
import com.syswin.ps.sdk.admin.platform.entity.FunctionItem;
import com.syswin.ps.sdk.admin.platform.entity.VCardInfo;
import com.syswin.ps.sdk.admin.service.AccountService;
import com.syswin.ps.sdk.admin.service.FunctionItemService;
import com.syswin.ps.sdk.admin.service.VCardInfoService;
import com.syswin.ps.sdk.admin.service.impl.PSAccountService;
import com.syswin.ps.sdk.service.NickNameService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Publisher;
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
	//创建出版社
	private static final String a_createp = "web/create-publish";
	//我的出版社
	private static final String a_publish = "web/my-publish";
	//推荐广场
	private static final String a_recommend = "web/recommend-list";
	//我的订阅列表
	private static final String a_subcrib_list = "web/subscribe-list";
	//邮件组管理
	private static final String a_group = "web";


	//历史消息
	private static final String p_history = "web/history-list";
	//订阅管理
	private static final String p_goup = "web/home";
	//Excel上传
	private static final String p_upload = "h5/help/upload";
	//更换管理员
	private static final String p_change = "web/edit-email-admin";

	@Value("${url.piper}")
	private String defaultUrl;
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

	@Autowired
	private PublisherService publisherService;

	@Value("${app.pipeline.userId}")
	private String from;

	@Value("${app.pipeline.imgUrl}")
	private String iconUrl;

	@Transactional
	public boolean registerAndLoginPiperAcount(String accountNo) {

//		accountService.findAccount(accountNo)
		psServerService.registerAccount(accountNo);
		//添加并激活账号
		addAccount(accountNo, "1234", "欢迎使用Piper");
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
		addRoleMenu(from);
		//登录账号
		psAccountService.login(from);
		return true;
	}


	@Transactional
	public boolean addPiperAcount(String accountNo) {

		Publisher p = publisherService.getPubLisherByPublishTmail(accountNo, null);
		if (p == null) {
			return false;
		}
		//添加并激活账号
		addAccount(accountNo, "1234", "请使用小助手");
		//添加名片
		addCard(accountNo, p.getName());
		//添加角色菜单
		addPiperRoleMenu(accountNo, p);
		//登录账号
		psAccountService.login(accountNo);
		return true;
	}

	private void addRoleMenu(String accountNo) {
		//添加中文菜单
		addMenuIte(accountNo, "创建个人出版社", getUrl(a_createp), 4, 1, "guest", "游客", iconUrl);
		addMenuIte(accountNo, "我的出版社", getUrl(a_publish), 4, 1, "person", "个人出版社", iconUrl);
		addMenuIte(accountNo, "我的订阅列表", getUrl(a_subcrib_list), 4, 2, "common", "公用", iconUrl);
		addMenuIte(accountNo, "推荐广场", getUrl(a_recommend), 4, 3, "common", "公用", iconUrl);
		addMenuIte(accountNo, "邮件组管理", getUrl(a_group), 4, 4, "org", "公用", iconUrl);

		//添加英文菜单
		addMenuIte(accountNo, "Create Publisher", getUrl(a_createp, null, "en"), 1, "guest-en");
		addMenuIte(accountNo, "My Publisher", getUrl(a_publish, null, "en"), 1, "person-en");
		addMenuIte(accountNo, "My Subscritions", getUrl(a_subcrib_list, null, "en"), 2, "common-en");
		addMenuIte(accountNo, "Recommend areas", getUrl(a_recommend, null, "en"), 3, "common-en");
		addMenuIte(accountNo, "Manage Group", getUrl(a_group, null, "en"), 4, "org-en");
	}

	private void addPiperRoleMenu(String accountNo, Publisher p) {
		//添加中文菜单
		addMenuIte(accountNo, "撰写", "--", 5, 1, "common", "公用", iconUrl);
		addMenuIte(accountNo, "撰写", "--", 4, 1, "common", "个人出版社", iconUrl);
		addMenuIte(accountNo, "历史消息", getUrl(p_history, p.getPublisherId()), 4, 2, "common", "公用", iconUrl);
		addMenuIte(accountNo, "订阅管理", getUrl(p_goup, p.getPublisherId()), 4, 3, "org", "公用", iconUrl);
		addMenuIte(accountNo, "Excel上传", getUrl(p_upload, p.getPublisherId()), 4, 4, "org", "公用", iconUrl);
		addMenuIte(accountNo, "更换组织成员", getUrl(p_change, p.getPublisherId()), 4, 5, "org", "公用", iconUrl);

		//添加英文菜单
		addMenuIte(accountNo, "write", "--", 5, 1, "common-en", "公用", iconUrl);
		addMenuIte(accountNo, "write", "--", 4, 1, "common-en", "个人出版社", iconUrl);
		addMenuIte(accountNo, "History", getUrl(p_history, p.getPublisherId(), "en"), 4, 2, "common-en", "公用", iconUrl);
		addMenuIte(accountNo, "Group Manage", getUrl(p_goup, p.getPublisherId()), 4, 3, "org-en", "公用", iconUrl);
		addMenuIte(accountNo, "Excel Upload", getUrl(p_upload, p.getPublisherId(), "en"), 4, 4, "org-en", "公用", iconUrl);
		addMenuIte(accountNo, "Change Owner", getUrl(p_change, p.getPublisherId(), "en"), 4, 5, "org-en", "公用", iconUrl);

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
		return addMenuIte(accountNo, itemName, url, 4, displayOrder, roleType, "--", iconUrl);
	}

	public boolean addMenuIte(String accountNo, String itemName, String url, int itemType, int displayOrder, String roleType, String roleDesc, String imgUrl) {
		FunctionItem functionItem = new FunctionItem();
		functionItem.setAccountNo(accountNo);
		functionItem.setFunctionName(itemName);
		functionItem.setFunctionUrl(url);
		functionItem.setItemType(itemType);
		functionItem.setDisplayOrder(displayOrder);
		functionItem.setUseStatus(1);
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

	public boolean addCard(String accountNo, String name) {
		VCardInfo vi = new VCardInfo();
		vi.setAccountNo(accountNo);
		vi.setAccountDesc("--");
		vi.setNickname(name);
		vi.setAccountImage(iconUrl);
		VCardInfo result = this.vCardInfoService.addVCardInfo(vi);
		if (StringUtils.isNotEmpty(vi.getNickname())) {
			this.nickNameService.resetNickName(vi.getAccountNo(), vi.getNickname());
		}
		return result != null;
	}

	public boolean addCard(String accountNo) {
		return addCard(accountNo, "Piper");
	}


	private String getUrl(String path) {
		return getUrl(path, null, null);
	}

	private String getUrl(String path, String publisherId) {
		return getUrl(path, publisherId, null);
	}

	private String getUrl(String path, String publisherId, String lang) {
		String l = (!StringUtil.isEmpty(lang) && lang.contains("en")) ? "?lang=en" : "?lang=zh";

		String r = defaultUrl + path + l;
		if (!StringUtil.isEmpty(publisherId)) {
			r = r + "&publisherId=" + publisherId;
		}
		return r;
	}
}
