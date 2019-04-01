package com.syswin.pipeline.manage.shiro.cache;

import com.syswin.pipeline.manage.shiro.TokenCacheManager;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by 115477 on 2019/4/1.
 */
@Component
public class TokenCacheManagerImpl implements TokenCacheManager {
    /**
     * 失效时间 单位秒
     */
    protected long expirateTime = 600;

    private final CacheManager cacheManager = EhCacheConfig.getInstance().cacheManager();
    private final Cache<String, String> cache;

    public TokenCacheManagerImpl() {
        this.cache = this.createCache("ehcache-token-", 100000);
    }

    /**
     * 暂时设置100000个长度，无过期时间
     * @param cacheName
     * @param entries
     * @return
     */
    private Cache<String, String> createCache(String cacheName, int entries) {
        CacheConfiguration<String, String> cacheConfiguration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap((long)entries)).withExpiry(ExpiryPolicyBuilder.noExpiration()).build();
        return this.cacheManager.createCache(cacheName, cacheConfiguration);
    }

    @Override
    public String getUserToken(String loginName) {
        return cache.get(loginName);
    }

    @Override
    public void updateUserToken(String loginName, String token) {
        cache.put(loginName, token);
    }

    @Override
    public void deleteUserToken(String loginName) {
        cache.remove(loginName);
    }
}
