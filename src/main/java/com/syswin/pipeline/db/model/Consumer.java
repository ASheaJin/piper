package com.syswin.pipeline.db.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发布者（出版社）
 * Created by 115477 on 2018/11/27.
 */
@Data
public class Consumer extends BaseEntity {

	private Integer id;

	private String userId;

	private String ptemail;

	private String curversion;

	private String role;

}
