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
public class PiperPublisherServiceTest {

	@Autowired
	private PiperPublisherService piperPublisherService;

	@Test
	public void insert() {
		piperPublisherService.addPublisher("luohongzhou1@syswin.com", "zhang", "p.10000001@msgseal.com", 1);
	}


	@Test
	public void selectbyId() {

	}

	@Test
	public void select() {

		System.out.println("Account :" + piperPublisherService.getPubLisherById("34418866622300160"));
	}
}