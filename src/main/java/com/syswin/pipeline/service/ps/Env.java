package com.syswin.pipeline.service.ps;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/4/12 9:41
 */
@Data
public class Env {
	//	{"language":"zh","platform":"android","moduleVersion":"1.0.0","os_version":25,"version":"1.2.0P","build":"1904030921"}
	private String language;
	private String platform;
	private String moduleVersion;
	private String os_version;
	private String version;
	private String build;

}
