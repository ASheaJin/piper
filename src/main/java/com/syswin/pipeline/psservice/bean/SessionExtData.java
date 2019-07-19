package com.syswin.pipeline.psservice.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionExtData implements Serializable {
    //"邮件组名称",
    private String name;
    //"avatarHost":"邮件组头像使用的openAvatar服务地址",
    private String avatarHost;
    // 应用类型 邮件组消息 5， 应用消息4
    // 应用类型 邮件组消息 5， 应用消息4
    private Integer contactType = 4;
}
