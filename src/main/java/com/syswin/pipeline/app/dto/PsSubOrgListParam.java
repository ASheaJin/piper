package com.syswin.pipeline.app.dto;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/2/21 18:22
 */
@Data
public class PsSubOrgListParam {
	//出版社Id
	private String publisherId;
	//要改的出版社名字
	private String name;
	//要改的出版社头像，有bug
	private String iconUrl;
}
