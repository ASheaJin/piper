package com.syswin.pipeline.manage.dto.output;

import lombok.Data;

/**
 * 在Publisher中展示
 *
 * @author:lhz
 * @date:2019/4/9 10:47
 */
@Data
public class ContentOutput {

	// contentId
	private String contentId;
	// PublisherId
	private String publisherId;
	// 出版社名称
	private String publisherName;
	// 是否推荐
	private String hasRecommend = "0";
	// 已推荐内容Id
	private String reid = "-1";
	// 时间
	private String createTime;
	// list
	private String listdesc;
	// decUrl
	private String decUrl;
}
