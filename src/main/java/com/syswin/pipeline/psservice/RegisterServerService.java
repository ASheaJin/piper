package com.syswin.pipeline.psservice;

import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.psservice.impl.CallBackRegister;
import com.syswin.pipeline.psservice.response.ResponeResultData;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.psservice.olderps.message.MessageEvent;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.admin.service.impl.PSAccountService;
import com.syswin.ps.sdk.common.CDTPResponse;
import com.syswin.ps.sdk.common.CommonMsg;
import com.syswin.ps.sdk.common.HandlerParam;
import com.syswin.ps.sdk.service.PsClientService;
import com.syswin.ps.sdk.service.SingleChatService;
import com.syswin.temail.ps.client.Header;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class RegisterServerService {

	public static int sendType = 801;
	@Value("${psserver.path}")
	private String serverPath;
	@Value("${app.pipeline.userId}")
	private String client;
	@Value("${psserver.server.tmail}")
	private String server;

	@Value("${url.temail-auth.server}")
	private String actUrl;

	@Autowired
	PsClientService psClientService;

	@Autowired
	CallBackRegister callBackRegister;
	@Autowired
	SingleChatService msgSender;


	@Autowired
	PSAccountService psAccountService;

	/**
	 * 注册秘邮号：
	 * a.piper给 a.dm 发消息 监听回调消息 获取激活码
	 * 找孟祥超 20190516
	 *
	 * @param temail
	 * @return
	 */
	public String registerAccount(String temail) {
		return registerAccount(temail, server);
	}

	/**
	 * 注册秘邮号：
	 * a.piper给 a.dm 发消息 监听回调消息 获取激活码
	 * 找孟祥超 20190516
	 *
	 * @param temail
	 * @return
	 */
	public String registerAccount(String temail, String server) {
		Map<String, String> attendanceData = new HashMap<>();
		attendanceData.put("temail", temail);
		String requestId = UUID.randomUUID().toString();

		log.info("开始获取参数,temail={}", temail);
		msgSender.sendMsg(commonMsg(client, server, serverPath, attendanceData, requestId));

		callBackRegister.register(requestId);
		CDTPResponse<String> cdtpResponse = new CDTPResponse<>();
		CDTPResponse<String> result = callBackRegister.getResponse(requestId, cdtpResponse.getClass());
		log.info("获取结果,temail={},result={}", temail, result);

		if (StringUtil.isEmpty(result.getData())) {
			throw new BusinessException("调用registerAccount返回数据为null");
		}
		return null;
	}

	/**
	 * 激活邮箱
	 * 找郭梦男 20190516
	 *
	 * @param temail
	 * @param code
	 * @return
	 */
	public boolean activeAccout(String temail, String code) {

		return psAccountService.active(temail, code);
	}

	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) {


		String contentStr = event.getChatMsg().getContent();
		if (StringUtil.isEmpty(contentStr)) {
			throw new BusinessException("event.getChatMsg().getContent() is null");
		}
		ResponeResultData r = parse(contentStr);
		activeAccout(r.getTemail(), r.getActiveCode());
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


	public <T> CommonMsg commonMsg(String from, String to, String path, T params, String requestId) {
		String msgId = UUID.randomUUID().toString();
		Header header = psClientService.header(from, to, msgId);
		log.info("header:" + header.toString());
		HandlerParam handlerParam = new HandlerParam(requestId, path, null, new HashMap<>(), null, null, params);
		CommonMsg commonMsg = new CommonMsg(header, sendType, handlerParam);
		log.info("commonMsg:" + commonMsg.toString());
		return commonMsg;
	}

}
