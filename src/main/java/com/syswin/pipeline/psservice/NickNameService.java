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

    @Value("${app.ps-app-sdk.user-id}")
    private String piper;

    @Autowired
    private PublisherService publisherService;


    @Override
    public String getNickName(String userId) {
        MsgHeader header = PsClientKeeper.msgHeader();

        if (userId.equals(piper)) {
            return "Piper";
        }
        Publisher p = publisherService.getPubLisherByPublishTmail(userId, null);
        if (p != null) {

            if (header != null && header.getSender().equals(p.getUserId())) {
                return "*" + p.getName();
            }
            return p.getName();
        }

        return userId.split("@")[0].split("\\.")[1];

    }

}
