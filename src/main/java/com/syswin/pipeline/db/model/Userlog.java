package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author:lhz
 * @date:2019/1/10 15:42
 */
@Data
public class Userlog{
	//用户Id （秘邮号）
	@NotNull
	private String userId;

	private String createTime;
	//消息内容
	private String requestData;

}
