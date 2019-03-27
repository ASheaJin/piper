package com.syswin.pipeline.service.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.syswin.pipeline.db.model.Token;
import com.syswin.pipeline.utils.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用于附件上传时生成有时间限制的token，以及判断token是否有效的方法
 * Created by 115477 on 2019/3/27.
 */
@Component
public class TokenGenerator {
    public static final String SPLIT = ":";

    //token 5分钟有效
    private static Cache<String, String> cacheMap = CacheBuilder.newBuilder()
            .expireAfterWrite(5L, TimeUnit.MINUTES)
            .concurrencyLevel(6)
            .initialCapacity(10)
            .softValues()
            .build();

    public String generator(String publisherId, String userId) {
        if (publisherId == null || userId == null) {
            return null;
        }
        String uniqueKey = publisherId + SPLIT + userId;
        String token = cacheMap.getIfPresent(uniqueKey);
        if (token != null) {
            return token;
        }
        String uuid = tryGet();
        cacheMap.put(uniqueKey, uuid);
        cacheMap.put(uuid, uniqueKey);

        return uuid;
    }
    private String tryGet() {
        //尝试十次
        for (int i=0;i<10;i++) {
            String s = TokenUtil.randString(5);
            if (cacheMap.getIfPresent(s) != null) {
                continue;
            }
            return s;
        }
        throw new RuntimeException("生成token失败");
    }

    public String[] getIdsByToken(String token) {
        if (token == null) {
            return null;
        }
        String uniqueKey = cacheMap.getIfPresent(token);
        if (uniqueKey == null) {
            return null;
        }

        return uniqueKey.split(SPLIT);
    }

    public boolean idExist(String publisherId, String userId) {
        String uniqueKey = publisherId + SPLIT + userId;
        return cacheMap.getIfPresent(uniqueKey) != null;
    }

    public boolean tokenExist(String token) {
        return getIdsByToken(token) != null;
    }
}
