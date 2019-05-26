package com.syswin.pipeline.psservice.olderps.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.enums.AppmuneEnum;
import com.syswin.pipeline.enums.PermissionEnums;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.psservice.olderps.ChatMsg;
import com.syswin.pipeline.psservice.olderps.Env;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.service.PiperConsumerService;
import com.syswin.pipeline.service.PiperDeviceInfoService;
import com.syswin.pipeline.utils.*;
import com.syswin.temail.ps.client.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 115477 on 2019/1/14.
 */
@Component
public class AMenusHandler implements EventHandler<MessageEvent> {

	private static final String PUBLISHER_CREATE = "/web/create-publish";
	private static final String MY_PUBLISHER = "/web/my-publish";
	//切换为推荐广场
	private static final String SUBSCRIBE_ADD = "/web/recommend-list";
	private static final String SUBSCRIBE_LIST = "/web/subscribe-list";

	@Value("${url.piper}")
	private String URL_PIPER;
	@Autowired
	PiperConsumerService consumerService;
	@Lazy
	@Autowired
	private SendMessegeService sendMessegeService;
	@Lazy
	@Autowired
	private PSClientService psClientService;

	@Autowired
	private PiperDeviceInfoService deviceInfoService;
	@Autowired
	private LanguageChange languageChange;
	/**
	 * 菜单的版本判断。
	 * 对999消息判断version，如果version与此值相等，则不处理。
	 * 如果不等，说明是999的第一次，则返回数据中version字段设为此值
	 */
	public String myRole = PermissionEnums.Guest.name;

	@Value("${app.pipeline.userId}")
	private String from;

	@Value("${a.piper.menu.promission}")
	private String menu;

	private final static Logger logger = LoggerFactory.getLogger(AMenusHandler.class);
	/**
	 * 输入板菜单的 bodyType
	 */
	public static final Integer GET_MESSAGE_INFO_FLAG = 999;


	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) {

		if (!event.getChatMsg().getBody_type().equals(GET_MESSAGE_INFO_FLAG)) {
			return;
		}
		Header header = event.getOriginHeader();
		if (!from.equals(header.getSender())) {
			return;
		}
		String version = PermissionEnums.Guest.name;
		Env appEnv = null;
		try {
			String content = event.getChatMsg().getContent();
			version = FastJsonUtil.parseObject(content).getString("version");
			String envValue = FastJsonUtil.parseObject(content).getString("env");
			appEnv = JacksonJsonUtil.fromJson(envValue, Env.class);
		} catch (Exception e) {
			logger.info("处理消息中的version字段失败", e);
		}
		if (!consumerService.getUserVersion(header)) {
			String pdfInfo = "{\"format\":\"application/pdf\",\"url\":\"https://ucloud-file.t.email/%2Fceca224cce52468dabc22390f2289e97.zip\",\"pwd\":\"EB04F13C-E30B-492E-90FA-E5300139041E\",\"suffix\":\".pdf\",\"desc\":\"Piper操作手册1.1.pdf\",\"size\":255784,\"percent\":100}";
			sendMessegeService.sendOthermessage(pdfInfo, 14, header.getReceiver(), from);
		}
		myRole = consumerService.getAMenuRole(header.getReceiver());
		String myVersion = consumerService.getUserVersion(header, version, myRole);

		String beforeLang = deviceInfoService.getLang(header.getReceiver());
		String appValue = "zh";
		//先更新数据库，在判断中英文是否一致
		if (appEnv != null) {
			appValue = deviceInfoService.insertOrupdate(header.getReceiver(), appEnv);
		}
		//判断版本是否一致 并且语言是否一致
		if (version.equals(myVersion) && beforeLang.contains(appValue)) {
			//版本号相同，不做加载
			return;
		}

		List keyList = new ArrayList();
		List valueList = new ArrayList();

		keyList.add("type");
		valueList.add(0);

		keyList.add("text");
		valueList.add(languageChange.getValueByUserId("menu.a.tip", header.getReceiver()));

//		keyList.add("features");
//		//判断当前用户是读者还是作者
//		valueList.add(appFeaturesList());

		//新版菜单
		keyList.add("helperConfig");
		valueList.add(appNewList(header.getReceiver()));

		//老版菜单
		keyList.add("shortcuts");
		valueList.add(appList(header.getReceiver()));


		Map<String, Object> replyMsgObject = null;
		replyMsgObject = CollectionUtil.fastMap(keyList, valueList);
		replyMsgObject.put("version", myVersion);
		consumerService.updateUserVersion(header, myVersion, myRole);
		ChatMsg msg = new ChatMsg(header.getSender(), header.getReceiver(),
						UUID.randomUUID().toString(), replyMsgObject);
		msg.setBody_type(GET_MESSAGE_INFO_FLAG);

		psClientService.sendChatMessage(msg, header.getReceiver(), header.getReceiverPK());
	}


	private List<Map<String, Object>> appNewList(String userId) {
		//TODO 处理京交会
		List<Map<String, Object>> appList = new ArrayList<>();
//		appList.add(createApp("", "测试邮箱跳转", URL_PIPER + "/webmg/index1"));
		//既是组织管理者，又是个人出版社管理者  person,recommend,mysublist,group

		if (PermissionEnums.OrgPerson.name.equals(myRole)) {
			appList = getNewList(appList, userId, 2 + 4 + 8 + 16);
		}
		if (PermissionEnums.OnlyOrg.name.equals(myRole)) {
			appList = getNewList(appList, userId, 1 + 4 + 8 + 16);
		}
		//个人管理者，订阅者
		if (PermissionEnums.Person.name.equals(myRole)) {
			appList = getNewList(appList, userId, 2 + 4 + 8);
		}
		//游客，订阅者
		if (PermissionEnums.Guest.name.equals(myRole)) {
			appList = getNewList(appList, userId, 1 + 4 + 8);
		}
		return appList;
	}

	private List getNewList(List appList, String userId, int permission) {
		//person,recommend,mysublist,group
		if (PermissionUtil.getMenuPromission(menu, "person")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.person.type)) {

				appList.add(createNewApp("", languageChange.getValueByUserId("menu.a.createpublisher", userId), languageChange.getUrl(URL_PIPER + PUBLISHER_CREATE, userId)));
			}
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.mypublish.type)) {
				appList.add(createNewApp("", languageChange.getValueByUserId("menu.a.mypublisher", userId), languageChange.getUrl(URL_PIPER + MY_PUBLISHER, userId)));

			}
		}
		if (PermissionUtil.getMenuPromission(menu, "recommend")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.recommend.type)) {
				appList.add(createNewApp("", languageChange.getValueByUserId("menu.a.gosub", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_ADD, userId)));
			}
		}
		if (PermissionUtil.getMenuPromission(menu, "mysublist")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.mysublist.type)) {
				appList.add(createNewApp("", languageChange.getValueByUserId("menu.a.mysublist", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_LIST, userId)));

			}
		}
		if (PermissionUtil.getMenuPromission(menu, "group")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.group.type)) {
				appList.add(createNewApp("", languageChange.getValueByUserId("menu.a.group", userId), languageChange.getUrl(URL_PIPER + "/web", userId)));
			}
		}
		return appList;
	}

	private Map<String, Object> createNewApp(String key, String title, String url) {
		List<String> keys1 = CollectionUtil.fastList("key", "title", "url");
		List<Object> app11 = CollectionUtil.fastList(key, title, url);
		return CollectionUtil.fastMap(keys1, app11);
	}


	private Map<String, Object> createApp(String iconUrl, String appName, String path) {
		List<String> keys1 = CollectionUtil.fastList("icon", "title", "url");
		List<Object> app11 = CollectionUtil.fastList(iconUrl, appName, path);
		return CollectionUtil.fastMap(keys1, app11);
	}


	private List<Map<String, Object>> appList(String userId) {
		//TODO 处理京交会
		List<Map<String, Object>> appList = new ArrayList<>();
//		appList.add(createApp("", "测试邮箱跳转", URL_PIPER + "/webmg/index1"));
		//既是组织管理者，又是个人出版社管理者  person,recommend,mysublist,group

		if (PermissionEnums.OrgPerson.name.equals(myRole)) {
			appList = getList(appList, userId, 2 + 4 + 8 + 16);
		}
		if (PermissionEnums.OnlyOrg.name.equals(myRole)) {
			appList = getList(appList, userId, 1 + 4 + 8 + 16);
		}
		//个人管理者，订阅者
		if (PermissionEnums.Person.name.equals(myRole)) {
			appList = getList(appList, userId, 2 + 4 + 8);
		}
		//游客，订阅者
		if (PermissionEnums.Guest.name.equals(myRole)) {
			appList = getList(appList, userId, 1 + 4 + 8);
		}
		return appList;
	}

	private List getList(List appList, String userId, int permission) {
		//person,recommend,mysublist,group
		if (PermissionUtil.getMenuPromission(menu, "person")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.person.type)) {

				appList.add(createApp("", languageChange.getValueByUserId("menu.a.createpublisher", userId), languageChange.getUrl(URL_PIPER + PUBLISHER_CREATE, userId)));
			}
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.mypublish.type)) {
				appList.add(createApp("", languageChange.getValueByUserId("menu.a.mypublisher", userId), languageChange.getUrl(URL_PIPER + MY_PUBLISHER, userId)));

			}
		}
		if (PermissionUtil.getMenuPromission(menu, "recommend")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.recommend.type)) {
				appList.add(createApp("", languageChange.getValueByUserId("menu.a.gosub", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_ADD, userId)));
			}
		}
		if (PermissionUtil.getMenuPromission(menu, "mysublist")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.mysublist.type)) {
				appList.add(createApp("", languageChange.getValueByUserId("menu.a.mysublist", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_LIST, userId)));

			}
		}
		if (PermissionUtil.getMenuPromission(menu, "group")) {
			if (PermissionUtil.checkmenus(permission, AppmuneEnum.group.type)) {
				appList.add(createApp("", languageChange.getValueByUserId("menu.a.group", userId), languageChange.getUrl(URL_PIPER + "/web", userId)));
			}
		}
		return appList;
	}


}
