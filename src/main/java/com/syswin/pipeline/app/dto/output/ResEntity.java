package com.syswin.pipeline.app.dto.output;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/1/3 16:41
 */
@Data
public class ResEntity<T> {

	public static String SUCCESS ="200";
	public static String Fail ="500";
	public static String ParamFail ="501";
	//响应码
	private String code;

	//响应信息
	private String msg;

	//响应其他参数
	private T data;

	public ResEntity() {
		this.code = ResEntity.SUCCESS;
		this.msg = "success";

	}

	/**
	 * 失败
	 *
	 * @param code
	 * @param msg
	 */
	public ResEntity(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 成功
	 *
	 * @param data
	 */
	public ResEntity(T data) {
		this.code = ResEntity.SUCCESS;
		this.msg = "success";
		this.data = data;
	}

	/**
	 * 失败
	 *
	 * @param code
	 * @param msg
	 */
	public ResEntity(String code, String msg, T data) {
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
