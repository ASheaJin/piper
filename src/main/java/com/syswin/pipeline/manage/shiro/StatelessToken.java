package com.syswin.pipeline.manage.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by 115477 on 2019/4/1.
 */
public class StatelessToken implements AuthenticationToken {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String loginName;

    private String token;

    public StatelessToken(String userCode, String token){
        this.loginName = userCode;
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return loginName;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getToken() {
        return token;
    }

}
