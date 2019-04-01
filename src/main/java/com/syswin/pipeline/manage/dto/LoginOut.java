package com.syswin.pipeline.manage.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/4/1.
 */
@Data
public class LoginOut {

    private String loginName;
    private String token;

    public LoginOut(String loginName, String token) {
        this.loginName = loginName;
        this.token = token;
    }
}
