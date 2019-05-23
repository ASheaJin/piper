package com.syswin.pipeline.service.message;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.temail.ps.client.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件驱动生成者消费者模式
 * https://blog.csdn.net/a78270528/article/details/79925404
 * Created by 115477 on 2019/1/14.
 */
@Service
public class ChatMessageHandler {

	private final static Logger logger = LoggerFactory.getLogger(ChatMessageHandler.class);

	@Autowired
	private AMenusHandler aMenusHandler;
	@Autowired
	private PMenusHandler pMenusHandler;
	@Autowired
	private CardHandler cardHandler;

	@Autowired
	private BusinessHandler businessHandler;

	@Autowired
	private RegisterHandler registerHandler;

	int bufferSize = 1024;
	Disruptor<MessageEvent> disruptor = new Disruptor<>(MessageEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

//	@PostConstruct
//	public void init() {
//		disruptor.handleEventsWith(aMenusHandler, pMenusHandler, cardHandler, businessHandler);
////		disruptor.handleEventsWith(pMenusHandler);
////		disruptor.handleEventsWith(cardandler);
////		disruptor.handleEventsWith(businessHandler);
//
//		disruptor.start();
//	}

	public void handle(ChatMsg chatMsg, Header header) {

//		RingBuffer<MessageEvent> ringBuffer = disruptor.getRingBuffer();
//		ringBuffer.publishEvent((event, sequence, msg, originHeader, isAndroid) -> {
//			event.setChatMsg(msg);
//			event.setOriginHeader(originHeader);
//		}, chatMsg, header, true);
		MessageEvent event = new MessageEvent();
		event.setChatMsg(chatMsg);
		event.setOriginHeader(header);

		//调用注册的接口
		if (800 == chatMsg.getBody_type() || 801 == chatMsg.getBody_type()) {

			registerHandler.onEvent(event, 0L, true);


		}
		try {
			aMenusHandler.onEvent(event, 0L, true);
		} catch (Exception e) {
			logger.error("aMenusHandler error", e);
		}
		try {
			pMenusHandler.onEvent(event, 0L, true);
		} catch (Exception e) {
			logger.error("pMenusHandler error", e);
		}
		try {
			cardHandler.onEvent(event, 0L, true);
		} catch (Exception e) {
			logger.error("cardHandler error", e);
		}
		try {
			businessHandler.onEvent(event, 0L, true);
		} catch (Exception e) {
			logger.error("businessHandler error", e);
		}


	}
}
