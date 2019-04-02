package com.syswin.pipeline.manage.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/4/1.
 */
@Data
public class PasswordInput {
    private String userId;

    private String oldPassword;

    private String newPassword;

}
