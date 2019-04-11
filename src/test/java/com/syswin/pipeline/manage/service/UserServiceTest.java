package com.syswin.pipeline.manage.service;

import com.syswin.pipeline.db.repository.MenuRepository;
import com.syswin.pipeline.service.DeviceInfoService;
import com.syswin.pipeline.utils.LanguageChange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by 115477 on 2019/3/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	@Autowired
	DeviceInfoService deviceInfoService;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private UserService userService;

	@Test
	public void list() {
//		menuRepository.selectParentIds();
//		System.out.println(userService.list(1, 1));
//		System.out.println(deviceInfoService.insertOrupdate("luo", "en", null, null, null));
		System.out.println(deviceInfoService.getLang("luo"));

		System.out.println(deviceInfoService.getLang("luo"));

		System.out.println(deviceInfoService.getLang("luo1"));
	}
}