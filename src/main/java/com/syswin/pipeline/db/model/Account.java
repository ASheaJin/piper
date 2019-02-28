package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 账户
 * Created by 115477 on 2018/11/27.
 */
@Data
public class Account extends BaseEntity {
    //账户Id
    private String accountId;
    //用户Id （秘邮号）
    @NotNull
    private String userId;
    //余额（秘票）
    private BigDecimal balance;
    //最后更新时间，也用于版本
    private int updateTime;
}
