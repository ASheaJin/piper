package com.syswin.pipeline.service.org.impl;

import lombok.Data;

/**
 * Created by 115477 on 2019/2/20.
 */
@Data
public class OrgResponse {
    private int code;

    private String message;

    private OrgData data;
}
