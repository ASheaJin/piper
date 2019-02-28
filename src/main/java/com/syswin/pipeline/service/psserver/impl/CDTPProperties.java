package com.syswin.pipeline.service.psserver.impl;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author:lhz
 * @date:2019/1/2 9:35
 */
public class CDTPProperties {

	@Value("${app.pssever.idleTimeSeconds}")
	private int idleTimeSeconds =1000;

	@Value("${app.pssever.cdtpport}")
	private int port = 8686;

	@Value("${app.pipeline.tenantId}")
	private String tenantId = "syswin";

	@Value("${app.pipeline.deviceId}")
	private String deviceId="aaaaaaaaa";

	@Value("${app.pipeline.userId}")
	private String userId="a.piper@t.email";

	@Value("${app.pssever.cdtphost}")
	private String cdtp_host="172.28.43.107";

	@Value("${url.temail-kms-cmi.service}")
	private String kmsBaseUrl ="http://172.28.43.107:8089/kms-server";

	//0A01  A001超过了short最大值 2561
	@Value("${app.pssever.cdtpcms}")
	private short cmd =2561;

	public int getIdleTimeSeconds() {
		return idleTimeSeconds;
	}

	public void setIdleTimeSeconds(int idleTimeSeconds) {
		this.idleTimeSeconds = idleTimeSeconds;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getKmsBaseUrl() {
		return kmsBaseUrl;
	}

	public void setKmsBaseUrl(String kmsBaseUrl) {
		this.kmsBaseUrl = kmsBaseUrl;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCdtp_host() {
		return cdtp_host;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCdtp_host(String cdtp_host) {
		this.cdtp_host = cdtp_host;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}
}
