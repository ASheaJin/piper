package com.syswin.pipeline.db.model;

import com.syswin.pipeline.utils.DateUtil;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author:lhz
 * @date:2018/11/27 17:58
 */
@Data
public class BaseEntity implements Serializable {

	private int createTime = DateUtil.getDate();
}
