package com.syswin.pipeline.utils;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.syswin.ps.sdk.service.CrossDomainService;
import com.syswin.ps.sdk.service.KmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PSUtil {

    @Autowired
    private CrossDomainService domainService;

    @Autowired
    private KmsService kmsService;

    @Value("${app.ps-app-sdk.tenant-id}")
    private String tentId;


    private Cache<String, String> myMap = CacheBuilder.newBuilder()
            .expireAfterAccess(Integer.MAX_VALUE, TimeUnit.DAYS)
            .expireAfterWrite(Integer.MAX_VALUE, TimeUnit.DAYS)

            .concurrencyLevel(6)
            .initialCapacity(1000)
            .maximumSize(10000)
            .softValues()
            .build();


    public String publickey(String userId) {
        if (StringUtil.isEmpty(userId)) {
            throw new RuntimeException(" userId is null");
        }
        //1 、查询缓存
        String pk = myMap.getIfPresent(userId);
        if (StringUtil.isEmpty(pk)) {
            //网关auth查询
            pk = domainService.publishKey(userId);

            if (pk == null) {
                //TODO kms查询,第二个参数不知干嘛,tentId?
                pk = kmsService.sign(userId, tentId);
            }
            //缓存注入
            myMap.put(userId, pk);
        }
        return pk;
    }


    public String sign(String userId) {
        return  kmsService.sign(userId, "aaaa");
    }

    /**
     * 判断该号是否在 网关存在
     * @param userId
     * @return
     */
    public boolean isExist(String userId) {
        if (StringUtil.isEmpty(userId)) {
            throw new RuntimeException(" userId is null");
        }
        if (StringUtil.isEmpty(domainService.publishKey(userId))) {
            return false;
        }
        return true;
    }
}
