package com.syswin.pipeline.psservice.impl;

import com.syswin.ps.sdk.message.IMsgFilterHandler;
import org.springframework.stereotype.Service;

//@Service
public class MsgFilterService implements IMsgFilterHandler {


    @Override
    public Boolean reject() {
        return null;
    }

    @Override
    public String rejectMsg() {
        return null;
    }
}
