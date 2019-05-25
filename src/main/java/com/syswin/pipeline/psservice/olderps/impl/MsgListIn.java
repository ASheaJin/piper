package com.syswin.pipeline.psservice.olderps.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 1.2方法的参数，对应 AppConstant.synsMsgListComId
 * Created by 115477 on 2018/12/17.
 */
@Data
@AllArgsConstructor
public class MsgListIn {
    private String from;
}