package com.syswin.pipeline.utils;

import com.syswin.pipeline.enums.PermissionEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:lhz
 * @date:2019/2/26 10:14
 */
public class PermissionUtil {

	/**
	 * 角色 权限
	 * 000 组织个人
	 */
	public static Map<String, String> map = new HashMap<>();

	static {
		map.put("0000", PermissionEnums.Guest.name);
		map.put("0001", PermissionEnums.Person.name);
		map.put("0010", PermissionEnums.OnlyOrg.name);
		map.put("0011", PermissionEnums.OrgPerson.name);
	}

	public static String getMyVersion(String permission) {
		return map.get(permission);
	}
}
