package com.syswin.pipeline.manage.dto.input;

import lombok.Data;

import java.util.List;

/**
 * Created by 115477 on 2019/4/2.
 */
@Data
public class RoleMenusInput {
    private String roleId;
    private List<String> menuIds;
}
