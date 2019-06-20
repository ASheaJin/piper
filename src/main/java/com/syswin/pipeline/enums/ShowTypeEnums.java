package com.syswin.pipeline.enums;

/**
 * Created by 115477 on 2019/6/20.
 */
public enum ShowTypeEnums {
    COMPLEX(1);

    private Integer type;

    ShowTypeEnums(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
