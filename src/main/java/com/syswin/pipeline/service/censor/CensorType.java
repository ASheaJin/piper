package com.syswin.pipeline.service.censor;

/**
 * Created by 115477 on 2019/7/23.
 */
public enum CensorType {
    Content("1"),
    Publisher("2")
    ;

    private String code;

    CensorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
