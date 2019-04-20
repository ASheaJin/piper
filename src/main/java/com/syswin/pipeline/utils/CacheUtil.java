package com.syswin.pipeline.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 　Guava Cache有两种创建方式：
 * <p>
 * 　　1. cacheLoader 　　2. callable callback
 * <p>
 * 　　通过这两种方法创建的cache，和通常用map来缓存的做法比，不同在于，这两种方法都实现了一种逻辑——从缓存中取key
 * X的值，如果该值已经缓存过了，则返回缓存中的值
 * ，如果没有缓存过，可以通过某个方法来获取这个值。但不同的在于cacheloader的定义比较宽泛，是针对整个cache定义的
 * ，可以认为是统一的根据key值load value的方法。而callable的方式较为灵活，允许你在get的时候指定。
 *
 * @author liangrui
 */
public class CacheUtil {

	private static Cache<String, String> myMap = CacheBuilder.newBuilder()
					.expireAfterAccess(30L, TimeUnit.DAYS)
					.expireAfterWrite(30L, TimeUnit.DAYS)

					.concurrencyLevel(6)
					.initialCapacity(1000)
					.maximumSize(10000)
					.softValues()
					.build();

	public static void put(String key, String value) {
		myMap.put(key, value);
	}
	public static String get(String key) {
		return myMap.getIfPresent(key);
	}


}
