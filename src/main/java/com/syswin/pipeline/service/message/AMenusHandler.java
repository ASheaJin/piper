package com.syswin.pipeline.service.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.enums.PermissionEnums;
import com.syswin.pipeline.service.ConsumerService;
import com.syswin.pipeline.service.DeviceInfoService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.Env;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.util.CollectionUtil;
import com.syswin.pipeline.service.ps.util.FastJsonUtil;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.pipeline.utils.LanguageChange;
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
	private static final String SUBSCRIBE_LIST = "/web/subcribe-list";

	@Value("${url.piper}")
	private String URL_PIPER;
	@Autowired
	ConsumerService consumerService;
	@Lazy
	@Autowired
	private SendMessegeService sendMessegeService;
	@Lazy
	@Autowired
	private PSClientService psClientService;

	@Autowired
	private DeviceInfoService deviceInfoService;
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
		if (version.equals(myVersion) && appValue.equals(beforeLang)) {
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

		keyList.add("shortcuts");
		//判断当前用户是读者还是作者
		Map<String, Object> replyMsgObject = null;
		valueList.add(appList(header.getReceiver()));

		replyMsgObject = CollectionUtil.fastMap(keyList, valueList);
		replyMsgObject.put("version", myVersion);
		consumerService.updateUserVersion(header, myVersion, myRole);
		ChatMsg msg = new ChatMsg(header.getSender(), header.getReceiver(),
						UUID.randomUUID().toString(), replyMsgObject);
		msg.setBody_type(GET_MESSAGE_INFO_FLAG);

		psClientService.sendChatMessage(msg, header.getReceiver(), header.getReceiverPK());
	}

	private List<Map<String, Object>> appFeaturesList() {
		List<Map<String, Object>> appList = new ArrayList<>();

		for (int i = 1; i < 10; i++) {
			appList.add(createApp("", "#@" + i, ""));
		}
		return appList;
	}

	private List<Map<String, Object>> appList(String userId) {
		//TODO 处理京交会
		List<Map<String, Object>> appList = new ArrayList<>();
//		appList.add(createApp("", "测试邮箱跳转", URL_PIPER + "/webmg/index1"));
		//既是组织管理者，又是个人出版社管理者
		if (PermissionEnums.OrgPerson.name.equals(myRole)) {
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.mypublisher", userId), languageChange.getUrl(URL_PIPER + MY_PUBLISHER, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.gosub", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_ADD, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.mysublist", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_LIST, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.group", userId), languageChange.getUrl(URL_PIPER + "/web", userId)));
		}
		if (PermissionEnums.OnlyOrg.name.equals(myRole)) {
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.createpublisher", userId), languageChange.getUrl(URL_PIPER + PUBLISHER_CREATE, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.gosub", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_ADD, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.mysublist", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_LIST, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.group", userId), languageChange.getUrl(URL_PIPER + "/web", userId)));
		}
		//个人管理者，订阅者
		if (PermissionEnums.Person.name.equals(myRole)) {
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.mypublisher", userId), languageChange.getUrl(URL_PIPER + MY_PUBLISHER, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.gosub", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_ADD, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.mysublist", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_LIST, userId)));
		}
		//游客，订阅者
		if (PermissionEnums.Guest.name.equals(myRole)) {
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.createpublisher", userId), languageChange.getUrl(URL_PIPER + PUBLISHER_CREATE, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.gosub", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_ADD, userId)));
			appList.add(createApp("", languageChange.getValueByUserId("menu.a.mysublist", userId), languageChange.getUrl(URL_PIPER + SUBSCRIBE_LIST, userId)));
		}
		return appList;
	}


	private Map<String, Object> createApp(String iconUrl, String appName, String path) {
		List<String> keys1 = CollectionUtil.fastList("icon", "title", "url");
		List<Object> app11 = CollectionUtil.fastList(iconUrl, appName, path);
		return CollectionUtil.fastMap(keys1, app11);
	}


//	private Map<String, Object> createFileApp(String iconUrl, String appName, String helper) {
//		List<String> keys1 = CollectionUtil.fastList("icon", "title", "helper", "url", "extInfo");
//		List<Object> app11 = CollectionUtil.fastList(iconUrl, appName, helper, "", new HashMap());
//		return CollectionUtil.fastMap(keys1, app11);
//	}

	public static void main(String[] args) {
		String envValue = "{\"language\":\"zh\",\"platform\":\"android\",\"moduleVersion\":\"1.0.0\",\"os_version\":25,\"version\":\"1.2.0P\",\"build\":\"1904030921\"}";
		Env appEnv = JacksonJsonUtil.fromJson(envValue, Env.class);
		System.out.println(appEnv);
	}
}
