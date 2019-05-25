package com.syswin.pipeline.service;


import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.sub.api.db.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 订阅服务
 * Created by 115477 on 2019/1/4.
 */
@Service
public class PiperSendSubscriptionService {
	@Autowired
	private SendMessegeService sendMessegeService;
	@Autowired
	private LanguageChange languageChange;

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
		//给创建者发个消息
		sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.submotice", new String[]{userId}, publisher.getUserId()), publisher.getUserId(), publisher.getPtemail());

		sendMessegeService.sendTextmessage(languageChange.getValueByUserId("msg.adminsendtip", userId), userId, publisher.getPtemail());

	}
}
