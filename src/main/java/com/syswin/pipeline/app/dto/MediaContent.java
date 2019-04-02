package com.syswin.pipeline.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 115477 on 2019/4/2.
 */
@Data
public class MediaContent {
    @ApiModelProperty(value = "消息体内容类型 1,文本 2,语音 3,图片 4,名片 5,地图位置 7,系统消息（toon群聊加减人灰条消息，toon由后端下发） 10,视频 12,gif图片 14,文件 15,分享 22,邮件eml格式传输 23,命令操作类消息，如：语音视频通道 30,话题复合消息体")
    private int bodyType;

    private String text;
    @ApiModelProperty(value = "后缀")
    private String suffix;

    private int time;

    private int w;

    private int h;

    @ApiModelProperty(value = "文件 图片和视频的url")
    private String url;
    @ApiModelProperty(value = "文件的名称")
    private String desc;
    @ApiModelProperty(value = "文件的格式")
    private String format;
}
