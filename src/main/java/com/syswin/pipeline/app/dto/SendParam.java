package com.syswin.pipeline.app.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/1/21.
 */
@Data
public class SendParam {
    private String content;
    /**
     * 出版社账号
     */
    private String piperTemail;
}
