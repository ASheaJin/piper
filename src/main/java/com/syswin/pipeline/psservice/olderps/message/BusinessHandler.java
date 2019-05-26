package com.syswin.pipeline.psservice.olderps.message;

import com.lmax.disruptor.EventHandler;
import com.syswin.pipeline.psservice.bussiness.PublisherSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by 115477 on 2019/1/23.
 */
@Component
public class BusinessHandler implements EventHandler<MessageEvent> {
	@Lazy
	@Autowired
	private PublisherSecService publisherSecService;

	@Override
	public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
		//会话式应用处理
//		publisherSecService.monitor(event.getOriginHeader().getReceiver(), event.getOriginHeader().getSender(), event.getChatMsg().getBody_type(), event.getChatMsg().getContent());

	}

}
