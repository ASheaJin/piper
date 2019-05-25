package com.syswin.pipeline.psservice.olderps;


import com.syswin.temail.ps.client.Message;

/**
 * Created by 115477 on 2018/12/17.
 */
public interface PSClientService {

    /**
     * 注册temail会话的处理逻辑
     * @param temail
     */
//    void loginTemail(String temail);
    public String getTemailTestPublicKey(String temail);
    /**
     * 发送消息
     * @param msg
     * @return 是否发送成功
     */
    public boolean sendChatMessage(ChatMsg msg, String toTemail, String toTemailPK);

    public boolean sendCardMessage(ChatMsg msg, String from, String fromPK, String to, String toTemailPK);

    /**
     * 获取公钥
     * @param temail
     * @return
     */
    public String getTemailPublicKey(String temail);
    /**
     *
     * @param temail
     * @return
     */
    public String registerTemail(String temail);

    /**
     *
     * @param temail
     * @return
     */
    public String registerPub(String temail);

    /**
     * 发送文本的消息
     *
     * @param content   内容
     * @param to        发给谁
     * @param deloytime 延时时间
     */
     void sendTextmessage(String content, String to, int deloytime);

    /**
     *
     * @param msg
     * @param toTemail
     * @param toTemailPK
     * @param sendTemail
     * @param sendPK
     * @return
     */
    Boolean sendChatMessage(ChatMsg msg, String toTemail, String toTemailPK, String sendTemail, String sendPK);

    /**
     * 发送cdtp消息
     * @param comSpaceId
     * @param comId
     * @param payload
     * @return
     */
    Message sendCdtpRequestFromPiper(short comSpaceId, short comId, Object payload);
    /**
     * 发送cdtp消息
     * @param comSpaceId
     * @param comId
     * @param payload
     * @param toTemail
     * @param toTemailPK
     * @param sendTemail
     * @param sendPK
     * @return
     */
    Message sendCdtpRequest(short comSpaceId, short comId, Object payload, String toTemail, String toTemailPK, String sendTemail, String sendPK);
}
