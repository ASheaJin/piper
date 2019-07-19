package com.syswin.pipeline.service;


import com.syswin.pipeline.psservice.MessegerSenderService;
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
    private MessegerSenderService messegerSenderService;
    @Autowired
    private LanguageChange languageChange;

    @Async("msgThreadPool")
    public void sendSub(String userId, Publisher publisher) {

        //判断是否自己订阅自己
//        if (userId.equals(publisher.getUserId())) {
//            messegerSenderService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
//        } else {
//            messegerSenderService.sendCard(publisher.getPtemail(), userId, publisher.getName());
//        }
        messegerSenderService.sendCard(publisher.getPtemail(), userId, publisher.getName());
//			if (admin != null) {
//				sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
//			} else {
//				sendMessegeService.sendCard(publisher.getPtemail(), userId, publisher.getName());
//			}
        //给创建者发个消息
        messegerSenderService.sendSynchronizationTxt(publisher.getPtemail(), publisher.getUserId(), languageChange.getLangByUserId("msg.submotice", new String[]{userId}, publisher.getUserId()));

        messegerSenderService.sendSynchronizationTxt(publisher.getPtemail(), userId, languageChange.getValueByUserId("msg.adminsendtip", userId));

    }
}
