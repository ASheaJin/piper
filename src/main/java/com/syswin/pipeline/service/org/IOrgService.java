package com.syswin.pipeline.service.org;

/**
 * Created by 115477 on 2019/2/20.
 */
public interface IOrgService {

    public OrgOut getOrgByVersion(String fromTemail, long version);
}
