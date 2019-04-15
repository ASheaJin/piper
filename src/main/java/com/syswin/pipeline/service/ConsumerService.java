package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.Consumer;
import com.syswin.pipeline.db.model.ConsumerExample;
import com.syswin.pipeline.db.repository.ConsumerRepository;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.temail.ps.client.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:lhz
 * @date:2019/3/6 13:51
 */
@Service
public class ConsumerService {
	@Autowired
	ConsumerRepository consumerRepository;

	@Autowired
	private AdminService adminService;
	@Autowired
	private SendMessegeService sendMessegeService;
	@Autowired
	private PublisherService publisherService;

	public String getUserVersion(Header header, String version, String myRole) {
		ConsumerExample consumerExample = new ConsumerExample();
		ConsumerExample.Criteria criteria = consumerExample.createCriteria();
		criteria.andPtemailEqualTo(header.getSender()).andUserIdEqualTo((header.getReceiver()));
		List<Consumer> consumerList = consumerRepository.selectByExample(consumerExample);
		Consumer consumer = null;
		if (consumerList.size() > 0) {
			consumer = consumerList.get(0);
			if (version.equals(consumer.getCurversion()) && myRole.equals(consumer.getRole())) {
				return version;
			}

		} else {

			consumer = new Consumer();
			consumer.setCurversion(version);
			consumer.setPtemail(header.getSender());
			consumer.setUserId(header.getReceiver());
			consumer.setRole(myRole);
			consumerRepository.insertSelective(consumer);

		}

		return String.valueOf(Integer.parseInt(version) + 1);
	}

	public boolean getUserVersion(Header header) {
		ConsumerExample consumerExample = new ConsumerExample();
		ConsumerExample.Criteria criteria = consumerExample.createCriteria();
		criteria.andPtemailEqualTo(header.getSender()).andUserIdEqualTo((header.getReceiver()));
		List<Consumer> consumerList = consumerRepository.selectByExample(consumerExample);

		return consumerList.size() > 0;
	}


	public void updateUserVersion(Header header, String version, String myRole) {
		Consumer consumer = new Consumer();
		consumer.setCurversion(version);
		consumer.setPtemail(header.getSender());
		consumer.setUserId(header.getReceiver());
		consumer.setRole(myRole);

		ConsumerExample consumerExample = new ConsumerExample();
		ConsumerExample.Criteria criteria = consumerExample.createCriteria();
		criteria.andPtemailEqualTo(header.getSender()).andUserIdEqualTo(header.getReceiver());
		consumerRepository.updateByExampleSelective(consumer, consumerExample);
	}

	/**
	 * a的出版社的角色
	 * 0 什么都不是
	 * 1 只是个人出版社的 作者
	 * 2 只是组织出版社 作者
	 * 3 同时组织个人出版社 的作者读者
	 *
	 * @param userId
	 * @return
	 */
	public String getAMenuRole(String userId) {
		//什么都不是
		int role = 0;
		//是个人出版社
		Publisher publisher = publisherService.getPublisherByUserId(userId, PublisherTypeEnums.person);
		if (publisher != null) {
			role = role + 1;
		}
		//是组织出版社
		Admin admin = adminService.getAdmin(userId, PublisherTypeEnums.organize);
		if (admin != null) {
			role = role + 2;
		}
		return String.valueOf(role);
	}

	/**
	 * p的出版社的角色
	 * 0 该出版社不存在
	 * 1 个人出版社的 读者
	 * 2 个人出版社的 作者
	 * 3 组织出版社 的读者
	 * 4 组织出版社 的作者
	 *
	 * @param userId
	 * @param pTemail
	 * @return
	 */
	public String getPiperMenuRole(String userId, String pTemail) {
		//什么都不是
		int role = 0;
		Publisher publisher = publisherService.getPubLisherByPublishTmail(pTemail, null);
		if (publisher == null) {
			return String.valueOf(role);
		}
		if (PublisherTypeEnums.person.equals(publisher.getPtype())) {
			role = publisher.getUserId().equals(userId) ? 2 : 1;
		}
		if (PublisherTypeEnums.organize.equals(publisher.getPtype())) {
			role = publisher.getUserId().equals(userId) ? 4 : 3;
		}
		return String.valueOf(role);
	}

}
