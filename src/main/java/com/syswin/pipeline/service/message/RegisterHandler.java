package com.syswin.pipeline.service.message;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.psservice.PsServerService;
import com.syswin.pipeline.psservice.response.ResponeResultData;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.service.ps.Card;
import com.syswin.pipeline.service.ps.util.FastJsonUtil;
import com.syswin.pipeline.service.ps.util.StringUtil;
import com.syswin.pipeline.utils.CacheUtil;
import com.syswin.ps.sdk.common.CDTPResponse;
import com.syswin.temail.ps.client.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

		CDTPResponse<String> cdtpResponse = new CDTPResponse<>();
		CDTPResponse<String> result = JSONObject.parseObject(event.getChatMsg().getContent(), cdtpResponse.getClass());

		if ("200".equals(result.getCode())) {
			ResponeResultData r = JSONObject.parseObject(result.getData(), ResponeResultData.class);

			if (r != null) {
				psServerService.activeAccout(r.getTemail(), r.getActiveCode());
			} else {
				throw new BusinessException("RegisterHandler  r为null");
			}
		} else {
			throw new BusinessException("RegisterHandler " + result.getMsg());

		}
	}

}
