package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发布者（出版社）
 * Created by 115477 on 2018/11/27.
 */
@Data
public class Consumer extends BaseEntity {
	//发布者Id
	private int id;
	//用户Id（秘邮）
	@NotNull
	private String userId;
	//公钥
	private String pubkey;
	//当前菜单版本号
	private String curversion;
	//当前P菜单版本号
	private String pversion;

	//当前用户角色
	private String role;

}
