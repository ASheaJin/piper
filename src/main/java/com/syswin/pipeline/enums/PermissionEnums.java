package com.syswin.pipeline.enums;

/**
 * @author:lhz
 * @date:2019/2/26 10:52
 */
public enum PermissionEnums {

	//游客
	Guest("-1"),
	//游客
	Reader("0"),
	//个人出版社
	Person("1"),
	//组织出版社单不是个人
	OnlyOrg("2"),
	//两者都是
	OrgPerson("3");

	PermissionEnums(String name) {

		this.name = name;
	}

	public String name;
}
