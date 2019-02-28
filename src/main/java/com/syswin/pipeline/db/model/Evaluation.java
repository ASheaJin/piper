package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 评价 1~5星
 * Created by 115477 on 2018/11/27.
 */
@Data
public class Evaluation extends BaseEntity {
    //评星Id
    private String evaluationId;
    //评星等级
    private int level;
    @NotNull
    private String userId;

    private String contentId;
}
