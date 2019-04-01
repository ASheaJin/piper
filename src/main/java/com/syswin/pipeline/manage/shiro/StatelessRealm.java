package com.syswin.pipeline.manage.shiro;

import com.syswin.pipeline.db.model.User;
import com.syswin.pipeline.manage.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2019/4/1.
 */
public class StatelessRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(StatelessRealm.class);

    private TokenManager tokenManager;

    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //根据用户名查找角色，请根据需求实现
        String loginName = (String) principals.getPrimaryPrincipal();
        User user = userService.getUserByLoginName(loginName);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String> selectRoles = userService.getRolesByUserId(String.valueOf(user.getUserId()))
                .stream().map((r) -> String.valueOf(r.getRoleId())).collect(Collectors.toList());
        authorizationInfo.addRoles(selectRoles);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        StatelessToken statelessToken = (StatelessToken)token;
        // 获取用户输入的用户名和密码
        String loginName = (String) token.getPrincipal();
        String credentials = (String)statelessToken.getCredentials();
        boolean checkToken = tokenManager.checkToken(statelessToken);
        if (checkToken) {
            return new SimpleAuthenticationInfo(loginName, credentials, super.getName());
        }else{
            throw new AuthenticationException("token认证失败");
        }
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
