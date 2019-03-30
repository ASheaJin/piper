package com.syswin.pipeline.service.bussiness.impl;

import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.enums.PeriodEnums;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.service.bussiness.PublisherSecService;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.utils.MessageUtil;
import com.syswin.pipeline.utils.SnowflakeIdWorker;
import com.syswin.pipeline.utils.SwithUtil;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.SendRecordService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.*;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author:lhz
 * @date:2018/12/27 9:23
 */
@Service
@EnableAsync
public class PublisherSecServiceImpl implements PublisherSecService {


	@Value("${app.pipeline.userId}")
	private String from;
	@Lazy
	@Autowired
	SendMessegeService sendMessegeService;

	@Autowired
	AdminService adminService;

	@Autowired
	private com.syswin.sub.api.LogService subLogService;

	@Autowired
	private com.syswin.sub.api.ContentService subContentService;

	@Autowired
	private com.syswin.sub.api.PublisherService subPublisherService;

	@Autowired
	private PiperSubscriptionService piperSubscriptionService;

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private SendRecordService subSendRecordService;

	private final static Logger logger = LoggerFactory.getLogger(PublisherSecServiceImpl.class);
	//过滤能用作发的
	Set<Integer> filterset = new HashSet();
	Set<String> childset = new HashSet();

	public PublisherSecServiceImpl() {
		//http://wiki.syswin.com/pages/viewpage.action?pageId=33689922
		filterset.add(2);//语音
		filterset.add(3);//图片
		filterset.add(10);//视频
		filterset.add(12);//GIF
		filterset.add(14);//文件
		filterset.add(15);//分享
		filterset.add(22);//邮件eml
		filterset.add(23);//命令操作类
		filterset.add(30);//复合消息体

		childset.add(String.valueOf(PeriodEnums.MenuList.HELP));
		childset.add(String.valueOf(PeriodEnums.MenuList.CREATPUBLISH));
		childset.add(String.valueOf(PeriodEnums.MenuList.ORDERPUBLISH));
		childset.add(String.valueOf(PeriodEnums.MenuList.UNORDERPUBLISH));
		childset.add(String.valueOf(PeriodEnums.MenuList.PUBLISHARTICLE));
	}

	/**
	 * insert log
	 *
	 * @param userId
	 * @param bodyType
	 * @param text
	 */
	private void insertLog(String userId, String to, int bodyType, String text) {
		Log log = new Log();
		log.setBodyType(bodyType);
		log.setText(text);
		log.setToemail(to);
		log.setUserId(userId);

		subLogService.addlog(log);
	}

	/**
	 * 推送文章
	 *
	 * @param publisher
	 * @param body_type
	 * @param txt
	 * @param publisherTypeEnums
	 */
	private void dealpusharticle(Publisher publisher, int body_type, String txt, PublisherTypeEnums publisherTypeEnums) {
		//生成文章Id
		SnowflakeIdWorker idWorker = SnowflakeIdWorker.getInstance();
		String contentId = String.valueOf(idWorker.nextId());
		Content content = new Content();
		content.setStatus(1);
		content.setContentId(String.valueOf(contentId));
		content.setContent(txt);
		content.setPublisherId(publisher.getPublisherId());

		subContentService.addContent(content);
		//2、获取订阅该用户的读者列表
		List<String> userIds = subscriptionService.getSubscribers(publisher.getPtemail(), publisherTypeEnums);

		int num = 0;
		//3、逐个发文章
		for (String orderUserId : userIds) {
			// TODO: 2018/12/17 推送文章
			try {
				String fromTemail = publisher.getPtemail();
				logger.info("Thread.currentThread().getName()--------" + Thread.currentThread().getName());
//			fromTemail ="a_piper@systoontest.com";
				sendMessegeService.sendOthermessage(txt, body_type, orderUserId, fromTemail);

				if (SwithUtil.ISLOG) {
					logger.info(publisher.getPtemail() + " send to " + orderUserId + "----   contentId：" + contentId);
				}
				num++;

			} catch (Exception ex) {
				logger.error(publisher.getPtemail() + " send to " + orderUserId + "error----   contentId：" + contentId);
			}

		}
		sendMessegeService.sendTextmessage(num + "人已发送", publisher.getUserId(), 1000, publisher.getPtemail());
		//推送记录
		SendRecord sendRecord = new SendRecord();
		sendRecord.setContentId(contentId);
		sendRecord.setSendnum(num);
		sendRecord.setUserId(publisher.getPtemail());
		subSendRecordService.addSendRecord(sendRecord);

	}

	public void monitorP(String userId, String ptemail, ChatMsg chatMsg) {

//		Publisher publisher = subPublisherService.getPubLisherByuserId(userId, PublisherTypeEnums.person);
		// 为测试二用
//		Publisher publisher = publisherRepository.selectByPtemail(ptemail);
//		ptemail = publisher.getPtemail();
		int body_type = chatMsg.getBody_type();
		String orgContent = chatMsg.getContent();
		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(ptemail, PublisherTypeEnums.person);
		if (publisher == null) {
			return;
		}

		if (orgContent.contains("《订阅》")) {
			piperSubscriptionService.subscribe(userId, ptemail, PublisherTypeEnums.person);
			return;
		}
		if (orgContent.contains("《取消订阅》")) {
			piperSubscriptionService.unsubscribe(userId, publisher.getPublisherId());
			return;
		}


		Subscription subscription = subscriptionService.getSub(userId, publisher.getPublisherId());
		if (subscription == null) {
			sendMessegeService.sendTextmessage("您尚未订阅该出版社 发送 《订阅》 订阅该出版社", userId, 0, publisher.getPtemail());

		} else {
//			sendMessegeService.sendTextmessage("您已订阅该出版社 发送 《取消订阅》 取消订阅该出版社", userId, 0, publisher.getPtemail());
		}
		//判断出版社是否存在
		if (userId.equals(publisher.getUserId())) {
//			if (!filterset.contains(body_type)) {
//				sendMessegeService.sendTextmessage(MessageUtil.sendCreateHelpTip("请发送文件、语音、图片、视频"), userId, 1000, ptemail);
//				return;
//			}
			dealpusharticle(publisher, body_type, orgContent, PublisherTypeEnums.person);
		} else {
			sendMessegeService.sendTextmessage(MessageUtil.sendCreateHelpTip("回复功能暂不支持"), userId, 1000, ptemail);
		}


	}

	//处理组织消息
	public void monitor(String userId, String ptemail, ChatMsg chatMsg) {
		String orgContent = chatMsg.getContent();
		int body_type = chatMsg.getBody_type();

		//判断发送格式
		insertLog(userId, ptemail, body_type, body_type > 30 ? "非业务指令" : JSONObject.toJSONString(chatMsg));

		if (body_type > 90) {
			return;
		}
		if (ptemail.equals(from)) {
			return;
		}

		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(ptemail, PublisherTypeEnums.organize);

		//判断该出版社是组织出版社还是个人出版社
		if (publisher == null) {
			monitorP(userId, ptemail, chatMsg);
			return;
		}

		if (userId.equals(publisher.getUserId())) {
			dealpusharticle(publisher, body_type, orgContent, PublisherTypeEnums.organize);
		} else {
			sendMessegeService.sendTextmessage(MessageUtil.sendCreateHelpTip("只有创建者有权限操作"), userId, 1000, ptemail);
		}

	}

}
