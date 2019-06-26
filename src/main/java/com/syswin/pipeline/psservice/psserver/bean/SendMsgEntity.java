package com.syswin.pipeline.psservice.psserver.bean;

import lombok.Data;

/**
 * 发送消息实体
 * @author:lhz
 * @date:2019/1/7 10:24
 */
@Data
public class SendMsgEntity<M, D> {

	//消息体内容
	private M msg;
	//发送者
	private String sender;
	//发送者公钥
	private String senderPK;
	//接受者
	private String receiver;
	//接受者公钥
	private String receiverPK;
	//额外消息体
	private D extrData;

}
