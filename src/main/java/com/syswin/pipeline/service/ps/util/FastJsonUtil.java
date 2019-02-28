package com.syswin.pipeline.service.ps.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by qiding on 2018/1/22.
 */
public class FastJsonUtil {

	/**
	 * 转化成json
	 *
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> String toJson(T t) {
		if (t == null) {
			return null;
		} else {
			return JSON.toJSONString(t);
		}
	}


	/**
	 * json转化对象
	 *
	 * @param text
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T parseObject(String text, Class<T> clazz) {
		if (text == null) {
			return null;
		}
		return JSON.parseObject(text, clazz);
	}


	/**
	 * json转化对象
	 *
	 * @param text
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T fromJson(String text, Class<T> clazz) {
		if (text == null) {
			return null;
		}
		return JSON.parseObject(text, clazz);
	}


	/**
	 * 对象转化
	 *
	 * @param u
	 * @param clazz
	 * @param <U>
	 * @param <V>
	 * @return
	 */
	public static <U, V> V convertObj(U u, Class<V> clazz) {
		return fromJson(toJson(u), clazz);
	}


	/**
	 * json转化对象
	 *
	 * @param text
	 * @param <T>
	 * @return
	 */
	public static <T> T fromJson(String text, Type type) {
		if (text == null) {
			return null;
		}
		return JSON.parseObject(text, type, new Feature[0]);
	}


	/**
	 * json转为为数组
	 *
	 * @param text
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> parseArray(String text, Class<T> clazz) {
		if (text == null) {
			return new ArrayList<T>();
		}
		return JSON.parseArray(text, clazz);
	}


	/**
	 * 转化为JSONobject
	 *
	 * @param text
	 * @return
	 */
	public static JSONObject parseObject(String text) {
		if (text == null) {
			return null;
		}
		return JSON.parseObject(text);
	}


	/**
	 * 转化成jsonArray
	 *
	 * @param text
	 * @return
	 */
	public static JSONArray parseArray(String text) {
		if (text == null) {
			return new JSONArray();
		}
		return JSON.parseArray(text);
	}


}
