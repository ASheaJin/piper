package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * token信息
 */
@Data
public class Token  extends BaseEntity {

	private Integer tokenId;
	@NotNull
	private String userId;

	private String token;

	private Integer updateTime;

	private String userInfo;

}