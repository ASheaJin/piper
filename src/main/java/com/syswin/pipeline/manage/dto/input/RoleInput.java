package com.syswin.pipeline.manage.dto.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 115477 on 2019/4/2.
 */
@Data
public class RoleInput {

    private String roleId;

    private String roleName;

    private String remark;

    @ApiModelProperty(value = "角色类型 1管理员 2普通")
    private Byte roleType;
}
