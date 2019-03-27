package com.syswin.pipeline.service;


import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.sub.api.db.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 订阅服务
 * Created by 115477 on 2019/1/4.
 */
@Service
public class SendSubscriptionService {
	@Autowired
	private SendMessegeService sendMessegeService;

	@Async("msgThreadPool")
	public void sendSub(String userId, Publisher publisher) {

		//判断是否自己订阅自己
		if (userId.equals(publisher.getUserId())) {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
		} else {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, publisher.getName());
		}
//			if (admin != null) {
//				sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
//			} else {
//				sendMessegeService.sendCard(publisher.getPtemail(), userId, publisher.getName());
//			}
		sendMessegeService.sendTextmessage("管理员邀请您进入该邮件组，之后管理员会在这给您发文档或消息", userId, 0, publisher.getPtemail());

	}
}
