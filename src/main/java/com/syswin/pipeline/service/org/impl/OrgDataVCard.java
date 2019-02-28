package com.syswin.pipeline.service.org.impl;

import lombok.Data;

/**
 * Created by 115477 on 2019/2/20.
 */
@Data
public class OrgDataVCard {

    private String name;
    private String title;
    private String org;

    public OrgDataVCard(String name, String title, String org) {
        this.name = name;
        this.title = title;
        this.org = org;
    }
}
