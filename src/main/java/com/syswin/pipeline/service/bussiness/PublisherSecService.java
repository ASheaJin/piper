package com.syswin.pipeline.service.bussiness;

import com.syswin.pipeline.service.ps.ChatMsg;

/**
 *
 * Created by lhz on 2018/11/27.
 */
public interface PublisherSecService {

	//处理组织的消息
	public void monitor(String userId, String ptemail, ChatMsg chatMsg) ;
}
