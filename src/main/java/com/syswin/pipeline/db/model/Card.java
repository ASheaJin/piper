package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 银行卡
 * Created by 115477 on 2018/11/27.
 */
@Data
public class Card extends BaseEntity {
    //银行卡Id
    private String cardId;
    //用户Id （秘邮号）
    @NotNull
    private String userId;
    //银行卡账号
    private String cardNo;
}
