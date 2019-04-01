package com.syswin.pipeline.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:lhz
 * @date:2019/4/1 18:36
 */
public class HeaderUtil {

	public static String getUserId(HttpServletRequest request) {
		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("token");
		if (token != null) {
			return null;
		}
		String userId = "";
		return userId;
	}
}
