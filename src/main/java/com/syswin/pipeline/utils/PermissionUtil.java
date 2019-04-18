package com.syswin.pipeline.utils;

import com.syswin.pipeline.enums.AppmuneEnum;

/**
 * @author:lhz
 * @date:2019/4/17 10:27
 */
public class PermissionUtil {


	public static boolean getMenuPromission(String configs, String env) {
		if (StringUtils.isNullOrEmpty(configs) || StringUtils.isNullOrEmpty(env)) {
			return false;
		}
		if (configs.contains(env)) {
			return true;
		}
		return false;
	}

	public static boolean getdomains(String configs, String userId) {
		if (StringUtils.isNullOrEmpty(configs) || StringUtils.isNullOrEmpty(userId)) {
			return false;
		}
		String[] domains = configs.split(",");
		for (int i = 0; i < domains.length; i++) {
			if (userId.contains(domains[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean getPipterPromission(String configs, String env) {
		if (StringUtils.isNullOrEmpty(configs) || StringUtils.isNullOrEmpty(env)) {
			return false;
		}
		if (configs.contains(env)) {
			return true;
		}
		return false;
	}

	public static boolean checkmenus(Integer allPermission, Integer menuInt) {
		if (allPermission == null || menuInt == null) {
			return false;
		}
		return (allPermission & menuInt) == menuInt;
	}

}
