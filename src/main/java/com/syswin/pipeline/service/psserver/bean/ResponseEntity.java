package com.syswin.pipeline.service.psserver.bean;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/1/3 16:41
 */
@Data
public class ResponseEntity<T> {

	//响应码
	private String code;

	//响应信息
	private String msg;

	//响应其他参数
	private T data;
	public ResponseEntity() {
		this.code = "200";
		this.msg = "操作成功";
	}

	/**
	 * 失败
	 * @param code
	 * @param msg
	 */
	public ResponseEntity(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 成功
	 * @param data
	 */
	public ResponseEntity( T data) {
		this.code = "200";
		this.msg = "操作成功";
		this.data = data;
	}

	/**
	 * 失败
	 * @param code
	 * @param msg
	 */
	public ResponseEntity(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResponseBean{" +
						"code='" + code + '\'' +
						", msg='" + msg + '\'' +
						", data=" + data +
						'}';
	}
}
