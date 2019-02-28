package com.syswin.pipeline.app.dto;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/2/25 18:23
 */
@Data
public class SubSearchParam {

	//模糊搜索
	private String keyword;

	//模糊搜索
	private String publisherId;
	//操作人userId
	private String userId;

	private String pageNo;

	private String pageSize;

}
