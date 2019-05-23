package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.message.ICustomConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
		logger.info("accept msgHeader :"+ msgHeader);
		logger.info("accept bodyType :"+ bodyType);

		logger.info("accept content :"+ content);
		return true;
	}

	@Override
	public String process(Object content) {
		logger.info("process content :"+ content);
		return "1234";
	}

	@Override
	public Boolean callBack() {
		return true;
	}
}
