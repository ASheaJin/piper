package com.syswin.pipeline.service;

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
public class AccountServiceTest {
	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void insert() {
		Account account = new Account();
		account.setAccountId(SnowflakeIdWorker.getInstance().nextId() + "");
		account.setAccountId(1l + "");
		account.setUserId("1111@temail.com");
		account.setBalance(new BigDecimal("12"));
		Assert.assertEquals(1, accountRepository.insert(account));
	}

	@Test
	public void update() {
		Account account = new Account();
		account.setAccountId(1l + "");
		account.setUserId("1111@temail.com");
		account.setBalance(new BigDecimal("12"));
		Assert.assertEquals(1, accountRepository.update(account));
	}

	@Test
	public void selectByUserId() {
		Assert.assertNotNull(accountRepository.selectByUserId("1111@temail.com"));
	}

	@Test
	public void select() {
		Assert.assertNotNull(accountRepository.select());
	}

}