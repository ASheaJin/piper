package com.syswin.pipeline.manage.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by 115477 on 2019/4/2.
 */
@Data
public class UserRolesInput {
    private String userId;
    private List<String> roleIds;
}
