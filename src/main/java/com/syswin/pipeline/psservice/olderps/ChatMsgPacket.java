package com.syswin.pipeline.psservice.olderps;

import lombok.Data;

import java.util.List;

/**
 * Created by 115477 on 2018/12/18.
 */
@Data
public class ChatMsgPacket {

    private String sender;
    private String senderPK;
    private String receiver;
    private String receiverPK;

    private List<ChatMsg> chatMsgs;
}
