package com.syswin.pipeline.manage.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/4/2.
 */
@Data
public class PageListInput {
    private Integer pageSize = 30;
    private Integer pageNo = 1;
}
