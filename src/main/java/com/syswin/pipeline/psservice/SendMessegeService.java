package com.syswin.pipeline.psservice;

import com.google.gson.Gson;
import com.syswin.pipeline.psservice.olderps.Card;
import com.syswin.pipeline.psservice.olderps.ChatMsg;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.sop.CheckParamNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author:lhz
 * @date:2019/1/18 11:12
 */
@Service
public class SendMessegeService {
	@Autowired
	private PSClientService psClientService;

	@Value("${app.ps-app-sdk.user-id}")
	private String from;

	private final static Logger logger = LoggerFactory.getLogger(SendMessegeService.class);

	/**
	 * 发送文本的消息
	 *
	 * @param content 内容
	 * @param to      发给谁
	 */
	@CheckParamNull(params = "to")
	public void sendTextMessage(String content, String to) {
		sendTextMessage(content, to, 0);
	}

	/**
	 * 发送文本的消息
	 *
	 * @param content   内容
	 * @param to        发给谁
	 * @param deloytime 延时时间
	 */

	@CheckParamNull(params = "content,to")
	public void sendTextMessage(String content, String to, int deloytime) {
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("text", content);
		String contentJson = new Gson().toJson(contentMap);
		ChatMsg chatMsg = new ChatMsg(contentJson, 1, from, to, deloytime);
		String publickey = psClientService.getTemailPublicKey(to);
		psClientService.sendChatMessage(chatMsg, to, publickey);
	}

	/**
	 * 发送文本的消息
	 *
	 * @param content   内容
	 * @param to        发给谁
	 * @param deloytime 延时时间
	 * @param from      谁发
	 */

	@CheckParamNull(params = "content,to,from")
	public void sendTextMessage(String content, String to, int deloytime, String from) {
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("text", content);
		String contentJson = new Gson().toJson(contentMap);
		ChatMsg chatMsg = new ChatMsg(contentJson, 1, from, to, deloytime);
		String publickey = psClientService.getTemailPublicKey(to);
		String senderPK = psClientService.getTemailPublicKey(from);
		psClientService.sendChatMessage(chatMsg, to, publickey, from, senderPK);
	}

	@CheckParamNull(params = "content,to,from")
	public void sendTextMessage(String content, String to, String from) {
		sendTextMessage(content, to, 0, from);
	}

	/**
	 * 发送文本的消息
	 *
	 * @param to   发给谁
	 * @param from 谁发
	 */
	@CheckParamNull(params = "name,to,from")
	public void sendCard(String from, String to, String name) {
		this.sendCard(from, to, name, null);
	}

	@CheckParamNull(params = "name,to,from")
	public void sendCard(String from, String to, String name, String imgUrl) {

		if (from.contains("p.")) {
			imgUrl = imgUrl == null ? purl : imgUrl;
		}
		if (from.contains("a.piper")) {
			imgUrl = imgUrl == null ? url : imgUrl;
		}
		ChatMsg msg = new ChatMsg(from, to,
						UUID.randomUUID().toString(), cardContent(from, name, imgUrl));

		msg.setBody_type(4);
		String publickey = psClientService.getTemailPublicKey(from);
		String senderPK = psClientService.getTemailPublicKey(to);
		psClientService.sendCardMessage(msg, from, publickey, to, senderPK);
	}

	//名片消息
	@Value("${app.pipeline.imgUrl}")
	private String url;
	@Value("${p.pipeline.imgUrl}")
	private String purl;


	public static final String VCARD_TEMPLATE = "BEGIN:VCARD\r\nPHOTO:%s\r\nVERSION:3.0\r\nN:%s\r\nEMAIL:%s\r\nEND:VCARD";

	private Card cardContent(String temail, String name, String imgUrl) {
		String vcard = String.format(VCARD_TEMPLATE, imgUrl, name, temail);
		Card card = new Card();
		card.setNick(name);
		card.setFeedId(vcard);
		card.setUrl(imgUrl);
		card.setDesc(temail);
		return card;
	}

	/**
	 * 发送其他类型的消息
	 *
	 * @param content 内容
	 * @param content 内容
	 * @param to      发给谁
	 */
//	private void sendOtherMessage(String content, int bodyType, String to) {
//		//延时1秒钟发。
//		ChatMsg chatMsg = new ChatMsg(content, bodyType, from, to, 1000);
//		String publickey = psClientService.getTemailPublicKey(to);
//		psClientService.sendChatMessage(chatMsg, to, publickey);
//	}
	@CheckParamNull(params = "content,to,from")
	public void sendOtherMessage(String content, int bodyType, String to, String from) {


		//延时1秒钟发。
		logger.info("Thread.currentThread().getName()1--------" + Thread.currentThread().getName());
		ChatMsg chatMsg = new ChatMsg(content, bodyType, from, to, 1000);
		String publickey = psClientService.getTemailPublicKey(to);
		String senderPK = psClientService.getTemailPublicKey(from);

		if (StringUtils.isEmpty(publickey)) {
			throw new IllegalArgumentException("to 的 publicKey不能为空 " + to);
		}
		if (StringUtils.isEmpty(senderPK)) {
			throw new IllegalArgumentException("from 的 publicKey不能为空" + from);
		}
		psClientService.sendChatMessage(chatMsg, to, publickey, from, senderPK);
	}

	@Deprecated
	public void sendOthermessageTest(String content, int bodyType, String to, String from) {

		//延时1秒钟发。
		logger.info("Thread.currentThread().getName()1--------" + Thread.currentThread().getName());
		ChatMsg chatMsg = new ChatMsg(content, bodyType, from, to, 1000);
		String publickey = psClientService.getTemailPublicKey(to);
		String senderPK = psClientService.getTemailPublicKey(from);

		if (StringUtils.isEmpty(publickey)) {
			throw new IllegalArgumentException("to 的 publicKey不能为空" + to);
		}
		if (StringUtils.isEmpty(senderPK)) {
			throw new IllegalArgumentException("from 的 publicKey不能为空" + from);
		}
		psClientService.sendChatMessage(chatMsg, to, publickey, from, senderPK);
	}

}
