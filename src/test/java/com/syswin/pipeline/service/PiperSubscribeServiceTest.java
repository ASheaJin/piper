package com.syswin.pipeline.service;

import com.syswin.pipeline.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 115477 on 2018/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PiperSubscribeServiceTest {

	@Autowired
	private PiperSubscriptionService piperSubscriptionService;

	@Test
	public void insert() {

	}


	@Test
	public void selectbyId() {

	}

	@Test
	public void select() {

		System.out.println("Account :" + piperSubscriptionService.getSubscriber("p.10000001@t.email"));
	}
}