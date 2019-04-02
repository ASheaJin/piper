package com.syswin.pipeline.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 115477 on 2019/4/2.
 */
@Data
public class ContentIdInput {
    @ApiModelProperty(value = "contentId")
    private String contentId;
}
