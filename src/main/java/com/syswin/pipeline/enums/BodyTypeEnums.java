package com.syswin.pipeline.enums;

/**
 * Created by 115477 on 2019/4/3.
 */
public enum BodyTypeEnums {
    TEXT(1),
    VOICE(2),
    PIC(3),
    CARD(4),
    MAP(5),
    SYSTEM(7),
    VIDEO(10),
    GIF(12),
    FILE(14),
    SHARE(15),
    MAIL(22),
    OP(23),
    COMPOSE(30);

    private Integer type;

    BodyTypeEnums(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
