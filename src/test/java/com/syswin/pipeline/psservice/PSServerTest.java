package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.common.CommonMsg;
import com.syswin.ps.sdk.common.HandlerParam;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.service.CrossDomainService;
import com.syswin.ps.sdk.service.SingleChatService;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.ps.sdk.utils.FastJsonUtil;
import com.syswin.temail.ps.client.Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author:lhz
 * @date:2019/6/3 17:10
 */

public class PSServerTest extends  PsAppExampleApplicationTests {

	@Autowired
	CrossDomainService crossDomainService;

	private String datagson = "{\"command\":3,\"commandSpace\":3,\"dataEncryptionMethod\":4,\"extraData\":\"{\\\"from\\\":\\\"luohongzhou@t.email\\\",\\\"msgId\\\":\\\"f-bc7a-2b08a315501c\\\",\\\"parentMsgId\\\":\\\"\\\",\\\"storeType\\\":1,\\\"to\\\":\\\"a.piper@t.email\\\",\\\"type\\\":0}\",\"packetId\":\"8a326c38-ce37-4904-91d2-eb5144ad5d59\",\"receiver\":\"luohongzhou@t.email\",\"receiverPK\":\"MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAnWNMGw_d4xauYnCB73fvjXKRbwVUISgv97256fvYG3AOnMH8H_g3JF9ymliBaHJfKXbbC9AR5O0lsBSXo0D9dXEA3AVphpKBTDIZ8LIxTR12rpMnZiWxP_6t2ixFtKzEoMtARxk45P0GtUsWRsgfDK0je0rTvOhAEIB4O42UOatjhVo\",\"sender\":\"a.piper@t.email\",\"senderPK\":\"\"MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc\",\"signatureAlgorithm\":0,\"targetAddress\":\"msgseal.t.email:8099\",\"timestamp\":1550468392}";
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

	@Test
	public void sendGetHelloWord() throws InterruptedException {
		String id = UUID.randomUUID().toString();
		String path = "aaa/bbb";
		Map<String, String> params = new HashMap<>();
		params.put("userId", "11111");
		params.put("iodatetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
		params.put("jqh", "111");
		params.put("operator", "a.door_syswin");
		msgSender.sendMsg(commonMsg(id, path, params));


	}

	@Test
	public void sendPostFormHelloWord() {
		String path = "postHello/qiding";
		sendMsg(path);
	}

	@Test
	public void sendPostJsonHelloWord() {
		String path = "postJsonHello/qiding";
		sendMsg(path);
	}


	@Test
	public void sendPacketTestMsg() {
		PsClientKeeper psClientKeeper=PsClientKeeper.newInstance();
		psClientKeeper.sendMsg(sender, receiver, new TextShow("qiding"));
	}
	
}
