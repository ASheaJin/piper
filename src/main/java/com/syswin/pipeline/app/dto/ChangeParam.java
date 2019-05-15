package com.syswin.pipeline.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:lhz
 * @date:2019/5/15 17:33
 */
@Data
public class ChangeParam {

	@ApiModelProperty(value = "当前用户的秘邮", required = true)
	private String curUserId;
	@ApiModelProperty(value = "切换的秘邮号", required = true)
	private String changeUserId;
	@ApiModelProperty(value = "当前出版社Id", required = true)
	private String publishId;
}
