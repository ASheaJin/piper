package com.syswin.pipeline.service;

import com.syswin.pipeline.enums.BalanceResultEnums;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
public class AccountBalanceServiceTest {

    @Autowired
    private AccountService accountService;

    private String userId;

    @Before
    public void before() {
        this.userId = System.currentTimeMillis() + "@temail.com";
        accountService.getAccount(userId);
    }

    @After
    public void after() {

    }

    @Test
    public void testUpdateBalance() {
        Assert.assertEquals(BalanceResultEnums.OK, accountService.updateBalance(userId, new BigDecimal("10")));

        Assert.assertEquals(BalanceResultEnums.OK, accountService.updateBalance(userId, new BigDecimal("-5")));

        Assert.assertEquals(BalanceResultEnums.INSUFFICIENT, accountService.updateBalance(userId, new BigDecimal("-8")));
    }
}