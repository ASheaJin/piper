package com.syswin.pipeline.app.dto;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/2/21 18:16
 */
@Data
public class CheckInput {
	private String userId;
	private String keyword;
	private Integer pageSize;
	private Integer pageNo;
}
