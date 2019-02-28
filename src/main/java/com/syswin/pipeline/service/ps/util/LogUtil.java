package com.syswin.pipeline.service.ps.util;

import com.syswin.temail.ps.client.Header;
import com.syswin.temail.ps.client.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

/**
 * Created by 115477 on 2018/12/18.
 */
public class LogUtil {

    private final static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void logCdtpMsgIn(Message msg, Consumer<String> func) {
        logCdtpMsg(msg, "====> ", func);
    }
    public static void logCdtpMsgOut(Message msg, Consumer<String> func) {
        logCdtpMsg(msg, "<==== ", func);
    }

    public static void logCdtpMsg(Message msg, String prefix,  Consumer<String> func) {
        if (msg == null) {
            func.accept("null");
            return;
        }
        Header header = msg.getHeader();
        byte[] payload = msg.getPayload();

        StringBuffer buf = new StringBuffer();
        buf.append(prefix);
        buf.append("header:{").append(FastJsonUtil.toJson(header)).append("}");
        try {
            buf.append(" payload:{").append(new String(payload, "UTF-8")).append("}");
        } catch (UnsupportedEncodingException e) {
            ;
        }

        func.accept(buf.toString());
    }
}
