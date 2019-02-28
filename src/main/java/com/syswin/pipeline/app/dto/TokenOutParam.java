package com.syswin.pipeline.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 115477 on 2018/10/18.
 */
@ApiModel
@Data
public class TokenOutParam {
    @ApiModelProperty(value = "token")
    private String token;

    public TokenOutParam() {
    }

    public TokenOutParam(String token) {
        this.token = token;
    }

}
