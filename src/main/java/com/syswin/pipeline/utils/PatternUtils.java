package com.syswin.pipeline.utils;

/**
 * @author:lhz
 * @date:2019/1/14 17:23
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
	private static final String rgex = "p.(.*?)@t.email";

	/**
	 * 获取邮箱
	 *
	 * @param content
	 * @param end
	 * @return
	 */
	public static String getEmail(String content, String end) {
		return "p." + content + "@" + end;
	}

	public static String getrex(String end) {
		return "p.(.*?)@" + end;
	}

	/**
	 *  正则表达式匹配两个指定字符串中间的内容
	 *   @param soap
	 *   @return
	 *     
	 */


	public static List<String> getSubUtil(String soap, String end) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(getrex(end));// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			int i = 1;
			list.add(m.group(i));
			i++;
		}
		return list;
	}

	/**
	 *  返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
	 *  @param soap
	 *  @param rgex
	 *  @return
	 *     
	 */
	public static String getSubUtilSimple(String soap, String end) {
		Pattern pattern = Pattern.compile(getrex(end));// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * 根据,;空格过滤获得邮箱号
	 *
	 * @param comBairuserIds
	 * @return
	 */
	public static List<String> tranStrstoList(String comBairuserIds) {

//        Card card = new Card();
//        card.setCardId(1l);
//        card.setCardNo("1111");
//        card.setUserId("111@temail.com");
//        cardRepository.update(card);
//		String comBairuserIds = "jsdjk, , sdjk11 dsjk ;; ,  dsjkds1 , sdjk";
//        String regex = "\\s+\\,*\\;*|\\s+\\;*\\,*|\\,+\\s*\\;*|\\,+\\;*\\s*|\\;+\\s*\\,*|\\;+\\s*\\,*";
		String regex = "\\s+|\\,+|\\，+|\\;+|\\；+";
		String[] arr = comBairuserIds.split(regex);
		List<String> aList = new ArrayList<>();
		for (String t : arr) {
			if (!StringUtils.isNullOrEmpty(t.trim())) {
				aList.add(t);
			}
		}
		return aList;
	}

	public static boolean orEmail(String email) {
		if (email == null || "".equals(email)) return false;
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		return email.matches(regex);
	}
	/**
	 *  测试
	 *  @param args
	 *     
	 */
	public static void main(String[] args) {
		String str = "p.aa啊啊10000010@t.email";
		String str1 = "p.10000010@systoontest.com";
		System.out.println(orEmail(str));
	}

}




