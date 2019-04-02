package com.syswin.pipeline.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * http://wiki.syswin.com/pages/viewpage.action?pageId=35986025
 * Created by 115477 on 2019/4/2.
 */
@Data
public class ContentOutput extends MediaContent {

    public ContentOutput(String contentId) {
        this.contentId = contentId;
    }

    @ApiModelProperty(value = "contentId")
    private String contentId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "发布时间")
    private Long publishTime;


    @ApiModelProperty(value = "发件人")
    private String from;
    @ApiModelProperty(value = "收件人")
    private String to;
    @ApiModelProperty(value = "附件数量")
    private int attachCount;

    @ApiModelProperty(value = "复合消息体的内容列表")
    private List<MediaContent> contentArray;
}
