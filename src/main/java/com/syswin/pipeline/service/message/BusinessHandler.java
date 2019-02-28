package com.syswin.pipeline.service.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.service.bussiness.PublisherSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 115477 on 2019/1/23.
 */
@Component
public class BusinessHandler implements EventHandler<MessageEvent> {
	@Autowired
	private PublisherSecService publisherSecService;

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
		//会话式应用处理
		publisherSecService.monitorORG(event.getOriginHeader().getReceiver(), event.getOriginHeader().getSender(), event.getChatMsg());

	}

}
