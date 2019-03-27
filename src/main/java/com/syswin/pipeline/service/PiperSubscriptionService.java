package com.syswin.pipeline.service;

import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.db.model.Subscription;
import com.syswin.sub.api.enums.PublisherTypeEnums;
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
	private com.syswin.sub.api.PublisherService subPublisherService;


	@Autowired
	private com.syswin.sub.api.SubscriptionService subSubscriptionService;

	@Autowired
	SendSubscriptionService sendSubscriptionService;

	/**
	 * 订阅
	 */
	public SubResponseEntity subscribe(String userId, String publishTemail) {

		if (StringUtils.isNullOrEmpty(publishTemail) || StringUtils.isNullOrEmpty(userId)) {
			return new SubResponseEntity("订阅邮箱不能为空");
		}
		if (StringUtils.isNullOrEmpty(psClientService.getTemailPublicKey(publishTemail))) {
			return new SubResponseEntity("订阅邮箱不存在");
		}
		SubResponseEntity resp = subSubscriptionService.subscribe(userId, publishTemail, PublisherTypeEnums.person);

		if (!resp.isSuc()) return resp;
		Publisher publisher = subPublisherService.getPubLisherByPublishTmail(publishTemail, PublisherTypeEnums.person);
		if (publisher == null) {
			return new SubResponseEntity("订阅失败，出版社不存在");
		}
		//判断是否自己订阅自己
		if (userId.equals(publisher.getUserId())) {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, "* " + publisher.getName());
		} else {
			sendMessegeService.sendCard(publisher.getPtemail(), userId, publisher.getName());
		}
		psClientService.sendTextmessage(publisher.getName() + "<" + publisher.getPtemail() + "> 订阅成功，作者即将推送文章", userId, 0);
		sendMessegeService.sendTextmessage("订阅成功，作者会在此为你发文章", userId, 0, publisher.getPtemail());

		return resp;
	}

	/**
	 * 取消订阅
	 *
	 * @param userId
	 * @param publishTemail
	 */
	public SubResponseEntity unsubscribe(String userId, String publishTemail, PublisherTypeEnums ptype) {
		if (StringUtils.isNullOrEmpty(psClientService.getTemailPublicKey(publishTemail))) {
			return new SubResponseEntity("订阅邮箱不存在");
		}
		return subSubscriptionService.unsubscribe(userId, publishTemail, ptype);
	}


	/**
	 * 批量订阅组织号
	 * 根据组织名称获取组织下所有有的邮箱
	 */
	public SubResponseEntity subscribeOrgList(String comBairuserIds, String publiserId, String oweruserId) {
		//以 英文分号隔开
//		List<String> stringB = Arrays.asList(stringArray);
//		String [] arr = comBairuserIds.split("\\s+|\\,+|\\;+");
		if (StringUtils.isNullOrEmpty(oweruserId) || StringUtils.isNullOrEmpty(comBairuserIds)) {
			return new SubResponseEntity("userId或者邮箱列表为空");
		}
		Admin admin = adminService.getAdmin(oweruserId);
		if (admin == null) {
			return new SubResponseEntity("你不是组织管理者");
		}
		List<String> userList = PatternUtils.tranStrstoList(comBairuserIds);
		SubResponseEntity resp = subSubscriptionService.subscribeList(userList, publiserId);
		Publisher publisher = subPublisherService.getPubLisherById(publiserId);
		for (String userId : userList) {
			//防止重复推送
			Subscription subscription = subSubscriptionService.getSub(userId, publiserId);
			if (subscription == null) {
				sendSubscriptionService.sendSub(userId, publisher);
			}
		}
		return resp;
	}

	public SubResponseEntity subscribeList(List<String> userIds, String publiserId, String oweruserId) {
		if (StringUtils.isNullOrEmpty(oweruserId)) {
			return new SubResponseEntity("userId为空");
		}
		Admin admin = adminService.getAdmin(oweruserId);
		if (admin == null) {
			return new SubResponseEntity("你不是邮件组管理者");
		}
		SubResponseEntity resp = subSubscriptionService.subscribeList(userIds, publiserId);
		Publisher publisher = subPublisherService.getPubLisherById(publiserId);
		for (String userId : userIds) {
			//防止重复推送
			Subscription subscription = subSubscriptionService.getSub(userId, publiserId);
			if (subscription == null) {
				sendSubscriptionService.sendSub(userId, publisher);
			}
		}
		return resp;
	}

	/**
	 * 取消订阅
	 *
	 * @param userId
	 * @param publisherId
	 */
	public SubResponseEntity unsubscribe(String userId, String publisherId) {
		Publisher publisher = subPublisherService.getPubLisherById(publisherId);
		if (publisher == null) {
			return new SubResponseEntity(false, "出版社不能为空");
		}
		if (PublisherTypeEnums.organize.equals(publisher.getPtype())) {
			return new SubResponseEntity(false, "组织出版社不能取消订阅");
		}
		return subSubscriptionService.unsubscribe(userId, publisherId);
	}

	public SubResponseEntity unsubscribeByOwnerId(String userId, String ownerId, Long publiserId, PublisherTypeEnums ptype) {

		return subSubscriptionService.unsubscribeByOwnerId(userId, ownerId, ptype);
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


	public Map<String, Object> listByExample(int pageIndex, int pageSize, String publisherId) {


		return subSubscriptionService.list(pageIndex, pageSize, publisherId);
	}

	public List<String> getSubscribersByUserId(String keyword, String userId, String publisherId, PublisherTypeEnums organize, int pageNo, int pageSize) {
		//// TODO: 2019/2/20
//		pageSize = pageSize == 0 ? 20 : pageSize;
		Admin admin = adminService.getAdmin(userId);
		if (admin == null) {
			return new ArrayList<>();
		}

		return subSubscriptionService.getSubscribersByKeyWord(keyword, userId, publisherId, organize, pageNo, pageSize);
	}
}
