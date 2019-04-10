package com.syswin.pipeline.manage.dto.output;

import lombok.Data;

/**
 * @author:lhz
 * @date:2019/4/1 17:27
 */
@Data
public class AdminManageVO {
	//管理员Id
	private int id;
	//用户秘邮号
	private String userId;
	//创建者秘邮号
	private String creater;
	//状态（0,无效 1 有效）
	private String status;
	//状态（0,无效 1 有效）
	private String ptype;;
	//创建时间
	private String creatTime;
}
