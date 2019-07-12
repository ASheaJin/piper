package com.syswin.pipeline.psservice.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionExtData implements Serializable {
    //"邮件组名称",
    private String name;
    //"avatarHost":"邮件组头像使用的openAvatar服务地址",
    private String avatarHost;
    // 应用类型
    private Integer contactType = 5;
}
