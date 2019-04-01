package com.syswin.pipeline.manage.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/4/1.
 */
@Data
public class RoleOut {
    private Long roleId;

    private String roleName;

    private String remark;

    private Byte roleType;
}
