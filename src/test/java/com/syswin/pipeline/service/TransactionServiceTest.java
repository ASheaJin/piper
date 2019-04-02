package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.Transaction;
import com.syswin.pipeline.db.repository.TransactionRepository;
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
public class TransactionServiceTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void insert() {
        Transaction transaction = new Transaction();
        transaction.setMoney(new BigDecimal("2.1"));
        transaction.setStatus(1);
        transaction.setTransactionId(1l+"");
        transaction.setUserId("3222@temail.com");
        transactionRepository.insert(transaction);
    }

    @Test
    public void update() {
//        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
//        Transaction transaction = new Transaction();
//        transaction.setMoney(new BigDecimal("2.1"));
//        transaction.setStatus(1);
//        transaction.setThrOrederId("1111");
//        transaction.setTransactionId(1l);
//        transaction.setUserId("3222@temail.com");
//        transactionRepository.update(transaction);
    }

    @Test
    public void selectbyId() {

//        System.out.println("Account :" + accountRepository.selectById(1l));
    }

    @Test
    public void select() {

        System.out.println("Account :" + transactionRepository.select().toString());
    }
}