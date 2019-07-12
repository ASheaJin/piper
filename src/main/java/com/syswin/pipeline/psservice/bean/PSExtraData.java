package com.syswin.pipeline.psservice.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PSExtraData implements Serializable {

    private String from;
    private String to;
    private String msgId;
    private String parentMsgId;
    private Integer storeType;
    private String type;
    private String sessionExtData;
}
