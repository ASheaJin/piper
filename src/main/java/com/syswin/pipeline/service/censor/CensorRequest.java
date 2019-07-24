package com.syswin.pipeline.service.censor;

import lombok.Data;

/**
 * Created by 115477 on 2019/7/23.
 */
@Data
public class CensorRequest {
    private String id;
    private String[] texts;
    private boolean is_tokenized = false;
}
