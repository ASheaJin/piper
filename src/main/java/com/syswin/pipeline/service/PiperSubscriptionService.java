package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.dto.output.PulisherSubOutput;
import com.syswin.pipeline.psservice.MessegerSenderService;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.PermissionUtil;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.db.model.Subscription;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.exceptions.SubException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订阅服务
 * Created by 115477 on 2019/1/4.
 */
@Service
public class PiperSubscriptionService {
	private static final Logger logger = LoggerFactory.getLogger(PiperSubscriptionService.class);

	@Autowired
	PSClientService psClientService;
	@Autowired
	AdminService adminService;

	@Autowired
	private SendMessegeService sendMessegeService;

	@Autowired
	private LanguageChange languageChange;


	@Autowired
	private com.syswin.sub.api.PublisherService subPublisherService;


	@Autowired
	private com.syswin.sub.api.SubscriptionService subSubscriptionService;

	@Autowired
	PiperSendSubscriptionService sendSubscriptionService;

	@Value("${domain.promission}")
	private String domain;

	/**
	 * 订阅
	 */
	public Subscription subscribe(String userId, String publishTemail, PublisherTypeEnums piperType) {

		if (StringUtils.isNullOrEmpty(publishTemail) || StringUtils.isNullOrEmpty(userId)) {
			throw new BusinessException("ex.userid.null");
		}
		if (!PermissionUtil.getdomains(domain, userId)) {
			throw new BusinessException(languageChange.getLangByUserId("ex.demain.err", new String[]{userId, domain}, userId));
		}
		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(publishTemail, piperType);
		if (publisher == null) {
			publisher = subPublisherService.getPubLisherById(publishTemail);
			if (publisher == null) {
				throw new BusinessException("ex.publisher.null");
			}
		}
		Subscription subscription = subSubscriptionService.subscribe(userId, publisher.getPublisherId());

		//判断是否自己订阅自己
		if (userId.equals(publisher.getUserId())) {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
		} else {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, publisher.getName());
		}
		//给创建者发个消息
		sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.submotice", new String[]{userId}, publisher.getUserId()), publisher.getUserId(), publisher.getPtemail());
		return subscription;
	}

	/**
	 * 取消订阅
	 *
	 * @param userId
	 * @param publishTemail
	 */
	public void unsubscribe(String userId, String publishTemail, PublisherTypeEnums ptype) {
		if (StringUtils.isNullOrEmpty(psClientService.getTemailPublicKey(publishTemail))) {
			throw new SubException("msg.noemail");
		}
		subSubscriptionService.unsubscribeByTemail(userId, publishTemail, ptype);
	}


	/**
	 * 批量订阅组织号
	 * 根据组织名称获取组织下所有有的邮箱
	 */
	public List<String> subscribeOrgList(String comBairuserIds, String publiserId, String oweruserId) {
		//以 英文分号隔开
//		List<String> stringB = Arrays.asList(stringArray);
//		String [] arr = comBairuserIds.split("\\s+|\\,+|\\;+");
		if (StringUtils.isNullOrEmpty(oweruserId) || StringUtils.isNullOrEmpty(comBairuserIds)) {
			throw new BusinessException("ex.userid.null");
		}
		Admin admin = adminService.getAdmin(oweruserId, PublisherTypeEnums.organize);
		if (admin == null) {
			throw new BusinessException("ex.needorganizer");
		}
		List<String> userList = PatternUtils.tranStrstoList(comBairuserIds);
		String errorDomain = "";
		for (String userId : userList) {
			if (!PermissionUtil.getdomains(domain, userId)) {
				errorDomain = errorDomain + userId;
			}
		}

		if (!StringUtils.isNullOrEmpty(errorDomain)) {
			throw new BusinessException(languageChange.getLangByUserId("ex.demain.err", new String[]{errorDomain, domain}, oweruserId));
		}
		List<String> sendList = subSubscriptionService.subscribeList(userList, publiserId);
		Publisher publisher = subPublisherService.getPubLisherById(publiserId);
		for (String userId : sendList) {
			//防止重复推送
			sendSubscriptionService.sendSub(userId, publisher);
		}
		return sendList;
	}

	public List<String> subscribeList(List<String> userIds, String publiserId, String oweruserId) {
		if (StringUtils.isNullOrEmpty(oweruserId)) {
			throw new BusinessException("ex.userid.null");
		}
		//增加域判断
		String errorDomain = "";
		for (String userId : userIds) {
			if (!PermissionUtil.getdomains(domain, userId)) {
				errorDomain = errorDomain + userId;
			}
		}

		if (!StringUtils.isNullOrEmpty(errorDomain)) {
			throw new BusinessException(languageChange.getLangByUserId("ex.demain.err", new String[]{errorDomain, domain}, oweruserId));
		}
		Admin admin = adminService.getAdmin(oweruserId, PublisherTypeEnums.organize);
		if (admin == null) {
			throw new BusinessException("ex.needorganizer");
		}
		List<String> sendList = subSubscriptionService.subscribeList(userIds, publiserId);
		Publisher publisher = subPublisherService.getPubLisherById(publiserId);
		for (String userId : sendList) {
			//防止重复推送
			sendSubscriptionService.sendSub(userId, publisher);
		}
		return sendList;
	}

	/**
	 * 取消订阅
	 *
	 * @param userId
	 * @param publisherId
	 */
	public void unsubscribe(String userId, String publisherId) {
		Publisher publisher = subPublisherService.getPubLisherById(publisherId);
		if (publisher == null) {
			throw new BusinessException("ex.publisher.null");
		}
		if (PublisherTypeEnums.organize.equals(publisher.getPtype())) {
			throw new BusinessException("msg.nopermission");
		}
		subSubscriptionService.unsubscribeByPubliserId(userId, publisherId);
	}

	public void unsubscribeByOwnerId(String userId, String ownerId, String publiserId, PublisherTypeEnums ptype) {
		Publisher publisher = subPublisherService.getPubLisherById(publiserId);
		if (publisher == null) {
			throw new SubException("ex.publisher.null");
		}
		if (!publisher.getPtype().equals(ptype)) {
			throw new SubException("ex.nosupport");
		}
		if (!publisher.getUserId().equals(ownerId)) {
			throw new SubException("ex.needorganizer");
		}
		subSubscriptionService.unsubscribeByPubliserId(userId, publiserId);
	}

	/**
	 * 获得当前用户订阅的所有出版社
	 *
	 * @param userId
	 * @return
	 */
	public PageInfo getPersonSubscribtions(String userId, int pageNo, int pageSize) {

		return subSubscriptionService.getMySubscribtionsByType(userId, PublisherTypeEnums.person, pageNo, pageSize);
	}

	/**
	 * 获得当前用户订阅的所有出版社
	 *
	 * @param userId
	 * @return
	 */
	public PageInfo getOrgSubscribtions(String userId, int pageNo, int pageSize) {

		return subSubscriptionService.getMySubscribtionsByType(userId, PublisherTypeEnums.organize, pageNo, pageSize);
	}

	/**
	 * 获得出版社的订阅者
	 *
	 * @param publishTemail
	 * @return
	 */
	public List<String> getSubscriber(String publishTemail) {
		return subSubscriptionService.getSubscribers(publishTemail, PublisherTypeEnums.person);
	}


	//================================ manage方法 =========================================>


	public PageInfo list(int pageIndex, int pageSize, String keyword, String publisherId) {
		return subSubscriptionService.list(pageIndex, pageSize, keyword, publisherId);
	}

	public PageInfo<List<String>> getSubscribersByUserId(String keyword, String userId, String publisherId, PublisherTypeEnums organize, int pageNo, int pageSize) {
		//// TODO: 2019/2/20
//		pageSize = pageSize == 0 ? 20 : pageSize;
//		Admin admin = adminService.getAdmin(userId, PublisherTypeEnums.organize);

		return subSubscriptionService.getSubscribersByKeyWord(keyword, userId, publisherId, organize, pageNo, pageSize);
	}


	/**
	 * 获取订阅关系
	 */
	public PulisherSubOutput getsubscribeByUidCid(String userId, String publisherId) {

		if (StringUtils.isNullOrEmpty(publisherId)) {
			throw new BusinessException("ex.publisherid.null");
		}

		Publisher publisher = subPublisherService.getPubLisherById(publisherId);
		if (publisher == null) {
			throw new BusinessException("ex.publisher.null");
		}
//		if (!publisher.getPtype().equals(piperType)) {
//			throw new BusinessException("出版社类型不支持订阅");
//		}
		PulisherSubOutput ps = new PulisherSubOutput();
		ps.setPublisherId(publisher.getPublisherId());
		ps.setPublisherName(publisher.getName());

		Subscription sub = subSubscriptionService.getSub(userId, publisher.getPublisherId());
		ps.setHasSub(sub == null ? "0" : "1");
		return ps;
	}

	public Subscription subscribeNOOrg(String userId, String publishTemail) {
		if (StringUtils.isNullOrEmpty(publishTemail) || StringUtils.isNullOrEmpty(userId)) {
			throw new BusinessException("ex.userid.null");
		}

		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(publishTemail, null);
		if (publisher == null) {
			publisher = subPublisherService.getPubLisherById(publishTemail);
			if (publisher == null) {
				throw new BusinessException("ex.publisher.null");
			}
		}
		if (publisher.getPtype().equals(PublisherTypeEnums.organize)) {
			throw new BusinessException("msg.nopermission");
		}
		Subscription subscription = subscribeInner(userId, publishTemail);

		//判断是否自己订阅自己
		if (userId.equals(publisher.getUserId())) {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
		} else {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, publisher.getName());
		}
//		psClientService.sendTextmessage(publisher.getName() + "<" + publisher.getPtemail() + "> 订阅成功，作者即将推送文章", userId, 0);
		sendMessegeService.sendTextmessage(languageChange.getValueByUserId("msg.subsec", userId), userId, 0, publisher.getPtemail());
		//给创建者发个消息
		sendMessegeService.sendTextmessage(languageChange.getLangByUserId("msg.submotice", new String[]{userId}, publisher.getUserId()), publisher.getUserId(), publisher.getPtemail());

		return subscription;

	}


	public Subscription subscribeInner(String userId, String publishTemail) {
		if (StringUtils.isNullOrEmpty(publishTemail) || StringUtils.isNullOrEmpty(userId)) {
			throw new BusinessException("ex.userid.null");
		}

		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(publishTemail, null);
		if (publisher == null) {
			publisher = subPublisherService.getPubLisherById(publishTemail);
			if (publisher == null) {
				logger.error("publishTemail{},userId{userId} 订阅失败", publishTemail, userId);
				throw new BusinessException("ex.publisher.null");
			}
		}
		if (publisher.getPtype().equals(PublisherTypeEnums.organize)) {
			logger.error("publishTemail{},userId{userId} 订阅失败", publishTemail, userId);
			throw new BusinessException("msg.nopermission");
		}
		Subscription subscription = subSubscriptionService.subscribe(userId, publisher.getPublisherId());

		return subscription;

	}
}
