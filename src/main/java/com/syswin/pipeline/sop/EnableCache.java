package com.syswin.pipeline.sop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnableCache {
	/**
	 * key前缀
	 */
	String keyPrefix();

	/**
	 * key主体，spel表示，例：#id（取形参中id的值）
	 */
	String fieldKey();

	CacheOperation cacheOperation();

	/**
	 * 缓存操作类型
	 */
	enum CacheOperation {
		QUERY, // 查询
		UPDATE// 修改
	}
}
