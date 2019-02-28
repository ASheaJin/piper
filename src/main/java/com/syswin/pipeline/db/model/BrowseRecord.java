package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 浏览记录
 * Created by 115477 on 2018/11/27.
 */
@Data
public class BrowseRecord extends BaseEntity {
    //浏览Id
    private String browseId;
    //用户Id （秘邮号）
    @NotNull
    private String userId;
    @NotNull
    private String fromtemail;
    //文章Id
    private String contentId;
    //推送时间
    private int browseTime;
}
