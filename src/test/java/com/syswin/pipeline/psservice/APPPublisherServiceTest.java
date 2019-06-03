package com.syswin.pipeline.psservice;

import com.syswin.pipeline.db.model.Account;
import com.syswin.pipeline.db.repository.AccountRepository;
import com.syswin.sub.api.utils.SnowflakeIdWorker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * Created by 115477 on 2018/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class APPPublisherServiceTest {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private APPPublisherService appPublisherService;

	@Test
	public void addAcount() {

		appPublisherService.addAcount();
	}

	@Test
	public void addPiperAcount() {
		appPublisherService.addPiperAcount("p.10000001@t.email");
	}


}