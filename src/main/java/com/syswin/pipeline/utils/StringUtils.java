package com.syswin.pipeline.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * 判断该字符串是否为空或者null
	 *
	 * @param str
	 * @return 为null或空返回true，否则返回false
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.length() <= 0 || str.equalsIgnoreCase("null");
	}

	public static int getInteger(String str) {
		return (isNullOrEmpty(str)) ? 0 : Integer.parseInt(str);
	}

	public static long getLong(String str) {
		return (isNullOrEmpty(str)) ? 0 : Long.parseLong(str);
	}

	/**
	 * 获取int类型
	 *
	 * @param request
	 * @param key
	 * @param def     默认值
	 * @return
	 */
	public static int getParam(HttpServletRequest request, String key, int def) {
		String param = request.getParameter(key);
		if (isNullOrEmpty(param)) {
			return def;
		}
		try {
			return Integer.parseInt(param);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 获取double类型
	 *
	 * @param request
	 * @param key
	 * @param def     默认值
	 * @return
	 */
	public static double getParam(HttpServletRequest request, String key, double def) {
		String param = request.getParameter(key);
		if (isNullOrEmpty(param)) {
			return def;
		}
		try {
			return Double.parseDouble(param);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 获取long类型
	 *
	 * @param request
	 * @param key
	 * @param def     默认值
	 * @return
	 */
	public static long getParam(HttpServletRequest request, String key, long def) {
		String param = request.getParameter(key);
		if (isNullOrEmpty(param)) {
			return def;
		}
		try {
			return Long.parseLong(param);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 获取float类型
	 *
	 * @param request
	 * @param key
	 * @param def     默认值
	 * @return
	 */
	public static float getParam(HttpServletRequest request, String key, float def) {
		String param = request.getParameter(key);
		if (isNullOrEmpty(param)) {
			return def;
		}
		try {
			return Float.parseFloat(param);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 获取String类型
	 *
	 * @param request
	 * @param key
	 * @param def     默认值
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String key, String def) {
		String param = request.getParameter(key);
		if (isNullOrEmpty(param)) {
			return def;
		}
		return param;
	}

	/**
	 * 获取Integer类型
	 *
	 * @param request
	 * @param key
	 * @param def     默认值
	 * @return
	 */
	public static Integer getParamInteger(HttpServletRequest request, String key, Integer def) {
		String param = request.getParameter(key);
		if (isNullOrEmpty(param)) {
			return def;
		}
		try {
			return Integer.parseInt(param);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取所有参数
	 *
	 * @param request
	 * @return 2018年6月4日 下午5:10:40
	 */
	public static Map<String, String> getAllParams(HttpServletRequest request) {
		Map<String, String[]> maps = request.getParameterMap();
		Map<String, String> map = new HashMap<>();
		for (Map.Entry<String, String[]> entry : maps.entrySet()) {
			map.put(entry.getKey(), entry.getValue()[0]);
		}
		return map;
	}


	/**
	 * 判断是否全为汉字
	 *
	 * @param str
	 * @return true全为汉字，false包含其他字符
	 */
	public static boolean checkChina(String str) {
		String reg = "[\\u4e00-\\u9fa5]+";
		boolean result1 = str.matches(reg);// false
		return result1;
	}

	/**
	 * 字符串只能包含，英文字母，汉字，和_，。,.?？!！
	 *
	 * @param str
	 * @return true匹配，false不匹配
	 */
	public static boolean isEnZh(String str) {
		Pattern pattern = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_，。,.?？!！\\u4e00-\\u9fa5]+$");
		Matcher matcher = pattern.matcher(str);
		return (matcher.find());
	}
}
