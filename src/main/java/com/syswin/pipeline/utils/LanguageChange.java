package com.syswin.pipeline.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 用于附件上传时生成有时间限制的token，以及判断token是否有效的方法
 * Created by 115477 on 2019/3/27.
 */
@Component
public class LanguageChange {

	@Autowired
	private MessageSource messageSource;

	public String getLang(String userId) {

		return  "zh";
	}

	public String getLangByUserId(String key, @Nullable Object[] args, String userId) {
		Locale locale = Locale.SIMPLIFIED_CHINESE;

		String value = messageSource.getMessage(key, args, locale);//获取转换后的字符。需要在messages.properties,messages_enUS.properties,messages.properties 中配置。
		return value;
	}


	public String getValueByUserId(String key, String userId) {
		Locale locale = Locale.SIMPLIFIED_CHINESE;

		String value = messageSource.getMessage(key, null, locale);//获取转换后的字符。需要在messages.properties,messages_enUS.properties,messages.properties 中配置。
		return value;
	}


	public String getLangByStr(String key, String lang) {
		Locale locale = Locale.SIMPLIFIED_CHINESE;
		if ("en".equals(lang)) {
			locale = Locale.US;
		}
		String value = messageSource.getMessage(key, null, locale);//获取转换后的字符。需要在messages.properties,messages_enUS.properties,messages.properties 中配置。
		return value;
	}

	public String getUrl(String url, String userId) {
		return url + "?userId=" + userId + "&lang=" + getLang(userId);
	}
}
