package com.syswin.pipeline.manage.dto.input;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/4/8 20:08
 */
@Data
public class ContentListInput {

	private String keyword;
	private String publiserId;
	private String pageNo;
	private String pageSize;
}