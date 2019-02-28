package com.syswin.pipeline.app.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/1/21.
 */
@Data
public class PublishMessageParam {
    private String content;
    private String bodyType;
    /**
     * 出版社账号
     */
    private String ptemail;
    /**
     * 出版社的用户账号
     */
    private String fromTemail;
}
