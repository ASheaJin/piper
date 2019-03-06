package com.syswin.pipeline.service.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.db.model.Consumer;
import com.syswin.pipeline.db.repository.ConsumerRepository;
import com.syswin.pipeline.enums.PermissionEnums;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.util.CollectionUtil;
import com.syswin.pipeline.service.ps.util.FastJsonUtil;
import com.syswin.pipeline.utils.PermissionUtil;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
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
public class AMenusHandler implements EventHandler<MessageEvent> {

	@Value("${url.piper}")
	private String URL_PIPER;

	@Autowired
	private AdminService adminService;

	@Autowired
	private PublisherService publisherService;

	private static final String H5_PUBLISHER_CREATE = "/h5/publisher/create";
	private static final String H5_PUBLISHER_MANAGE = "/h5/publisher/manage";
	private static final String H5_SUBSCRIBE_ADD = "/h5/subcribe/add";
	private static final String H5_SUBSCRIBE_LIST = "/h5/subcribe/list";
	private static final String H5_ACCOUNT_INFO = "/h5/account/info";
	private static final String H5_HELP_INFO = "/h5/help/info";
	private static final String ICON_PUBLISHER_MANAGE = "http://jco-app.cn/html/icon/file.png";
	private static final String ICON_SUBSCRIBE_ADD = "http://jco-app.cn/html/icon/tosub.png";
	private static final String ICON_SUBSCRIBE_LIST = "http://jco-app.cn/html/icon/sublist.png";
	private static final String ICON_ACCOUNT_INFO = "http://jco-app.cn/html/icon/mine.png";

	@Lazy
	@Autowired
	private PSClientService psClientService;

	@Autowired
	ConsumerRepository consumerRepository;
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
		try {
			String content = event.getChatMsg().getContent();
			version = FastJsonUtil.parseObject(content).getString("version");

		} catch (Exception e) {
			logger.info("处理消息中的version字段失败", e);
		}
		myRole = getPermission(header);
		String myVersion = getUserLogVersion(header, version);
		if (version.equals(myVersion)) {
			//版本号相同，不做加载
			return;
		}

		List keyList = new ArrayList();
		List valueList = new ArrayList();

		keyList.add("type");
		valueList.add(0);

		keyList.add("text");
		valueList.add("请使用智能小助手");

//		keyList.add("features");
//		//判断当前用户是读者还是作者
//		valueList.add(appFeaturesList());

		keyList.add("shortcuts");
		//判断当前用户是读者还是作者
		Map<String, Object> replyMsgObject = null;
		valueList.add(appList(header.getReceiver()));

		replyMsgObject = CollectionUtil.fastMap(keyList, valueList);
		replyMsgObject.put("version", myVersion);
		updateUserLogVersion(header.getReceiver(), myVersion);
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

//	private List<Map<String, Object>> appHelp() {
//
//		List<Map<String, Object>> appList = new ArrayList<>();
//
//		appList.add(createApp(ICON_ACCOUNT_INFO, "帮助", URL_PIPER + H5_HELP_INFO));
//
//		return appList;
//	}

	private List<Map<String, Object>> appList(String userId) {

		List<Map<String, Object>> appList = new ArrayList<>();

		//既是组织管理者，又是个人传输管理者
		if (PermissionEnums.OrgPerson.name.equals(myRole)) {
			appList.add(createApp(ICON_ACCOUNT_INFO, "我的出版社", URL_PIPER + H5_PUBLISHER_MANAGE + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_ADD, "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_LIST, "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_LIST, "管理邮件群发", URL_PIPER + "/web?userId=" + userId));
		}
		if (PermissionEnums.OnlyOrg.name.equals(myRole)) {
			appList.add(createApp(ICON_ACCOUNT_INFO, "创建出版社", URL_PIPER + H5_PUBLISHER_CREATE + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_ADD, "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_LIST, "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_LIST, "管理邮件群发", URL_PIPER + "/web?userId=" + userId));
		}
		//个人管理者，订阅者
		if (PermissionEnums.Person.name.equals(myRole)) {
			appList.add(createApp(ICON_ACCOUNT_INFO, "我的出版社", URL_PIPER + H5_PUBLISHER_MANAGE + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_ADD, "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_LIST, "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
		}
		//游客，订阅者
		if (PermissionEnums.Guest.name.equals(myRole)) {
			appList.add(createApp(ICON_ACCOUNT_INFO, "创建出版社", URL_PIPER + H5_PUBLISHER_CREATE + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_ADD, "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp(ICON_SUBSCRIBE_LIST, "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
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

	private String getUserLogVersion(Header header, String version) {
		Consumer consumer = consumerRepository.selectById(header.getReceiver());
		if (consumer == null) {
			consumer = new Consumer();
			consumer.setCurversion(version);
			consumer.setPubkey(header.getReceiverPK());
			consumer.setUserId(header.getReceiver());
			consumer.setRole(myRole);
			consumerRepository.insert(consumer);
		} else {
			if (version.equals(consumer.getCurversion()) && myRole.equals(consumer.getRole())) {
				return version;
			}
		}

		return String.valueOf(Integer.parseInt(version) + 1);
	}

	private void updateUserLogVersion(String userId, String version) {
		Consumer consumer = new Consumer();
		consumer.setCurversion(version);
		consumer.setUserId(userId);
		consumer.setRole(myRole);
		consumerRepository.update(consumer);
	}

	private String getPermission(Header header) {
		Publisher publisher = publisherService.getPubLisherByuserId(header.getReceiver(), PublisherTypeEnums.person);
		//判断version字段，是否需要发送菜单
		//不是个人出版社
		int isPerson = publisher != null ? 1 : 0;
		Admin admin = adminService.getAdmin(header.getReceiver());
		//判断是不是组织出版社
		int isOrg = admin != null ? 1 : 0;

		String per = "00" + String.valueOf(isOrg) + String.valueOf(isPerson);

		return PermissionUtil.getMyVersion(per);
	}
}
