package com.syswin.pipeline.service.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.db.repository.ConsumerRepository;
import com.syswin.pipeline.enums.PermissionEnums;
import com.syswin.pipeline.service.ConsumerService;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.util.CollectionUtil;
import com.syswin.pipeline.service.ps.util.FastJsonUtil;
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
public class PMenusHandler implements EventHandler<MessageEvent> {

	@Value("${url.piper}")
	private String URL_PIPER;

	@Autowired
	ConsumerService consumerService;

	@Autowired
	private PublisherService publisherService;

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
	private final static Logger logger = LoggerFactory.getLogger(PMenusHandler.class);
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
		if (from.equals(header.getSender())) {
			return;
		}
		Publisher publisher = publisherService.getPubLisherByPublishTmail(header.getSender(), null);
		//如果不是出版社
		if (publisher == null) {
			return;
		}
		String version = PermissionEnums.Guest.name;
		try {
			String content = event.getChatMsg().getContent();
			version = FastJsonUtil.parseObject(content).getString("version");

		} catch (Exception e) {
			logger.info("处理消息中的version字段失败", e);
		}

		myRole = consumerService.getPiperMenuRole(header.getReceiver(), header.getSender());
		String myVersion = consumerService.getUserVersion(header, version, myRole);
		if (version.equals(myVersion)) {
			//版本号相同，不做加载
			return;
		}
		sendTipsMessage(publisher, header.getReceiver());

		List keyList = new ArrayList();
		List valueList = new ArrayList();

		keyList.add("type");
		valueList.add(0);

		keyList.add("text");
		valueList.add("请使用智能小助手");

		keyList.add("features");
		valueList.add(appFeaturesList());


		Map<String, Object> replyMsgObject = null;
		keyList.add("shortcuts");
//判断当前用户是读者还是作者

		if (header.getReceiver().equals(publisher.getUserId())) {

			valueList.add(appList(publisher.getPtype(), publisher.getUserId(), publisher.getPublisherId()));
		} else {
			valueList.add(new ArrayList<>());
		}
		replyMsgObject = CollectionUtil.fastMap(keyList, valueList);
		replyMsgObject.put("version", myVersion);
		consumerService.updateUserVersion(header, myVersion, myRole);
		ChatMsg msg = new ChatMsg(header.getSender(), header.getReceiver(),
						UUID.randomUUID().toString(), replyMsgObject);
		msg.setBody_type(GET_MESSAGE_INFO_FLAG);

		psClientService.sendChatMessage(msg, header.getReceiver(), header.getReceiverPK(), header.getSender(), header.getSenderPK());
	}

	//加载默认按钮 http://wiki.syswin.com/pages/viewpage.action?pageId=33708676
	private List<Map<String, Object>> appFeaturesList() {
		List<Map<String, Object>> appList = new ArrayList<>();
//		appList.add(createApp("", "#@" + 13, ""));
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

	void sendTipsMessage(Publisher publisher, String userId) {
		if (publisher.getPtype().equals(PublisherTypeEnums.organize)) {

			if (userId.equals(publisher.getUserId())) {
				sendMessegeService.sendTextmessage("您是 该出版社管理者 您可以在此向所有订阅者发消息、文件、图片等", userId, 0, publisher.getPtemail());
			} else {
				Subscription subscription = subSubscriptionService.getSub(userId, publisher.getPublisherId());
				if (subscription == null) {
					sendMessegeService.sendTextmessage("您尚未订阅该组织邮件组 您可联系管理员加入", userId, 0, publisher.getPtemail());

				} else {
					sendMessegeService.sendTextmessage("邮件组管理员在此给您发消息", userId, 0, publisher.getPtemail());
				}
			}
		} else {
			if (userId.equals(publisher.getUserId())) {
				sendMessegeService.sendTextmessage("您是 该出版社管理者 您可以在此向所有订阅者发消息、文件、图片等", userId, 0, publisher.getPtemail());
			} else {
				Subscription subscription = subSubscriptionService.getSub(userId, publisher.getPublisherId());
				if (subscription == null) {
					sendMessegeService.sendTextmessage("您尚未订阅该出版社 发送 《订阅》 订阅该出版社", userId, 0, publisher.getPtemail());

				} else {
					sendMessegeService.sendTextmessage("您已订阅该出版社 发送 《取消订阅》 取消订阅该出版社", userId, 0, publisher.getPtemail());
				}
			}
		}
	}

	private List<Map<String, Object>> appList(PublisherTypeEnums ptype, String userId, String publiserId) {

		List<Map<String, Object>> appList = new ArrayList<>();

		//既是组织管理者
		if (PublisherTypeEnums.organize.equals(ptype)) {
			appList.add(createApp("", "管理订阅人", URL_PIPER + "/web/home?userId=" + userId + "&publisherId=" + publiserId));
			appList.add(createApp("", "账号上传说明", URL_PIPER + "/h5/help/upload?userId=" + userId + "&publisherId=" + publiserId));
		}

		return appList;
	}


	private Map<String, Object> createApp(String iconUrl, String appName, String path) {
		List<String> keys1 = CollectionUtil.fastList("icon", "title", "url");
		List<Object> app11 = CollectionUtil.fastList(iconUrl, appName, path);
		return CollectionUtil.fastMap(keys1, app11);
	}

}
