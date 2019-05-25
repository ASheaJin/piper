package com.syswin.pipeline.psservice.olderps;

import com.syswin.pipeline.utils.FastJsonUtil;
import lombok.Data;

import java.util.UUID;

/**
 * Created by 115477 on 2018/12/17.
 */
@Data
public class ChatMsg {
	private String atTemails;
	private Integer body_type;
	private Integer chat_type;
	private String content;
	private String from;
	private String msg_id;
	private String session_id;
	private Integer status;
	private Long time_stamp;
	private String to;

	public ChatMsg() {
	}

	/**
	 * 发送文本类型提示
	 *
	 * @param content
	 * @param from
	 * @param to
	 */
	public ChatMsg(String content,int body_type, String from, String to) {
		this.from = from;
		this.to = to;
		this.msg_id = UUID.randomUUID().toString();
		this.session_id = from + ":" + to;
		this.status = 3;
		this.time_stamp = System.currentTimeMillis();
		this.chat_type = 0;
		this.content = content;
		this.body_type = body_type;
	}

	/**
	 * 发送文本类型提示
	 *
	 * @param content
	 * @param from
	 * @param to
	 */
	public ChatMsg(String content,int body_type, String from, String to,int delay) {
		this.from = from;
		this.to = to;
		this.msg_id = UUID.randomUUID().toString();
		this.session_id = from + ":" + to;
		this.status = 3;
		this.time_stamp = System.currentTimeMillis()+ delay;
		this.chat_type = 0;
		this.content = content;
		this.body_type = body_type;
	}

	public ChatMsg(String from, String to, String msgId, Object common_replay_message) {
		this.from = from;
		this.to = to;
		this.msg_id = msgId;
		this.session_id = from + ":" + to;
		this.content = FastJsonUtil.toJson(common_replay_message);
		this.chat_type = 0;
		this.status = 3;
		this.time_stamp = System.currentTimeMillis();
//		this.body_type = body_type;
	}

	public String getAtTemails() {
		return atTemails;
	}

	public void setAtTemails(String atTemails) {
		this.atTemails = atTemails;
	}

	public Integer getBody_type() {
		return body_type;
	}

	public void setBody_type(Integer body_type) {
		this.body_type = body_type;
	}

	public Integer getChat_type() {
		return chat_type;
	}

	public void setChat_type(Integer chat_type) {
		this.chat_type = chat_type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(Long time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}
