package com.syswin.pipeline.manage.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/4/1.
 */
@Data
public class LoginOutput {

    private Boolean success;
    private String userId;
    private String loginName;
    private String token;

    public LoginOutput() {
    }

    public LoginOutput(String userId, String loginName, String token) {
        this.success = true;
        this.userId = userId;
        this.loginName = loginName;
        this.token = token;
    }

    public static LoginOutput fail() {
        LoginOutput l = new LoginOutput();
        l.setSuccess(false);
        return l;
    }
}
