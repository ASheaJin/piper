package com.syswin.pipeline.utils;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;

import java.util.Collection;
import java.util.List;

/**
 * @author:lhz
 * @date:2019/3/4 16:17
 */
public class BeanConvertUtil {
	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * 基于Dozer转换对象的类型.
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		if(sourceList==null ){
			return  destinationList;
		}
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 */
	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}

}
