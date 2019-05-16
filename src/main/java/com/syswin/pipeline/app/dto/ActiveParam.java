package com.syswin.pipeline.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:lhz
 * @date:2019/5/15 17:33
 */
@Data
public class ActiveParam {

	@ApiModelProperty(value = "秘邮号", required = true)
	private String temail;
	@ApiModelProperty(value = "激活码", required = true)
	private String code;
}
