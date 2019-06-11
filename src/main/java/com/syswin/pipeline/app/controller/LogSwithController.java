package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.utils.SwithUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志管理
 */
@CrossOrigin
@RestController
@RequestMapping("/log")
public class LogSwithController {

	@RequestMapping("/open")
	public ResponseEntity open() {
		SwithUtil.ISLOG = true;
		return new ResponseEntity("打开成功", "当前日志开关状态： " + SwithUtil.ISLOG, null);
	}

	@RequestMapping("/close")
	public ResponseEntity close() {
		SwithUtil.ISLOG = false;
		return new ResponseEntity("关闭成功", "当前日志开关状态： " + SwithUtil.ISLOG, null);
	}

}
