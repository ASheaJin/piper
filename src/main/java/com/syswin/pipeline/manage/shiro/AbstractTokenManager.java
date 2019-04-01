package com.syswin.pipeline.manage.shiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
/**
 * Created by 115477 on 2019/4/1.
 */
public abstract class AbstractTokenManager implements TokenManager {


    protected final Logger logger = LoggerFactory.getLogger(AbstractTokenManager.class);

    protected String userTokenPrefix ="token_";

    @Autowired
    protected TokenCacheManager tokenCacheManager;

    @Override
    public StatelessToken createToken(String userCode) {
        StatelessToken tokenModel = null;
        String token = tokenCacheManager.getUserToken(userTokenPrefix+userCode);
        if(StringUtils.isEmpty(token)){
            token = createStringToken(userCode);
        }
        tokenCacheManager.updateUserToken(userTokenPrefix+userCode, token);
        tokenModel = new StatelessToken(userCode, token);
        return tokenModel;
    }

    public abstract String createStringToken(String userCode);

    protected boolean checkMemoryToken(StatelessToken model) {
        if(model == null){
            return false;
        }
        String loginName = (String)model.getPrincipal();
        String credentials = (String)model.getCredentials();
        String token = tokenCacheManager.getUserToken(userTokenPrefix+loginName);
        if (token == null || !credentials.equals(token)) {
            return false;
        }
        return true;
    }

    @Override
    public StatelessToken getToken(String authentication){
        if(StringUtils.isEmpty(authentication)){
            return null;
        }
        String[] au = authentication.split("_");
        if (au.length <=1) {
            return null;
        }
        String userCode = au[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < au.length; i++) {
            sb.append(au[i]);
            if(i<au.length-1){
                sb.append("_");
            }
        }
        return new StatelessToken(userCode, sb.toString());
    }

    @Override
    public boolean check(String authentication) {
        StatelessToken token = getToken(authentication);
        if(token == null){
            return false;
        }
        return checkMemoryToken(token);
    }

    @Override
    public void deleteToken(String userCode) {
        tokenCacheManager.deleteUserToken(userTokenPrefix+userCode);
    }
}
