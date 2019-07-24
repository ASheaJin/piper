package com.syswin.pipeline.service.censor;

import lombok.Data;

import java.util.List;

/**
 * Created by 115477 on 2019/7/23.
 */
@Data
public class CensorResponse {
    private String id;
    private Integer status;
    private List<CensorResponseResult> result;

    public static class CensorResponseResult {
        private String l;
        private String s;

        public String getL() {
            return l;
        }

        public void setL(String l) {
            this.l = l;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }
}

