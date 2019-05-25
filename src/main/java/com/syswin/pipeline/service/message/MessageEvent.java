package com.syswin.pipeline.service.message;

import com.syswin.pipeline.psservice.olderps.ChatMsg;
import com.syswin.temail.ps.client.Header;
import lombok.Data;

/**
 * Created by 115477 on 2019/1/14.
 */
@Data
public class MessageEvent {
    private ChatMsg chatMsg;
    private Header originHeader;

}
