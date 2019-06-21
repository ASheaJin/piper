package com.syswin.pipeline.psservice.bussiness;

import com.syswin.pipeline.psservice.bean.SaveText;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;

/**
 * Created by lhz on 2018/11/27.
 */
public interface PublisherSecService {

	//处理组织的消息
//	void monitor(String userId, String ptemail, ChatMsg chatMsg);
//	Integer dealPushArticle(Publisher publisher, int body_type, String txt, PublisherTypeEnums publisherTypeEnums);
//处理组织的消息
	void monitor(String userId, String ptemail, int bodyType, Object show);

	Integer dealPushArticle(Publisher publisher, int bodyType, Object show, PublisherTypeEnums publisherTypeEnums);
	Integer dealPushArticle(Publisher publisher, int bodyType, Object show, ContentEntity saveText, PublisherTypeEnums publisherTypeEnums);


}
