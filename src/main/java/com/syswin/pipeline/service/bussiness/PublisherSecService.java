package com.syswin.pipeline.service.bussiness;

import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;

/**
 * Created by lhz on 2018/11/27.
 */
public interface PublisherSecService {

	//处理组织的消息
	void monitor(String userId, String ptemail, ChatMsg chatMsg);

	Integer dealpusharticle(Publisher publisher, int body_type, String txt, PublisherTypeEnums publisherTypeEnums);
}
