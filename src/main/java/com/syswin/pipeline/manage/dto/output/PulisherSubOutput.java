package com.syswin.pipeline.manage.dto.output;

import lombok.Data;

/**
 * 在Publisher中展示
 *
 * @author:lhz
 * @date:2019/4/9 10:47
 */
@Data
public class PulisherSubOutput {

	// contentId
	private String contentId;
	// PublisherId
	private String publisherId;
	// 出版社名称
	private String publisherName;
	// 是否推荐
	private String hasSub = "0";
}
