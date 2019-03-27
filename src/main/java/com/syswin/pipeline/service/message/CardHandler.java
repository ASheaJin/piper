package com.syswin.pipeline.service.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.Card;
import com.syswin.pipeline.service.ps.util.FastJsonUtil;
import com.syswin.pipeline.service.ps.util.StringUtil;
import com.syswin.pipeline.utils.CacheUtil;
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
public class CardHandler implements EventHandler<MessageEvent> {

	//名片消息
	public static final Integer CARD_MESSAGE_BODY_TYPE = 4;

	public static final String VCARD_TEMPLATE = "BEGIN:VCARD\r\nPHOTO:%s\r\nVERSION:3.0\r\nN:%s\r\nEMAIL:%s\r\nEND:VCARD";

	@Value("${app.pipeline.nick}")
	private String nick;

	@Value("${app.pipeline.imgUrl}")
	private String url;
	private final static Logger logger = LoggerFactory.getLogger(CardHandler.class);

	@Value("${app.pipeline.userId}")
	private String from;

	@Lazy
	@Autowired
	private SendMessegeService sendMessegeService;

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
		String content = event.getChatMsg().getContent();
		if (!StringUtil.isEmpty(content)) {
			try {
				//第一条消息
				String feedId = FastJsonUtil.parseObject(content).getString("feedId");
				if (!StringUtil.isEmpty(feedId)) {
					Header header = event.getOriginHeader();
					if (!from.equals(header.getSender())) {
						return;
					}
					CacheUtil.put(header.getSender(), header.getSenderPK());
					CacheUtil.put(header.getReceiver(), header.getReceiverPK());
					sendMessegeService.sendCard(header.getSender(), header.getReceiver(), "a.piper");
//        ChatMsg msg = new ChatMsg(header.getSender(), header.getReceiver(),
//                UUID.randomUUID().toString(), cardContent(header.getSender()));
//        msg.setBody_type(CARD_MESSAGE_BODY_TYPE);
//
//        psClientService.sendCardMessage( msg , header.getSender(), header.getSenderPK(),header.getReceiver(), header.getReceiverPK());
//        psClientService.sendChatMessage(msg, header.getReceiver(), header.getReceiverPK());

				}
			} catch (Exception e) {
				logger.info("处理名片消息失败", e);
			}
		}
	}

	private Card cardContent(String temail) {
		String vcard = String.format(VCARD_TEMPLATE, url, "Piper", temail);
		Card card = new Card();
		card.setNick("Piper");
		card.setFeedId(vcard);
		card.setUrl(url);
		card.setDesc(temail);
		return card;
	}
}
