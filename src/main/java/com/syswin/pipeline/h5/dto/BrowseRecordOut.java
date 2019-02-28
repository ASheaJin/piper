package com.syswin.pipeline.h5.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/1/17.
 */
@Data
public class BrowseRecordOut {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小
     */
    private String size;
    /**
     * 出版社名称
     */
    private String pname;
    /**
     * 出版社
     */
    private String ptemail;
    /**
     * 发布时间
     */
    private int createTime;
}
