package com.syswin.pipeline.service.ps.impl;

import lombok.Data;

/**
 * Created by 115477 on 2018/12/17.
 */
@Data
public class MessageResult {
    private String from;
    private String to;
    private Integer status;
    private Integer type;
    private String message;
    private String at;
    private String topic;
    private String msgId;
    private Long seqId;
    private Long timestamp;

    private Long pageSize;
    private String signal = "after";

    protected static Boolean isNull(MessageResult result) {
        return result == null;
    }
}
