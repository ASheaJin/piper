package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.UserIdParam;
import com.syswin.pipeline.psservice.olderps.ChatMsg;
import com.syswin.pipeline.psservice.psserver.SendMsgService;
import com.syswin.pipeline.psservice.psserver.bean.SendMsgEntity;
import com.syswin.pipeline.utils.CollectionUtil;
import com.syswin.ps.sdk.common.CommonMsg;
import com.syswin.ps.sdk.common.HandlerParam;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.common.PSResult;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.service.CrossDomainService;
import com.syswin.ps.sdk.service.KmsService;
import com.syswin.ps.sdk.service.SingleChatService;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.ps.sdk.utils.FastJsonUtil;
import com.syswin.temail.ps.client.Header;
import com.syswin.temail.ps.client.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2018/12/18.
 */
@CrossOrigin
@RestController
@RequestMapping("/psserver")
@Api(value = "psserver", tags = "psserver")
public class PSSeverController {
	private static final Logger logger = LoggerFactory.getLogger(PSSeverController.class);

	@Autowired
	SendMsgService pendMsgServer;


	@GetMapping("send")
	@ApiOperation(
					value = "發送消息"
	)
	public Message send() {
		ChatMsg chatMsg = new ChatMsg("1112112",1, "a.piper@t.email", "luohongzhou@t.email");
		SendMsgEntity sendMsgEntity = new SendMsgEntity();
		sendMsgEntity.setExtrData(null);
		sendMsgEntity.setMsg(chatMsg);
		sendMsgEntity.setReceiver("luohongzhou@t.email");
		sendMsgEntity.setReceiverPK("MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc");
		sendMsgEntity.setSender("a.piper@t.email");
		sendMsgEntity.setSenderPK("MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc");
		Message message =pendMsgServer.sendChatTestMessage(sendMsgEntity,(short)1);
		logger.info("send Message"+message);
		return message;
	}



	@GetMapping("aaa/bbb")
	@PSResult()
	public List unlockRecord( String userId) throws Exception {
		MsgHeader header = PsClientKeeper.msgHeader();
		logger.info("header={},path={},data={}", header, "unlock/record", userId);

		List respList = new ArrayList<>();

//			List<CommunityLock> communityLockList = doorClientService.getUserLockByIds(ids);
//			Map<Long, CommunityLock> communityLockMap = communityLockList.stream()
//					.collect(Collectors.toMap(CommunityLock::getLockId, Function.identity(), (key1, key2) -> key2));


		return respList;
	}



	@Autowired
	CrossDomainService crossDomainService;

	public String datagson = "{" +
			"\"command\": 6," +
			"\"commandSpace\": 1," +
			"\"dataEncryptionMethod\": 4," +
			"\"extraData\": \"{\\\"from\\\":\\\"luohongzhou@t.email\\\",\\\"msgId\\\":\\\"f-bc7a-2b08a315501c\\\",\\\"parentMsgId\\\":\\\"\\\",\\\"storeType\\\":1,\\\"to\\\":\\\"a.piper@t.email\\\",\\\"type\\\":0}\"," +
			"\"packetId\": \"8a326c38-ce37-4904-91d2-eb5144ad5d59\"," +
			"\"receiver\": \"luohongzhou@t.email\"," +
			"\"receiverPK\": \"MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAnWNMGw_d4xauYnCB73fvjXKRbwVUISgv97256fvYG3AOnMH8H_g3JF9ymliBaHJfKXbbC9AR5O0lsBSXo0D9dXEA3AVphpKBTDIZ8LIxTR12rpMnZiWxP_6t2ixFtKzEoMtARxk45P0GtUsWRsgfDK0je0rTvOhAEIB4O42UOatjhVo\"," +
			"\"sender\": \"a.piper@t.email\"," +
			"\"senderPK\": \"MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU - j9nmLlU2zgfVXkCHk7KCRAc \"," +
			"\"signatureAlgorithm \": 0," +
			"\"targetAddress\": \"\"," +
			"\"msgseal.t.email\": 8099," +
			"\"timestamp \": 1550468392" +
			"}";
	//private String deegson="{\"command\":1,\"commandSpace\":1,\"dataEncryptionMethod\":4,\"extraData\":\"{\\\"from\\\":\\\"a.inyourstyle@t.email\\\",\\\"msgId\\\":\\\"bc583b3d-8cf9-43a1-96fa-d84a9e16-6757-4c8ca6dfbae2c43\\\",\\\"parentMsgId\\\":\\\"f84a9e16-6757-4c8c-bc7a-2b08a315501c\\\",\\\"storeType\\\":1,\\\"to\\\":\\\"a.inyourstyle@t.email\\\",\\\"type\\\":0}\",\"packetId\":\"5375f8f6-5801-4fd4-b265-aad8aaa2df0d\",\"receiver\":\"a.inyourstyle@t.email\",\"receiverPK\":\"MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAuTOaYy3tBvnvim2HLp0r5oJBgChYaBs-6BcaQ4S-V0FMdjV_S39s9mRGKs1uSb1NyUd8elhyw56kM4SmMYa9Y54BTCA0DAAjaFJNLA-uHQOt_A-Xx9tbEHPozgeEoSM0Vid_5ZOZtInVyeTDGDemuOrLEgYlaUdUIuMkgorf6s4MsUs\",\"sender\":\"a.inyourstyle@t.email\",\"senderPK\":\"MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAuTOaYy3tBvnvim2HLp0r5oJBgChYaBs-6BcaQ4S-V0FMdjV_S39s9mRGKs1uSb1NyUd8elhyw56kM4SmMYa9Y54BTCA0DAAjaFJNLA-uHQOt_A-Xx9tbEHPozgeEoSM0Vid_5ZOZtInVyeTDGDemuOrLEgYlaUdUIuMkgorf6s4MsUs\",\"signatureAlgorithm\":0,\"targetAddress\":\"msgseal.t.email:8099\",\"timestamp\":1550474184078}";


	private String sender = "a.piper@t.email";
	private String senderPK = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAuTOaYy3tBvnvim2HLp0r5oJBgChYaBs-6BcaQ4S-V0FMdjV_S39s9mRGKs1uSb1NyUd8elhyw56kM4SmMYa9Y54BTCA0DAAjaFJNLA-uHQOt_A-Xx9tbEHPozgeEoSM0Vid_5ZOZtInVyeTDGDemuOrLEgYlaUdUIuMkgorf6s4MsUs";

	private String receiver = "luohongzhou@t.email";


	@Autowired
	SingleChatService msgSender;

	//	@Test
	public Header getHeader() {
		//header
		Header header = FastJsonUtil.parseObject(datagson, Header.class);
		header.setSender(sender);
		header.setSenderPK(crossDomainService.publishKey(sender));

		header.setReceiver(receiver);
		header.setReceiverPK(crossDomainService.publishKey(receiver));
		header.setPacketId(UUID.randomUUID().toString());
		return header;
	}

	public CommonMsg commonMsg(String id, String path, Map<String, String> params) {
		Header header = getHeader();
		HandlerParam handlerParam = new HandlerParam(id, path, null, new HashMap<>(), null, null, params);
		CommonMsg commonMsg = new CommonMsg(header, 801, handlerParam);
		return commonMsg;
	}

	public void sendMsg(String path) {
		String id = UUID.randomUUID().toString();
		Map<String, String> params = new HashMap<>();
		params.put("name", "qiding");
		params.put("age", "100");
		msgSender.sendMsg(commonMsg(id, path, params));
	}
	@Autowired
	KmsService kmsService;

	@GetMapping("testhello")
	public void sendGetHelloWord() throws InterruptedException {

		kmsService.sign("a.piper@t.email","aaaaa");
		String id = UUID.randomUUID().toString();
		String path = "psserver/aaa/bbb";
		Map<String, String> params = new HashMap<>();
		params.put("userId", "11111");
		params.put("iodatetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
		params.put("jqh", "111");
		params.put("operator", "a.door_syswin");
		msgSender.sendMsg(commonMsg(id, path, params));


	}

	public void sendPostFormHelloWord() {
		String path = "postHello/qiding";
		sendMsg(path);
	}

	public void sendPostJsonHelloWord() {
		String path = "postJsonHello/qiding";
		sendMsg(path);
	}


	public void sendPacketTestMsg() {
		PsClientKeeper psClientKeeper=PsClientKeeper.newInstance();
		psClientKeeper.sendMsg(sender, receiver, new TextShow("qiding"));
	}

	public static void main(String[] args) {
		FastJsonUtil.parseObject(new PSSeverController().datagson);
	}

}
