package com.syswin.pipeline.psservice.psserver.impl;

import com.syswin.pipeline.psservice.olderps.impl.EnumEncryptionMethod;
import com.syswin.pipeline.psservice.psserver.SendMsgService;
import com.syswin.pipeline.psservice.psserver.bean.SendMsgEntity;
import com.syswin.pipeline.utils.FastJsonUtil;
import com.syswin.temail.kms.vault.VaultKeeper;
import com.syswin.temail.ps.client.Header;
import com.syswin.temail.ps.client.Message;
import com.syswin.temail.ps.client.PsClient;
import com.syswin.temail.ps.client.PsClientBuilder;
import com.syswin.temail.ps.common.entity.SignatureAlgorithm;
import com.syswin.temail.ps.common.packet.KeyAwarePacketSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 封装发送消息
 * Created by lhz on 2019/1/4.
 */

@Service
public class SendMsgServiceImpl<M, D> implements SendMsgService<M, D> {
    private static final Logger logger = LoggerFactory.getLogger(SendMsgServiceImpl.class);

    private PsClient psClient;
    @Autowired
    CDTPProperties cdtpProperties;

    /**
     * CDTP客户端
     */
	@PostConstruct
    private void init() {
        InetAddress address = null;
        try {
            address = InetAddress.getByName("application.t.email");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("获取失败");
        }
        System.out.println(address.getHostAddress().toString());

        this.psClient = new PsClientBuilder(cdtpProperties.getDeviceId())
                .defaultHost("application.t.email")
                .defaultPort(8100)
                .signer(new KeyAwarePacketSigner(
                        VaultKeeper.keyAwareVault(cdtpProperties.getKmsBaseUrl(), cdtpProperties.getTenantId())
                )).build();

    }

    //初始化header
    private Header initHeader(String sender, String senderPK, String receiver, String receiverPK) {
        logger.info("send initHeader");
        Header header = new Header();

        header.setSignatureAlgorithm(SignatureAlgorithm.ECC512_CODE);
        header.setDataEncryptionMethod(EnumEncryptionMethod.NO.getCode());
        header.setTimestamp(System.currentTimeMillis());
        header.setPacketId(UUID.randomUUID().toString());
        header.setSender(sender);
        header.setSenderPK(senderPK);
        header.setReceiver(receiver);
        header.setReceiverPK(receiverPK);
        return header;
    }

    /**
     * 模拟客户端向服务器发
     *
     * @param sendMsgEntity
     * @param command
     * @return
     */
    @Deprecated
    public Message sendChatTestMessage(SendMsgEntity sendMsgEntity, short command) {
//		Header chatMsgHeader = initHeader( (short)1, "a.piper@t.email", "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc");
        Header chatMsgHeader = initHeader(sendMsgEntity.getSender(), sendMsgEntity.getSenderPK(), sendMsgEntity.getReceiver(), sendMsgEntity.getReceiverPK());
        chatMsgHeader.setCommandSpace(cdtpProperties.getCmd());
        chatMsgHeader.setCommand(command);
        if (null != sendMsgEntity.getExtrData()) {
            chatMsgHeader.setExtraData(FastJsonUtil.toJson(sendMsgEntity.getExtrData()));
        }
        Message chatMsg = initChatMessage(chatMsgHeader, sendMsgEntity.getMsg());
        Message resultMsg = psClient.sendMessage(chatMsg);
        return resultMsg;
    }


    /**
     * 服务器向客户端发采用单聊发
     *
     * @param sendMsgEntity 一般是文章
     * @return 有bug暂时作废
     */
    @Deprecated
    public boolean sendFileMessage(SendMsgEntity sendMsgEntity) {
//		Header chatMsgHeader = initHeader( (short)1, "a.piper@t.email", "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc");
        Header chatMsgHeader = initHeader(sendMsgEntity.getSender(), sendMsgEntity.getSenderPK(), sendMsgEntity.getReceiver(), sendMsgEntity.getReceiverPK());
        chatMsgHeader.setCommandSpace((short) 1);
        chatMsgHeader.setCommand((short) 1);
        if (null != sendMsgEntity.getExtrData()) {
            chatMsgHeader.setExtraData(FastJsonUtil.toJson(sendMsgEntity.getExtrData()));
        }
        Message chatMsg = initChatMessage(chatMsgHeader, sendMsgEntity.getMsg());
        Message resultMsg = psClient.sendMessage(chatMsg);
        return true;
    }

//	public boolean sendChatMessage(ChatMsg msg) {
//		Header chatMsgHeader = initHeader((short) 1, "a.piper@t.email", "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBtBIPnoFOpyVCj8LiW2xgdOKqbizLUhZo5AWppUW3SuIHjf32aHgEI_V47ytdWwY7DykZjnrCoL_OuqaQkHawFVkAaBVdu5w8K00rh7rFK80BBL8o0DROHE78NNhX1d3jSITHRjY0loNPG54P3z40VfU-j9nmLlU2zgfVXkCHk7KCRAc");
////		ExtraData extraData = new ExtraData(appTemail, toTemail,
////						UUID.randomUUID().toString(), 1);
////		chatMsgHeader.setExtraData(FastJsonUtil.toJson(extraData));
//
//		Message chatMsg = initChatMessage(chatMsgHeader, msg);
//
//		Message resultMsg = psClient.sendMessage(chatMsg);
//		logger.info("send initHeader");
//		return true;
//	}

    private <T> Message initChatMessage(Header header, T payload) {

        Message message = new Message();
        message.setHeader(header);
        byte[] data = FastJsonUtil.toJson(payload).getBytes(Charset.forName("UTF-8"));
        //不加密
        message.setPayload(data);

        return message;
    }
}
