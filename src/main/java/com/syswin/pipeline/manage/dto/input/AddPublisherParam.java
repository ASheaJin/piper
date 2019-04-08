package com.syswin.pipeline.manage.dto.input;

import lombok.Data;

/**
 * Created by 115477 on 2019/1/21.
 */
@Data
public class AddPublisherParam {

    private String userId;
    private String publishName;
    private String publishMail;
    private String piperType;
}
