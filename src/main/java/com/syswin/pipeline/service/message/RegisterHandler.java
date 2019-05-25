package com.syswin.pipeline.service.message;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.psservice.PsServerService;
import com.syswin.pipeline.psservice.response.ResponeResultData;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by 115477 on 2019/1/23.
 */
@Component
public class RegisterHandler implements EventHandler<MessageEvent> {

	@Lazy
	@Autowired
	private PsServerService psServerService;

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) {


		String contentStr = event.getChatMsg().getContent();
		if (StringUtil.isEmpty(contentStr)) {
			throw new BusinessException("event.getChatMsg().getContent() is null");
		}
		ResponeResultData r = parse(contentStr);
		psServerService.activeAccout(r.getTemail(), r.getActiveCode());
	}

	ResponeResultData parse(String content) {
		JSONObject con = JSONObject.parseObject(content);
		String code = JSONObject.parseObject(con.getString("data")).getString("code");
		ResponeResultData data = null;
		if ("200".equals(code)) {
			data = JSONObject.parseObject(JSONObject.parseObject(con.getString("data")).getString("data"), ResponeResultData.class);
		} else {
			throw new BusinessException("parse err code" + code);
		}
		return data;
	}

	public static void main(String[] args) {
		String content = "{\"data\":{\"code\":200,\"data\":{\"activeCode\":\"316270\",\"temail\":\"p.luojh1@msgseal.com\",\"type\":4},\"message\":\"成功\"},\"id\":\"d6e73771-da96-4d65-906c-ec9148b250ce\"}";
		System.out.println(new RegisterHandler().parse(content));
	}
}
