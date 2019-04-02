package com.syswin.pipeline.db.model;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/4/1 10:02
 */
@Data
public class ReCommendContent  extends BaseEntity {

	private  String id;
	private  String contentId;
	private  String userId;

}
