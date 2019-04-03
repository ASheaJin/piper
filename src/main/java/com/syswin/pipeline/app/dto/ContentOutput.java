package com.syswin.pipeline.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "出版社id")
    private String publisherId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "出版社名称")
    private String publisherName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "标题")
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "简介")
    private String intro;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "发布时间")
    private Long publishTime;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "发件人")
    private String from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "收件人")
    private String to;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "附件数量")
    private Integer attachCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "复合消息体的内容列表")
    private List<MediaContent> contentArray;
}
