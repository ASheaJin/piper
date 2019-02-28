package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.Account;
import com.syswin.pipeline.db.model.Transaction;
import com.syswin.pipeline.db.repository.AccountRepository;
import com.syswin.pipeline.db.repository.TransactionRepository;
import com.syswin.pipeline.enums.BalanceResultEnums;
import com.syswin.pipeline.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 账号管理服务
 * Created by 115477 on 2018/11/29.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 根据userId获取账号，如果不存在则新建账号
     * @param userId
     * @return
     */
    public Account getAccount(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        Account account = accountRepository.selectByUserId(userId);
        if (account == null) {
            account = new Account();
            account.setAccountId(String.valueOf(SnowflakeIdWorker.getInstance().nextId()));
            account.setBalance(BigDecimal.ZERO);
            account.setUserId(userId);
            account.setUpdateTime(account.getCreateTime());
            accountRepository.insert(account);
            return account;
        }
        return account;
    }

    /**
     * 更新账户的值
     * @param userId
     * @param changeValue 变化的值
     * @return
     */
    @Transactional
    public BalanceResultEnums updateBalance(String userId, BigDecimal changeValue) {
        Account account  = this.getAccount(userId);
        if (account.getBalance().add(changeValue).compareTo(BigDecimal.ZERO) < 0) {
            return BalanceResultEnums.INSUFFICIENT;
        }
        account.setBalance(account.getBalance().add(changeValue));
        int c = accountRepository.update(account);
        if (c == 0) {
            return BalanceResultEnums.FAIL;
        } else {
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setMoney(changeValue);
            transaction.setType(2);
            transaction.setStatus(1);
            transaction.setTransactionId(String.valueOf(SnowflakeIdWorker.getInstance().nextId()));
            transactionRepository.insert(transaction);
        }

        return BalanceResultEnums.OK;
    }
}
