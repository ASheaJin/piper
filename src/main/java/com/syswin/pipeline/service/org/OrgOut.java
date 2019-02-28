package com.syswin.pipeline.service.org;

import lombok.Data;

import java.util.List;

/**
 * Created by 115477 on 2019/2/20.
 */
@Data
public class OrgOut {
    private String name;

    private List<OrgOut> subOrg;

    private List<EmployeeOut> employees;
}
