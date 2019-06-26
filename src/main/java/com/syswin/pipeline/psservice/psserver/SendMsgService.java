package com.syswin.pipeline.psservice.psserver;

import com.syswin.pipeline.psservice.psserver.bean.SendMsgEntity;

/**
 * 发送消息统一接口
 * Created by lhz on 2018/11/27.
 */
public interface SendMsgService<M, D> {


	/**
	 * 目前先以单聊，向PsClient发送消息（文件）
	 * @param sendMsgEntity 发送消息 一般是文件
	 * @return
	 * 有bug
	 */
	@Deprecated
	boolean sendFileMessage(SendMsgEntity sendMsgEntity);

	/**
	 * 提供给服务器测试使用
	 * @param sendMsgEntity 消息内容
	 * @param command 指令
	 * @return
	 */
	@Deprecated
	boolean sendChatTestMessage(SendMsgEntity sendMsgEntity, short command);
}
