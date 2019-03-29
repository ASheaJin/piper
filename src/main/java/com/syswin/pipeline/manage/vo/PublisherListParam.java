package com.syswin.pipeline.manage.vo;

import lombok.Data;

/**
 * Created by 115477 on 2019/1/21.
 */
@Data
public class PublisherListParam {
    private String userId;
    //出版社名称，或者邮箱模糊查询
    private String keyword;
    private String pageNo;
    private String pageSize;
}
