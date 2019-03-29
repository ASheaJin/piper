package com.syswin.pipeline.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

/**
 * @author:lhz
 * @date:2019/3/29 9:55
 */

public class MyLocalResolver implements LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = Locale.getDefault();
		if (request == null) {
			return locale;
		}
		String l = request.getParameter("l");

		if (!StringUtils.isEmpty(l)) {
			String[] split = l.split("_");
			locale = new Locale(split[0], split[1]);

		}
		return locale;
	}


	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// TODO Auto-generated method stub
	}

}
