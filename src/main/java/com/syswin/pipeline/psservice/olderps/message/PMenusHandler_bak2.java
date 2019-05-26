package com.syswin.pipeline.psservice.olderps.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.service.PiperConsumerService;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.psservice.olderps.ChatMsg;
import com.syswin.pipeline.psservice.olderps.Env;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.utils.CollectionUtil;
import com.syswin.pipeline.utils.FastJsonUtil;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.pipeline.utils.SwithUtil;
import com.syswin.sub.api.PublisherService;
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
public class PMenusHandler_bak2 implements EventHandler<MessageEvent> {

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
		int version = 0;
		Env appEnv = null;
		try {
			String content = event.getChatMsg().getContent();
			version = Integer.parseInt(FastJsonUtil.parseObject(content).getString("version"));
			String envValue = FastJsonUtil.parseObject(content).getString("env");
			appEnv = JacksonJsonUtil.fromJson(envValue, Env.class);
		} catch (Exception e) {
			logger.info("处理消息中的version字段失败", e);
		}
		List keyList = new ArrayList();
		List valueList = new ArrayList();

		keyList.add("type");
		valueList.add(0);

		keyList.add("text");
		valueList.add("菜单测试");


		//新版本菜单
		if ((SwithUtil.tt & 1) == 1) {
			keyList.add("helperConfig");
			valueList.add(helperConfig());
		}
		//新版本菜单
		if ((SwithUtil.tt & 2) == 2) {
			keyList.add("inputConfig");
			valueList.add(inputConfig());
		}
		if ((SwithUtil.tt & 4) == 4) {
			keyList.add("menuConfig");
			valueList.add(menuConfig());
		}
		Map<String, Object> replyMsgObject = null;
		replyMsgObject = CollectionUtil.fastMap(keyList, valueList);
		replyMsgObject.put("version", version + 1);
		ChatMsg msg = new ChatMsg(header.getSender(), header.getReceiver(),
						UUID.randomUUID().toString(), replyMsgObject);
		msg.setBody_type(GET_MESSAGE_INFO_FLAG);

		psClientService.sendChatMessage(msg, header.getReceiver(), header.getReceiverPK(), header.getSender(), header.getSenderPK());
	}

	private List<Map<String, Object>> helperConfig() {

		List<Map<String, Object>> appList = new ArrayList<>();
		appList.add(createHelperConfig("input_write", "", "", ""));
		appList.add(createHelperConfig("input_at", "", "", ""));
		appList.add(createHelperConfig("", "", "", "http://www.baidu.com"));

		return appList;
	}

	private Map<String, Object> createHelperConfig(String key, String iconUrl, String appName, String path) {
		List<String> keys1 = CollectionUtil.fastList("key", "icon", "title", "url");
		List<Object> app11 = CollectionUtil.fastList(key, iconUrl, appName, path);
		return CollectionUtil.fastMap(keys1, app11);
	}


	private List<Map<String, Object>> inputConfig() {

		List<Map<String, Object>> appList = new ArrayList<>();
		appList.add(createHelperConfig("input_text&emoji", "", "", ""));
		appList.add(createHelperConfig("input_photo", "", "", ""));

		return appList;
	}

	private List<Map<String, Object>> menuConfig() {
		List<Map<String, Object>> appList = new ArrayList<>();
		appList.add(createMenuConfig("用户论坛", "", new ArrayList(), "1"));
		List<Map<String, Object>> subList = new ArrayList<>();
		subList.add(getSubMenu("热门文章", "http://www.bing.com", ""));
		subList.add(getSubMenu("历史消息", "http://www.zhihu.com", ""));
		appList.add(createMenuConfig("职场干货", "", subList, ""));

		appList.add(createMenuConfig("历史消息", "http://www.bing.com", new ArrayList(), ""));
		return appList;
	}

	private Map<String, Object> createMenuConfig(String title, String url, List subMenu, String mainPage) {
		List<String> keys1 = CollectionUtil.fastList("title", "url", "subMenu", "mainPage");
		List<Object> app11 = CollectionUtil.fastList(title, url, subMenu, mainPage);
		return CollectionUtil.fastMap(keys1, app11);
	}

	private Map<String, Object> getSubMenu(String title, String url, String mainPage) {
		List<String> keys1 = CollectionUtil.fastList("title", "url", "mainPage");
		List<Object> app11 = CollectionUtil.fastList(title, url, mainPage);
		return CollectionUtil.fastMap(keys1, app11);
	}

}
