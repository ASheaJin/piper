package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.Token;
import com.syswin.pipeline.db.repository.TokenRepository;
import com.syswin.pipeline.utils.MD5Coder;
import com.syswin.pipeline.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by 115477 on 2018/11/28.
 */
@Service
public class PiperTokenService {
    private static final Logger logger = LoggerFactory.getLogger(PiperTokenService.class);

    @Autowired
    private TokenRepository tokenRepository;

    /**
     * 根据userId获取Token， 如果不存在则生成新的token
     *
     * @param userId
     * @return
     */
    public Token getToken(String userId) {
        Token token = tokenRepository.getTokenByUserId(userId);
        if (token == null) {
            Long now = System.currentTimeMillis();
            String tokenStr = encryptToken(userId);

            Token newToken = new Token();
            newToken.setToken(tokenStr);
            newToken.setUpdateTime(newToken.getCreateTime());
            newToken.setUserId(userId);
            tokenRepository.addToken(newToken);
            return newToken;
        } else {
            return token;
        }
    }

    /**
     * 检查userId与token是否匹配
     *
     * @param userId
     * @param token
     * @return
     */
    public boolean checkToken(String userId, String token) {
        Token tokenInfo = tokenRepository.selectByToken(token);
        if (tokenInfo == null || !tokenInfo.getUserId().equals(userId)) {
            return false;
        }
        return true;
    }

    private String encryptToken(String userId) {
        try {
            return TokenUtil.randString(8) + MD5Coder.EncoderByMd5(userId + String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            logger.error(String.format("生成Token出错 userid=%s userAgent=%s", userId), e);
        }
        return null;
    }
}
