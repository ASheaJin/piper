package com.syswin.pipeline.service.org.impl;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by 115477 on 2019/2/20.
 */
@Data
public class OrgData {

    private Long version;

    private String icon;

    private String title;

    private String clearOldVer;

    private String[] tags;

    private Map<String, List<OrgDataTemail>> cipherContacts;
}
