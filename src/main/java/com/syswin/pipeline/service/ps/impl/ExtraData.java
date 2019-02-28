package com.syswin.pipeline.service.ps.impl;

import lombok.Data;

/**
 * 发送1.1消息时，header的extraData属性
 * Created by 115477 on 2018/12/18.
 */
@Data
public class ExtraData {
    private String from;
    private String to;
    private String msgId;
    private String parentMsgId;
    private Integer storeType;
    private Integer type;

    public ExtraData(String from, String to, String msgId, Integer storeType) {
        this.from = from;
        this.to = to;
        this.msgId = msgId;
        this.parentMsgId = "";
        this.storeType = storeType;
        this.type = 0;
    }
}
