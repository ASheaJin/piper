package com.syswin.pipeline.service.psserver.impl;

import com.syswin.temail.ps.common.entity.CDTPPacket;

/**
 * @author:lhz
 * @date:2019/1/2 10:21
 */
public class BusinessException extends RuntimeException{


	private String msg;//错误消息


	public BusinessException(){
		super();
	}
	public BusinessException(String msg){
		super(msg);
	}

	public BusinessException(Exception e,CDTPPacket reqPacket) {
		super(e.getMessage());
		if (e instanceof BusinessException) {
			this.msg = e.getMessage();
		} else {
			this.msg = reqPacket.toString();
		}
	}

	public String getMsg() {
			return this.msg;
		}
}
