package com.syswin.pipeline.psservice.bean;

import com.syswin.ps.sdk.common.ActionItem;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:lhz
 * @date:2019/5/27 9:57
 */
@Data
public class SaveText implements Serializable {
	private Integer showType;
	private Map<String, Object> showContent;
	private List<ActionItem> actions = new ArrayList();
}
