package com.syswin.pipeline.manage.dto.output;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/4/1 19:55
 */
@Data
public class PublisherManageVO {

	//出版社Id
	private String publisherId;
	//创建者秘邮号
	private String userId;
	//出版社邮箱
	private String ptemail;
	//出版社名称
	private String name;
	//出版社类型
	private String piperType;
	// 已推荐社区Id
	private String reid = "-1";
	//是否推荐
	private String hasRecommend = "0";
	//创建时间
	private String creatTime;
}
