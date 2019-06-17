package com.syswin.pipeline.psservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by 115477 on 2018/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessegeSendrServiceTest {
	@Autowired
	private MessegerSenderService messegerSenderService;


	@Test
	public void sendSpiderMsg() {
		String title = "《有效提升你的谈判能力》";
		String url = null;
		String infoTitle = "价格类谈判：怎样谈出好价格";
		String infoUrl = "http://t.cn/E9BjssG";

		messegerSenderService.sendComplexMsg("p.10000001@t.email", "luohongzhou33@msgseal.com", title,"", url, infoTitle, infoUrl);	}

	@Test
	public void sendIMG() throws IOException {

//		appPublisherService.addPiperAcount("p.10000001@t.email");
		messegerSenderService.sendImage("p.10000001@t.email", "luohongzhou33@msgseal.com", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559561796288&di=6d34a72bace5a7dfa8d5a8f59db2ff6b&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F908fa0ec08fa513db777cf78376d55fbb3fbd9b3.jpg","zhangsan.jpg");
	}


}