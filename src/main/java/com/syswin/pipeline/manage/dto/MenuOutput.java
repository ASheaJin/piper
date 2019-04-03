package com.syswin.pipeline.manage.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/4/1.
 */
@Data
public class MenuOutput {

    private String menuId;

    private String parentId;

    private String name;

    private Byte isLeaf;

    private String url;
}
