package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 交易记录
 * Created by 115477 on 2018/11/27.
 */
@Data
public class Transaction extends BaseEntity {
    //交易记录Id
    private String transactionId;
    //账户Id
    @NotNull
    private String userId;
    //金额
    private BigDecimal money;
    //交易状态：0未完成，1已完成
    private int status;
    //交易类型 1充值 2扣款
    private int type;
}
