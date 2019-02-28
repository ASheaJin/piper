package com.syswin.pipeline.enums;

/**
 * Created by 115477 on 2019/1/4.
 */
public enum BalanceResultEnums {
    //正常返回
    OK,
    //余额不足，扣减失败
    INSUFFICIENT,
    //账户版本号异常，扣减失败
    FAIL
    ;
}
