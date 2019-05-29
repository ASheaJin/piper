package com.syswin.pipeline.psservice.bussiness.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.enums.PeriodEnums;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.psservice.bean.SaveText;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.psservice.bussiness.PublisherSecService;
import com.syswin.pipeline.service.content.ContentHandleJobManager;
import com.syswin.pipeline.psservice.olderps.ChatMsg;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.pipeline.utils.SwithUtil;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.showType.BaseShow;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.SendRecordService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.*;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.utils.SnowflakeIdWorker;
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

	@Autowired
	private ContentHandleJobManager contentHandleJobManager;

	@Autowired
	private LanguageChange languageChange;

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

	@Override
	public Integer dealpusharticle(Publisher publisher, int body_type, Object show, PublisherTypeEnums publisherTypeEnums) {
		return dealpusharticle(publisher, body_type, show, null, publisherTypeEnums);
	}

	/**
	 * 推送文章
	 *
	 * @param publisher
	 * @param body_type
	 * @param show
	 * @param publisherTypeEnums
	 */
	@Override
	public Integer dealpusharticle(Publisher publisher, int body_type, Object show, SaveText saveShow, PublisherTypeEnums publisherTypeEnums) {
		if (StringUtil.isEmpty(show)) {
			throw new BusinessException("消息不能为空");
		}

		//生成文章Id
		String contentId = String.valueOf(SnowflakeIdWorker.getInstance().nextId());
		Content content = new Content();
		try {

			content.setStatus(1);
			content.setContentId(contentId);
			if (saveShow != null) {
				content.setContent(JSONObject.toJSONString(saveShow));
			} else {
				content.setContent(show.toString());
			}
			content.setBodyType(body_type);
			content.setPublisherId(publisher.getPublisherId());

			subContentService.addContent(content);
		} catch (Exception e) {
			logger.error(content + " 添加失败", e);
		}
		//2、获取订阅该用户的读者列表
		List<String> userIds = subscriptionService.getSubscribers(publisher.getPtemail(), publisherTypeEnums);

		//内容处理
		contentHandleJobManager.addJob(publisher.getPublisherId(), contentId, body_type, content.getContent(), content.getCreateTime());

		int num = 0;
		//3、逐个发文章
		for (String orderUserId : userIds) {
			// TODO: 2018/12/17 推送文章
			try {
				String fromTemail = publisher.getPtemail();
//			fromTemail ="a_piper@systoontest.com";
				//分别对不同类型的文章进行处理
				if (body_type == 1) {
					String cont = JSON.parseObject(show.toString()).getString("text");
					sendMessegeService.sendTextmessage(cont, orderUserId, fromTemail);

				} else if (body_type == 801) {
					logger.info("fromTemail, orderUserId, show" + fromTemail + orderUserId + show);
					PsClientKeeper.newInstance().sendMsg(fromTemail, orderUserId, (BaseShow) show);
				} else {
					sendMessegeService.sendOthermessage(show.toString(), body_type, orderUserId, fromTemail);
					logger.info("Thread.currentThread().getName()--------" + Thread.currentThread().getName());

				}
				if (SwithUtil.ISLOG) {
					logger.info(publisher.getPtemail() + " send to " + orderUserId + "----   contentId：" + contentId);
				}
				num++;

			} catch (Exception ex) {
				logger.error(publisher.getPtemail() + " send to " + orderUserId + "error----   contentId：" + contentId, ex);
			}

		}
		sendMessegeService.sendTextmessage(num + languageChange.getValueByUserId("msg.hassend", publisher.getUserId()), publisher.getUserId(), 1000, publisher.getPtemail());
		//推送记录
		SendRecord sendRecord = new SendRecord();
		sendRecord.setContentId(contentId);
		sendRecord.setSendnum(num);
		sendRecord.setUserId(publisher.getPtemail());
		subSendRecordService.addSendRecord(sendRecord);
		return num;
	}

	public void monitorP(String userId, String ptemail, int bodyType, Object show) {

//		Publisher publisher = subPublisherService.getPubLisherByuserId(userId, PublisherTypeEnums.person);
		// 为测试二用
//		Publisher publisher = publisherRepository.selectByPtemail(ptemail);
//		ptemail = publisher.getPtemail();
		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(ptemail, PublisherTypeEnums.person);
		if (publisher == null) {
			return;
		}

		//如果用户输入的是文本


		Subscription subscription = subscriptionService.getSub(userId, publisher.getPublisherId());
		if (subscription == null && !publisher.getUserId().equals(userId)) {
			if (bodyType == 1) {
				String txt = StringUtils.filterStr(show.toString());
				if ("subscribe".equals(txt) || "订阅".equals(txt) || "1".equals(txt)) {
					piperSubscriptionService.subscribe(userId, ptemail, publisher.getPtype());
					return;
				} else {
					sendMessegeService.sendTextmessage(languageChange.getValueByUserId("msg.ordertip", userId), userId, 0, publisher.getPtemail());
				}
			}
			sendMessegeService.sendTextmessage(languageChange.getValueByUserId("msg.noreply", userId), userId, 1000, ptemail);
//			sendMessegeService.sendTextmessage("您已订阅该出版社 发送 《取消订阅》 取消订阅该出版社", userId, 0, publisher.getPtemail());
		}
		//判断出版社是否存在
		if (userId.equals(publisher.getUserId())) {
//			if (!filterset.contains(body_type)) {
//				sendMessegeService.sendTextmessage(MessageUtil.sendCreateHelpTip("请发送文件、语音、图片、视频"), userId, 1000, ptemail);
//				return;
//			}
			dealpusharticle(publisher, bodyType, show, PublisherTypeEnums.person);
		}


	}

	//处理组织消息
	@Override
	public void monitor(String userId, String ptemail, int bodyType, Object show) {

		if (StringUtil.isEmpty(show)) {
			throw new BusinessException("消息体不能为空");
		}
		//判断发送格式
		insertLog(userId, ptemail, bodyType, bodyType > 30 ? "非业务指令" : show.toString());

		if (bodyType > 90) {
			return;
		}
		if (ptemail.equals(from)) {
			return;
		}

		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(ptemail, PublisherTypeEnums.organize);

		//判断该出版社是组织出版社还是个人出版社
		if (publisher == null) {
			monitorP(userId, ptemail, bodyType, show);
			return;
		}

		if (userId.equals(publisher.getUserId())) {
			dealpusharticle(publisher, bodyType, show, PublisherTypeEnums.organize);
		} else {
			sendMessegeService.sendTextmessage(languageChange.getValueByUserId("msg.nopermission", userId), userId, 1000, ptemail);
		}

	}

}
