package com.syswin.pipeline.db.model;

import lombok.Data;

@Data
public class DeviceInfo {
    private Integer id;

    private String userid;

    private String language;

    private String platform;

    private String moduleversion;

    private String version;

    private String osVersion;

    private String appversion;

    private String build;

}