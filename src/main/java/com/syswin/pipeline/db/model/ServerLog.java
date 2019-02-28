package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 日志记录
 * Created by lhz on 2018/11/27.
 */
@Data
public class ServerLog {
	//银行卡Id
	private String id;
	//用户Id （秘邮号）
	@NotNull
	private String userId;
	//消息体类型 http://wiki.syswin.com/pages/viewpage.action?pageId=33689922
	private int command;
	//消息内容
	private String requestData;

	//响应消息内容
	private String responseData;

	private String createTime;

	private long endTime;
}
