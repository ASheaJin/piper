package com.syswin.pipeline.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 用于附件上传时生成有时间限制的token，以及判断token是否有效的方法
 * Created by 115477 on 2019/3/27.
 */
@Component
public class LanguageChange {

	@Autowired
	private MessageSource messageSource;

	public String getValue(String key, Locale locale) {
		String value = messageSource.getMessage(key, null, locale);//获取转换后的字符。需要在messages.properties,messages_enUS.properties,messages.properties 中配置。
		return value;
	}
}
