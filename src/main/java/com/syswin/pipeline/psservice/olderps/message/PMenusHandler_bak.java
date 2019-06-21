package com.syswin.pipeline.psservice.olderps.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.db.repository.ConsumerRepository;
import com.syswin.pipeline.enums.PermissionEnums;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.psservice.olderps.ChatMsg;
import com.syswin.pipeline.psservice.olderps.Env;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.service.PiperConsumerService;
import com.syswin.pipeline.service.PiperDeviceInfoService;
import com.syswin.pipeline.utils.CollectionUtil;
import com.syswin.pipeline.utils.FastJsonUtil;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.db.model.Subscription;
import com.syswin.sub.api.enums.PublisherTypeEnums;
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
public class PMenusHandler_bak implements EventHandler<MessageEvent> {

	@Value("${url.piper}")
	private String URL_PIPER;

	@Autowired
	PiperConsumerService consumerService;

	@Autowired
	private PublisherService publisherService;

	//	private static final String ICON_SUBSCRIBE_LIST = "http://jco-app.cn/html/icon/sublist.png";
	@Lazy
	@Autowired
	private SendMessegeService sendMessegeService;
	@Lazy
	@Autowired
	private PSClientService psClientService;

	@Autowired
	ConsumerRepository consumerRepository;

	@Autowired
	SubscriptionService subSubscriptionService;
	/**
	 * 菜单的版本判断。
	 * 对999消息判断version，如果version与此值相等，则不处理。
	 * 如果不等，说明是999的第一次，则返回数据中version字段设为此值
	 */
	public String myRole = PermissionEnums.Guest.name;

	@Value("${app.pipeline.userId}")
	private String from;
	private final static Logger logger = LoggerFactory.getLogger(PMenusHandler_bak.class);
	/**
	 * 输入板菜单的 bodyType
	 */
	public static final Integer GET_MESSAGE_INFO_FLAG = 999;
	@Autowired
	private PiperDeviceInfoService deviceInfoService;

	@Autowired
	private LanguageChange languageChange;

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) {

		if (!event.getChatMsg().getBody_type().equals(GET_MESSAGE_INFO_FLAG)) {
			return;
		}
		Header header = event.getOriginHeader();
		if (from.equals(header.getSender())) {
			return;
		}
		Publisher publisher = publisherService.getPubLisherByPublishTmail(header.getSender(), null);
		//如果不是出版社
		if (publisher == null) {
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

		myRole = consumerService.getPiperMenuRole(header.getReceiver(), header.getSender());
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
		sendTipsMessage(publisher, header.getReceiver());

		List keyList = new ArrayList();
		List valueList = new ArrayList();

		keyList.add("type");
		valueList.add(0);

		keyList.add("text");
		valueList.add(languageChange.getValueByUserId("menu.p.tip", header.getReceiver()));


		//新版本菜单
		keyList.add("helperConfig");
		valueList.add(appNewList(header.getReceiver(), publisher));

		//老版本菜单
		keyList.add("features");
		valueList.add(appFeaturesList(header.getReceiver()));

		keyList.add("shortcuts");
		valueList.add(appList(header.getReceiver(), publisher));

		Map<String, Object> replyMsgObject = null;
		replyMsgObject = CollectionUtil.fastMap(keyList, valueList);
		replyMsgObject.put("version", myVersion);
		consumerService.updateUserVersion(header, myVersion, myRole);
		ChatMsg msg = new ChatMsg(header.getSender(), header.getReceiver(),
						UUID.randomUUID().toString(), replyMsgObject);
		msg.setBody_type(GET_MESSAGE_INFO_FLAG);

		psClientService.sendChatMessage(msg, header.getReceiver(), header.getReceiverPK(), header.getSender(), header.getSenderPK());
	}

	//加载默认按钮 http://wiki.syswin.com/pages/viewpage.action?pageId=33708676
	private List<Map<String, Object>> appFeaturesList(String userId) {
		List<Map<String, Object>> appList = new ArrayList<>();

		appList.add(createApp("", "#@" + 1, ""));
		appList.add(createApp("", "#@" + 2, ""));
		appList.add(createApp("", "#@" + 5, ""));
		appList.add(createApp("", "#@" + 6, ""));
		appList.add(createApp("", "#@" + 8, ""));
//		appList.add(createApp("", "#@" + 10, ""));
		//撰写,如果老版本不支持
//		DeviceInfo deviceInfo = deviceInfoService.getDeviceInfo(userId);
//		if (deviceInfo != null) {
//			if (Integer.parseInt(deviceInfo.getBuild()) > 1904005617) {
//				appList.add(createApp("", "#@" + 13, ""));
//			}
//		}

//		appList.add(createApp("", "#@" + 13, ""));
//		for (int i = 1; i < 10; i++) {
//			appList.add(createApp("", "#@" + i, ""));
//		}
		return appList;
	}

//	private List<Map<String, Object>> appHelp() {
//
//		List<Map<String, Object>> appList = new ArrayList<>();
//
//		appList.add(createApp(ICON_ACCOUNT_INFO, "帮助", URL_PIPER + H5_HELP_INFO));
//
//		return appList;
//	}

	void sendTipsMessage(Publisher publisher, String userId) {
		if (publisher.getPtype().equals(PublisherTypeEnums.person)) {
			if (userId.equals(publisher.getUserId())) {
			} else {
				Subscription subscription = subSubscriptionService.getSub(userId, publisher.getPublisherId());
				if (subscription == null && !publisher.getUserId().equals(userId)) {
					sendMessegeService.sendTextMessage(languageChange.getValueByUserId("msg.ordertip", userId), userId, 0, publisher.getPtemail());
				}
			}
		}
	}


	private List<Map<String, Object>> appNewList(String userId, Publisher publisher) {

		List<Map<String, Object>> appList = new ArrayList<>();
		appList.add(createNewApp("helper_write", "", ""));
		appList.add(createNewApp("", languageChange.getValueByUserId("menu.p.history", userId), languageChange.getUrl(URL_PIPER + "/web/history-list", userId) + "&publisherId=" + publisher.getPublisherId()));
		//既是组织管理者
		if (userId.equals(publisher.getUserId())) {
			appList.add(createNewApp("", languageChange.getValueByUserId("menu.p.managesub", userId), languageChange.getUrl(URL_PIPER + "/web/home", userId) + "&publisherId=" + publisher.getPublisherId()));
			appList.add(createNewApp("", languageChange.getValueByUserId("menu.p.accountupload", userId), languageChange.getUrl(URL_PIPER + "/h5/help/upload", userId) + "&publisherId=" + publisher.getPublisherId()));
		}

		return appList;
	}


	private Map<String, Object> createNewApp(String key, String title, String url) {
		List<String> keys1 = CollectionUtil.fastList("key", "title", "url");
		List<Object> app11 = CollectionUtil.fastList(key, title, url);
		return CollectionUtil.fastMap(keys1, app11);
	}

	private List<Map<String, Object>> appList(String userId, Publisher publisher) {

		List<Map<String, Object>> appList = new ArrayList<>();

		appList.add(createApp("", languageChange.getValueByUserId("menu.p.history", userId), languageChange.getUrl(URL_PIPER + "/web/history-list", userId) + "&publisherId=" + publisher.getPublisherId()));
		//既是组织管理者
		if (userId.equals(publisher.getUserId())) {
			appList.add(createApp("", languageChange.getValueByUserId("menu.p.managesub", userId), languageChange.getUrl(URL_PIPER + "/web/home", userId) + "&publisherId=" + publisher.getPublisherId()));
			appList.add(createApp("", languageChange.getValueByUserId("menu.p.accountupload", userId), languageChange.getUrl(URL_PIPER + "/h5/help/upload", userId) + "&publisherId=" + publisher.getPublisherId()));
		}

		return appList;
	}


	private Map<String, Object> createApp(String iconUrl, String appName, String path) {
		List<String> keys1 = CollectionUtil.fastList("icon", "title", "url");
		List<Object> app11 = CollectionUtil.fastList(iconUrl, appName, path);
		return CollectionUtil.fastMap(keys1, app11);
	}


	
}
