package com.syswin.pipeline.service.org;

import lombok.Data;

/**
 * Created by 115477 on 2019/2/20.
 */
@Data
public class EmployeeOut {
    public String name;

    private String temail;

    private String title;

    public EmployeeOut(String name, String temail, String title) {
        this.name = name;
        this.temail = temail;
        this.title = title;
    }
}
