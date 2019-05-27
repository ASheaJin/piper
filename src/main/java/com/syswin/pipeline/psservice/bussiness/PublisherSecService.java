package com.syswin.pipeline.psservice.bussiness;

import com.syswin.pipeline.psservice.bean.SaveText;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;

/**
 * Created by lhz on 2018/11/27.
 */
public interface PublisherSecService {

	//处理组织的消息
//	void monitor(String userId, String ptemail, ChatMsg chatMsg);
//	Integer dealpusharticle(Publisher publisher, int body_type, String txt, PublisherTypeEnums publisherTypeEnums);
//处理组织的消息
	void monitor(String userId, String ptemail, int bodyType, Object show);

	Integer dealpusharticle(Publisher publisher, int bodyType, Object show,  PublisherTypeEnums publisherTypeEnums);
	Integer dealpusharticle(Publisher publisher, int bodyType, Object show, SaveText saveText, PublisherTypeEnums publisherTypeEnums);


}
