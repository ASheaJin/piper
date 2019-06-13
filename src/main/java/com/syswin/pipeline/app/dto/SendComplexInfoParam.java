package com.syswin.pipeline.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 115477 on 2019/1/21.
 */
@Data
public class SendComplexInfoParam {

	@ApiModelProperty(value = "校验的taken", required = true)
	private String token;
	@ApiModelProperty(value = "消息的title")
	private String title;
	@ApiModelProperty(value = "图片文字")
	private String imgTxt;
	@ApiModelProperty(value = "图片Url")
	private String imgUrl;
	@ApiModelProperty(value = "文字超链接")
	private String url;
	@ApiModelProperty(value = "文字")
	private String txt;
	@ApiModelProperty(value = "出版社账号")
	private String piperTemail;
}
