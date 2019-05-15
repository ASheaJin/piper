package com.syswin.pipeline.enums;

/**
 * @author:lhz
 * @date:2019/4/18 10:26
 */
public enum AppmuneEnum {
	person(1),
	mypublish(2),
	recommend(4),
	mysublist(8),
	group(16);


	public Integer type;

	AppmuneEnum(Integer type) {
		this.type = type;
	}
}
