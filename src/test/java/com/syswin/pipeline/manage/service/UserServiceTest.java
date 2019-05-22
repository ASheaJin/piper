package com.syswin.pipeline.manage.service;

import com.syswin.pipeline.db.repository.MenuRepository;
import com.syswin.pipeline.service.PiperDeviceInfoService;
import com.syswin.pipeline.service.ps.Env;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 115477 on 2019/3/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	@Autowired
	PiperDeviceInfoService deviceInfoService;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private PiperUserService userService;

	@Test
	public void list() {
//		menuRepository.selectParentIds();
//		System.out.println(userService.list(1, 1));
		String envValue = "{\"language\":\"zh\",\"platform\":\"android\",\"moduleVersion\":\"1.0.1\",\"os_version\":25,\"version\":\"1.2.0P\",\"build\":\"1904030921\"}";
		Env appEnv = JacksonJsonUtil.fromJson(envValue, Env.class);


		System.out.println(deviceInfoService.insertOrupdate("luo", appEnv));
		System.out.println(deviceInfoService.getLang("luo"));

		System.out.println(deviceInfoService.getLang("luo"));

		System.out.println(deviceInfoService.getLang("luo1"));
	}
}