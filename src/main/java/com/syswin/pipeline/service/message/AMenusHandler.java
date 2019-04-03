package com.syswin.pipeline.service.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.enums.PermissionEnums;
import com.syswin.pipeline.service.ConsumerService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.util.CollectionUtil;
import com.syswin.pipeline.service.ps.util.FastJsonUtil;
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

	private static final String H5_PUBLISHER_CREATE = "/h5/publisher/create";
	private static final String H5_PUBLISHER_MANAGE = "/h5/publisher/manage";
	private static final String H5_SUBSCRIBE_ADD = "/h5/subcribe/add";
	private static final String H5_SUBSCRIBE_LIST = "/h5/subcribe/list";

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
		myRole = consumerService.getAMenuRole(header.getReceiver());
		String myVersion = consumerService.getUserVersion(header, version, myRole);
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

		List<Map<String, Object>> appList = new ArrayList<>();

		//既是组织管理者，又是个人出版社管理者
		if (PermissionEnums.OrgPerson.name.equals(myRole)) {
			appList.add(createApp("", "我的个人出版社", URL_PIPER + H5_PUBLISHER_MANAGE + "?userId=" + userId));
			appList.add(createApp("", "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp("", "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
			appList.add(createApp("", "管理邮件群发", URL_PIPER + "/web?userId=" + userId));
			sendMessegeService.sendTextmessage("您的身份是 邮件组管理者 和 个人出版社管理者 您可以在a.piper小助手中管理个人出版社、订阅出版社、管理我的订阅、管理邮件组", userId);
		}
		if (PermissionEnums.OnlyOrg.name.equals(myRole)) {
			appList.add(createApp("", "创建个人出版社", URL_PIPER + H5_PUBLISHER_CREATE + "?userId=" + userId));
			appList.add(createApp("", "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp("", "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
			appList.add(createApp("", "管理邮件群发", URL_PIPER + "/web?userId=" + userId));
			sendMessegeService.sendTextmessage("您的身份是 邮件组管理者 ， 您可以在a.piper小助手中创建个人出版社、订阅出版社、管理我的订阅、管理邮件组", userId);
		}
		//个人管理者，订阅者
		if (PermissionEnums.Person.name.equals(myRole)) {
			appList.add(createApp("", "我的出版社", URL_PIPER + H5_PUBLISHER_MANAGE + "?userId=" + userId));
			appList.add(createApp("", "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp("", "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
			sendMessegeService.sendTextmessage("您的身份是 个人出版社管理者 ， 您可以在a.piper小助手中管理个人出版社、订阅出版社、管理我的订阅", userId);

		}
		//游客，订阅者
		if (PermissionEnums.Guest.name.equals(myRole)) {
			appList.add(createApp("", "创建出版社", URL_PIPER + H5_PUBLISHER_CREATE + "?userId=" + userId));
			appList.add(createApp("", "去订阅", URL_PIPER + H5_SUBSCRIBE_ADD + "?userId=" + userId));
			appList.add(createApp("", "我的订阅列表", URL_PIPER + H5_SUBSCRIBE_LIST + "?userId=" + userId));
			sendMessegeService.sendTextmessage("您的身份是 游客 ， 您可以在a.piper小助手中创建个人出版社、订阅出版社、管理我的订阅", userId);

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


}
