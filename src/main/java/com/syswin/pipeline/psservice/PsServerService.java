package com.syswin.pipeline.psservice;

import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.psservice.impl.CallBackRegister;
import com.syswin.pipeline.psservice.response.ActiveResult;
import com.syswin.pipeline.psservice.response.ResponeResultData;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.util.StringUtil;
import com.syswin.pipeline.utils.HttpsUtil;
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
public class PsServerService {

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
	private PSClientService clientService;

	/**
	 * 注册秘邮号：
	 * a.piper给 a.dm 发消息 监听回调消息 获取激活码
	 * 找孟祥超 20190516
	 *
	 * @param temail
	 * @return
	 */
	public ResponeResultData registerAccount(String temail) {
		Map<String, String> attendanceData = new HashMap<>();
		attendanceData.put("temail", temail);
		String requestId = UUID.randomUUID().toString();
		callBackRegister.register(requestId);
		log.info("开始获取参数,temail={}", temail);
		msgSender.sendMsg(commonMsg(client, server, serverPath, attendanceData, requestId));
		CDTPResponse<String> cdtpResponse = new CDTPResponse<>();
		CDTPResponse<String> result = callBackRegister.getResponse(requestId, cdtpResponse.getClass());
		log.info("获取结果,temail={},result={}", temail, result);

		if (StringUtil.isEmpty(result.getData())) {
			throw new BusinessException("调用registerAccount返回数据为null");
		}
		return JSONObject.parseObject(result.getData(), ResponeResultData.class);
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
		Map<String, String> header = new HashMap();
		header.put("Content-Type", "application/x-www-form-urlencoded");

		Map<String, String> entity = new HashMap();
		entity.put("PUBLIC_KEY", clientService.getTemailPublicKey(temail));
		entity.put("TeMail", temail);
		entity.put("ACTIVATION_CODE", code);
		String result = HttpsUtil.sendHttpsPost(actUrl + "/publish/activate", header, entity);
		ActiveResult ar = JSONObject.parseObject(result, ActiveResult.class);
		log.info("ActiveResult:" + ar.toString());
		return "200".equals(ar.getCode());
	}

	public <T> CommonMsg commonMsg(String from, String to, String path, T params, String requestId) {
		String msgId = UUID.randomUUID().toString();
		Header header = psClientService.header(from, to, msgId);
		HandlerParam handlerParam = new HandlerParam(requestId, path, null, new HashMap<>(), null, null, params);
		CommonMsg commonMsg = new CommonMsg(header, sendType, handlerParam);
		log.info("commonMsg:" + commonMsg.toString());
		return commonMsg;
	}


}
