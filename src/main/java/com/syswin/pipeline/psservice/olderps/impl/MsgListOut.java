package com.syswin.pipeline.psservice.olderps.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 1.2方法的返回，对应 AppConstant.synsMsgListComId
 * Created by 115477 on 2018/12/17.
 */
@Data
@AllArgsConstructor
public class MsgListOut {
    private Integer sessionType;

    private String to;

    private String title;

    private Boolean onTop;

    private MessageResult lastMsg;
}
