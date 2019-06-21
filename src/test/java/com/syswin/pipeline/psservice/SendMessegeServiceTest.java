package com.syswin.pipeline.psservice;

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
public class SendMessegeServiceTest {
	@Autowired
	private SendMessegeService sendMessegeService;


	@Test
	public void sendCard() {

		sendMessegeService.sendCard("a.piper@t.email", "luohongzhou33@msgseal.com", "Ptest","http://img4.imgtn.bdimg.com/it/u=3125493143,185152793&fm=26&gp=0.jpg");
	}

	@Test
	public void sendTextmessage() {

//		appPublisherService.addPiperAcount("p.10000001@t.email");
		sendMessegeService.sendTextMessage("1212", "luohongzhou33@msgseal.com", "a.piper@t.email");
	}


}