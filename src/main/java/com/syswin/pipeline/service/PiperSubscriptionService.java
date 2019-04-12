package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.dto.output.PulisherSubOutput;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.ContentService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Content;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.db.model.Subscription;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.exceptions.SubException;
import com.syswin.sub.api.response.SubResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订阅服务
 * Created by 115477 on 2019/1/4.
 */
@Service
public class PiperSubscriptionService {

	@Autowired
	PSClientService psClientService;
	@Autowired
	AdminService adminService;

	@Autowired
	private SendMessegeService sendMessegeService;

	@Autowired
	private ContentService contentService;


	@Autowired
	private com.syswin.sub.api.PublisherService subPublisherService;


	@Autowired
	private com.syswin.sub.api.SubscriptionService subSubscriptionService;

	@Autowired
	SendSubscriptionService sendSubscriptionService;

	/**
	 * 订阅
	 */
	public Subscription subscribe(String userId, String publishTemail, PublisherTypeEnums piperType) {

		if (StringUtils.isNullOrEmpty(publishTemail) || StringUtils.isNullOrEmpty(userId)) {
			throw new BusinessException("ex.userid.null");
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
//		if (PublisherTypeEnums.organize.equals(publisher.getPtype())) {
//			throw new BusinessException("组织出版社不能取消订阅");
//		}
		subSubscriptionService.unsubscribeByPubliserId(userId, publisherId);
	}

	public void unsubscribeByOwnerId(String userId, String ownerId, String publiserId, PublisherTypeEnums ptype) {
		Publisher publisher = subPublisherService.getPubLisherById(publiserId);
		if (publisher == null) {
			throw new SubException("ex.publisher.null");
		}
		if (!publisher.getPtype().equals(ptype) || !publisher.getUserId().equals(ownerId)) {
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
	public List<Publisher> getMySubscribtion(String userId) {

		return subSubscriptionService.getMySubscribtions(userId);
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
		List<Subscription> list = subSubscriptionService.list(pageIndex, pageSize, keyword, publisherId);
		return new PageInfo(list);
	}

	public List<String> getSubscribersByUserId(String keyword, String userId, String publisherId, PublisherTypeEnums organize, int pageNo, int pageSize) {
		//// TODO: 2019/2/20
//		pageSize = pageSize == 0 ? 20 : pageSize;
		Admin admin = adminService.getAdmin(userId, PublisherTypeEnums.organize);
		if (admin == null) {
			return new ArrayList<>();
		}

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
		Subscription subscription = subSubscriptionService.subscribe(userId, publisher.getPublisherId());

		//判断是否自己订阅自己
		if (userId.equals(publisher.getUserId())) {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
		} else {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, publisher.getName());
		}
//		psClientService.sendTextmessage(publisher.getName() + "<" + publisher.getPtemail() + "> 订阅成功，作者即将推送文章", userId, 0);
//		sendMessegeService.sendTextmessage("订阅成功，作者会在此为你发文章", userId, 0, publisher.getPtemail());

		return subscription;

	}
}
