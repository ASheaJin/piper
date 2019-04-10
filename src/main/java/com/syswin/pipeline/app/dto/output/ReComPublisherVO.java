package com.syswin.pipeline.app.dto.output;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/4/1 19:55
 */
@Data
public class ReComPublisherVO {

	//出版社Id
	private String publisherId;
	//出版社邮箱
	private String ptemail;
	//出版社名称
	private String name;
	//出版社类型
	private String piperType;
	//是否订阅
	private String hasSub = "0";
}
