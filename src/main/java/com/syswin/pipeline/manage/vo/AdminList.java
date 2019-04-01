package com.syswin.pipeline.manage.vo;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/3/30 21:23
 */
@Data
public class AdminList {
	private String userId;
	//出版社名称，或者邮箱模糊查询
	private String keyword;
	private String pageNo;
	private String pageSize;
}
