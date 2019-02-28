package com.syswin.pipeline.service.org.impl;

import lombok.Data;

/**
 * Created by 115477 on 2019/2/20.
 */
@Data
public class OrgRequest {
    private String temail;
    private String pubKey;
    private Long version;

    public OrgRequest(String temail, String pubKey, Long version) {
        this.temail = temail;
        this.pubKey = pubKey;
        this.version = version;
    }
}
