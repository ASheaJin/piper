package com.syswin.pipeline.utils;

import org.springframework.util.StringUtils;

/**
 * 轮询开关
 *
 * @author:lhz
 * @date:2018/10/31 21:59
 */
public class SwithUtil {

	//出版社申请前缀

	public static String TURNPS = "no";
	public static boolean ISLOG = false;
	public static String MD5 = "8812C36AA5AE336C2A77BF63211D899A";
	public static String cornPeriod = "*/2 * * * * ?";


	//3到10位
	public static boolean isNumericOrChar(String str) {
		if (!(!StringUtils.isEmpty(str) && str.length() < 11 && str.length() > 2)) {
			return false;
		}
		String regex = "^[a-z0-9A-Z]+$";//其他需要，直接修改正则表达式就好
		return str.matches(regex);
	}

	//2~10位
	public static boolean isLessTenLength(String str) {
		System.out.println(str.length());
		if (!(!StringUtils.isEmpty(str) && str.length() < 11 && str.length() > 2)) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(isLessTenLength("张三李四王二麻子"));
	}
}
