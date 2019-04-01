package com.syswin.pipeline.manage.dto;

import lombok.Data;

/**
 * Created by 115477 on 2019/4/1.
 */
@Data
public class MenuOut {

    private String menuId;

    private String parentId;

    private String name;

    private Byte isLeaf;
}
