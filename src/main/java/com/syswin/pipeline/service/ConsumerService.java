package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.Consumer;
import com.syswin.pipeline.db.repository.ConsumerRepository;
import com.syswin.pipeline.utils.PermissionUtil;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.temail.ps.client.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private PublisherService publisherService;

	public String getUserLogVersion(Header header, String version,String myRole) {
		Consumer consumer = consumerRepository.selectByUserId(header.getReceiver());
		if (consumer == null) {
			consumer = new Consumer();
			consumer.setCurversion(version);
			consumer.setPubkey(header.getReceiverPK());
			consumer.setUserId(header.getReceiver());
			consumer.setRole(myRole);
			consumerRepository.insert(consumer);
		} else {
			if (version.equals(consumer.getCurversion()) && myRole.equals(consumer.getRole())) {
				return version;
			}
		}

		return String.valueOf(Integer.parseInt(version) + 1);
	}

	public void updateUserLogVersion(String userId, String version,String myRole) {
		Consumer consumer = new Consumer();
		consumer.setCurversion(version);
		consumer.setUserId(userId);
		consumer.setRole(myRole);
		consumerRepository.update(consumer);
	}

	public String getPermission(Header header) {
		Publisher publisher = publisherService.getPubLisherByuserId(header.getReceiver(), PublisherTypeEnums.person);
		//判断version字段，是否需要发送菜单
		//不是个人出版社
		int isPerson = publisher != null ? 1 : 0;
		Admin admin = adminService.getAdmin(header.getReceiver());
		//判断是不是组织出版社
		int isOrg = admin != null ? 1 : 0;

		String per = "00" + String.valueOf(isOrg) + String.valueOf(isPerson);

		return PermissionUtil.getMyVersion(per);
	}
}
