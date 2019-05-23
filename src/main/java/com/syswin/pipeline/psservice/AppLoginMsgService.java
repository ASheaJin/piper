package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.message.ICustomConfig;
import com.syswin.ps.sdk.showType.TextShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author:lhz
 * @date:2019/5/23 14:06
 */
@Service
public class AppLoginMsgService implements ICustomConfig {
	private final static Logger logger = LoggerFactory.getLogger(AppLoginMsgService.class);

	@Override
	public Boolean accept(Integer bodyType, Object content) {

		MsgHeader msgHeader= PsClientKeeper.msgHeader();
		logger.error("accept msgHeader :"+ msgHeader);
		logger.info("accept bodyType :"+ bodyType);

		logger.info("accept content :"+ content);
		return true;
	}

	@Override
	public String process(Object content) {
		logger.info("process content :"+ content);
		MsgHeader msgHeader= PsClientKeeper.msgHeader();
		Map<String, Object> map = new HashMap<>();
		map.put("title", "qiding");
		map.put("imageUrl", "https://www.baidu.com/img/bd_logo1.png");
		map.put("text", "hello");

		List<ActionItem> infoList = Stream.of(new ActionItem("前进", "http://www.baidu.com")
						, new ActionItem("后退", "http://www.google.com")).collect(Collectors.toList());

		TextShow show = new TextShow(1, map, infoList);
		PsClientKeeper.newInstance().sendMsg(msgHeader.getReceiver(), msgHeader.getSender(), show);
		return "1234";
	}

	@Override
	public Boolean callBack() {
		return true;
	}
}
