package com.syswin.pipeline.psservice;

import com.syswin.pipeline.enums.EnumEncryptionMethod;
import com.syswin.pipeline.psservice.bean.PSExtraData;
import com.syswin.pipeline.psservice.bean.SessionExtData;
import com.syswin.pipeline.utils.PSUtil;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.common.ChatMsgDetail;
import com.syswin.ps.sdk.common.CommonMsg;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.msgType.BaseMsgType;
import com.syswin.ps.sdk.sender.AbstractMsgSender;
import com.syswin.ps.sdk.service.CipherService;
import com.syswin.ps.sdk.service.CrossDomainService;
import com.syswin.ps.sdk.service.SingleChatService;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.ps.sdk.utils.FastJsonUtil;
import com.syswin.temail.ps.client.Header;
import com.syswin.temail.ps.client.Message;
import com.syswin.temail.ps.common.entity.SignatureAlgorithm;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于测试发消息
 *
 * @author:lhz
 * @date:2019/5/25 13:38
 */
@Service
public class MessegerSenderService extends AbstractMsgSender {
    private static final Logger logger = LoggerFactory.getLogger(MessegerSenderService.class);
    @Autowired
    private SingleChatService singleChatService;

    @Autowired
    private PSUtil psUtil;

    @Autowired
    private CrossDomainService domainService;

    @Autowired
    private NickNameService nickNameService;

    @Autowired
    private CipherService cipherService;


    @Override
    public void sendMsg(String from, String to, BaseMsgType baseMsgType) {
        String msgId = UUID.randomUUID().toString();
        Header header = psClientService.header(from, to, msgId);
        CommonMsg commonMsg = new CommonMsg(header, baseMsgType.getShowType(), baseMsgType.getShowContent());
        singleChatService.sendMsg(commonMsg);
    }

    public void sendImage(String from, String to, String url, String fileName) throws IOException {
        super.sendImage(from, to, getImageFromNetByUrl(url), fileName);
    }

    /**
     * 读取 本地文件，转为字节数组
     *
     * @param strUrl 本地文件路径
     * @return
     * @throws IOException
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    } //由于读取需要一定时间，所以不能单纯往字节数组里读，所以需要判断是否读完

    public static byte[] readInputStream(InputStream inStream) throws Exception { //存放读取的所有的字节数组
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public void sendComplexMsg(String from, String to, String title, String text, String imageUrl, String infoTile, String infoUrl) {
//		MsgHeader msgHeader= PsClientKeeper.msgHeader();
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isEmpty(title)) {
            throw new RuntimeException("title不能为空");
        }
        map.put("title", title);
        if (!StringUtil.isEmpty(imageUrl)) {
            map.put("imageUrl", imageUrl);
        }
        if (!StringUtil.isEmpty(text)) {
            map.put("text", text);
        }

        List<ActionItem> infoList = Stream.of(new ActionItem(infoTile, infoUrl)
        ).collect(Collectors.toList());

        TextShow show = new TextShow(1, map, infoList);
        logger.info("fromTemail {}, orderUserId {}, show {}", from, to, show);
        PsClientKeeper.newInstance().sendMsg(from, to, show);
    }

    @Async("msgThreadPool")
    public Message sendAsyncContent(String from, String to, String newMsgId, int bodyType, Object content) throws Exception {
        return this.sendContent(from, to, newMsgId, bodyType, content);
    }

    public Message sendSynchronizationTxt(String from, String to, Object content) {
        String msgId = UUID.randomUUID().toString();
        try {
            return this.sendContent(from, to, msgId
                    , 1, "{\"text\":\"" + content + "\"}");
        } catch (Exception ex) {
            logger.error(from + " send to " + to + "error----   msgId：" + msgId, ex);
        }
        return null;
    }

    public Message sendSynchronizationContent(String from, String to, String newMsgId, int bodyType, Object content) throws Exception {

        return this.sendContent(from, to, newMsgId, bodyType, content);
    }


    //名片消息
    @Value("${app.pipeline.imgUrl}")
    private String url;
    @Value("${p.pipeline.imgUrl}")
    private String purl;

    public Message sendCard(String from, String to, String name) {

        try {
            return this.sendCard(from, to, name, null);
        } catch (Exception ex) {
            logger.error(from + " send to " + to + "error----   card：", ex);
        }
        return null;
    }

    public Message sendCard(String from, String to, String name, String imgUrl) throws Exception {

        if (from.contains("p.")) {
            imgUrl = imgUrl == null ? purl : imgUrl;
        }
        if (from.contains("a.piper")) {
            imgUrl = imgUrl == null ? url : imgUrl;
        }

        return this.sendContent(from, to, UUID.randomUUID().toString(), 4, FastJsonUtil.toJson(cardContent(from, name, imgUrl)));
    }

    public static final String VCARD_TEMPLATE = "BEGIN:VCARD\r\nPHOTO:%s\r\nVERSION:3.0\r\nN:%s\r\nEMAIL:%s\r\nEND:VCARD";

    private Card cardContent(String temail, String name, String imgUrl) {
        String vcard = String.format(VCARD_TEMPLATE, imgUrl, name, temail);
        Card card = new Card();
        card.setNick(name);
        card.setFeedId(vcard);
        card.setUrl(imgUrl);
        card.setDesc(temail);
        return card;
    }

    @Data
    class Card {
        private String nick;
        private String url;
        private String feedId;
        private String desc;
    }


    /**
     * @param from
     * @param to
     * @param msgId
     * @param bodyType
     * @param content
     * @return
     * @throws Exception
     */
    private Message sendContent(String from, String to, String msgId, int bodyType, Object content) throws Exception {
        logger.info("sendContent from：{}，to：{}，msgId：{}", from, to, msgId);

        Header header = null;
        header = initHeader(from, to, msgId);
        CommonMsg commonMsg = new CommonMsg(header, bodyType, content);
//        singleChatService.sendMsg(header, getMsgContent(commonMsg));
        ChatMsgDetail callBackMsg = getMsgContent(commonMsg);
        callBackMsg.setNickName(this.nickNameService.getNickName(header.getSender()));

        header.setTimestamp(System.currentTimeMillis());
        String payload = FastJsonUtil.toJson(callBackMsg);
        if (header.getDataEncryptionMethod() == EnumEncryptionMethod.RECC.getCode()) {
            payload = this.cipherService.encrypt(psUtil.publickey(to), payload);
        }
        byte[] data = payload.getBytes(Charset.forName("UTF-8"));

        Message toMeMsg = new Message(header, data);
        Message result = this.psClientService.sendMessage(toMeMsg);
        return result;
    }

    public ChatMsgDetail getMsgContent(CommonMsg msgData) {
        String from = msgData.getHeader().getSender();
        String to = msgData.getHeader().getReceiver();
        Object content = msgData.getContent();
        Integer bodyType = msgData.getBodyType();


        ChatMsgDetail callBackMsg = new ChatMsgDetail();

        callBackMsg.setAtTemails("");
        callBackMsg.setBody_type(bodyType);
        callBackMsg.setChat_type(0);
        callBackMsg.setContent(String.valueOf(content));
        callBackMsg.setData(content);
        callBackMsg.setFrom(from);
        callBackMsg.setMsg_id(UUID.randomUUID().toString());
        callBackMsg.setSession_id(from + ":" + to);
        callBackMsg.setStatus(3);
        callBackMsg.setTime_stamp(System.currentTimeMillis());
        callBackMsg.setTo(to);

        return callBackMsg;
    }


    private Header initHeader(String from, String to, String newMsgId) {
        Header header = new Header();
        header.setCommandSpace((short) 1);
        header.setCommand((short) 1);
        header.setSignatureAlgorithm(SignatureAlgorithm.ECC512_CODE);
        header.setDataEncryptionMethod(EnumEncryptionMethod.RECC.getCode());
        header.setTimestamp(System.currentTimeMillis());
        header.setPacketId(UUID.randomUUID().toString());
        header.setSender(from);
        header.setSenderPK(psUtil.publickey(from));
        header.setReceiver(to);
        header.setReceiverPK(psUtil.publickey(to));
        PSExtraData extraData = new PSExtraData();
        extraData.setMsgId(newMsgId);
        extraData.setFrom(from);
        extraData.setTo(to);
        extraData.setStoreType(1);
        extraData.setType("0");
        SessionExtData sessionExtData = new SessionExtData();
        sessionExtData.setName(this.nickNameService.getNickName(from));


        String imgUrl = "";
        if (from.contains("p.")) {
            imgUrl = purl;
        }
        if (from.contains("a.piper")) {
            imgUrl = url;
        }
        sessionExtData.setAvatarHost(imgUrl);
        sessionExtData.setContactType(5);
        extraData.setSessionExtData(FastJsonUtil.toJson(sessionExtData));

        header.setExtraData(FastJsonUtil.toJson(extraData));
        header.setTargetAddress(this.domainService.gateWay(to));
        header.setTopic("");
        header.setAt("");
        return header;
    }

}
