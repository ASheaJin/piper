package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.nickName.INickNameService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NickNameService implements INickNameService {

    @Value("${app.pipeline.userId}")
    private String piper;

    @Autowired
    private PublisherService publisherService;


    @Override
    public String getNickName(String userId) {
        MsgHeader header = PsClientKeeper.msgHeader();

        if (header.getReceiver().equals(piper)) {
            return "Piper";
        }
        Publisher p = publisherService.getPubLisherByPublishTmail(header.getReceiver(), null);
        if (p != null) {
            if (header.getSender().equals(p.getUserId())) {
                return "*" + p.getName();
            }
            return p.getName();
        }

        return header.getReceiver().split("@")[1].split("\\.")[0];

    }

}
