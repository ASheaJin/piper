package com.syswin.pipeline.app.dto;

import lombok.Data;

import java.util.List;

/**
 * @author:lhz
 * @date:2019/2/25 18:23
 */
@Data
public class PubliserIdListParam {

	//操作人userId
	private List<String> publiserIds;
	
}
