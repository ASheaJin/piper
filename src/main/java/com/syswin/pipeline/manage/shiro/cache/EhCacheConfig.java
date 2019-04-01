package com.syswin.pipeline.manage.shiro.cache;


import java.lang.invoke.MethodHandles;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EhCacheConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CacheManager cacheManager;

    static EhCacheConfig getInstance() {
        return EhCacheConfig.SingletonHelper.INSTANCE;
    }

    private EhCacheConfig() {
        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        this.cacheManager.init();
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    CacheManager cacheManager() {
        return this.cacheManager;
    }

    private void close() {
        try {
            this.cacheManager.close();
        } catch (Exception var2) {
            LOG.error("Failed to close cache manager", var2);
        }

    }

    private static final class SingletonHelper {
        private static final EhCacheConfig INSTANCE = new EhCacheConfig();

        private SingletonHelper() {
        }
    }
}
