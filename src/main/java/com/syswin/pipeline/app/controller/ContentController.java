package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.SubUserListParam;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发布内容、查看、评价、搜索
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@RestController
@RequestMapping("/content")
@Api(value = "content", tags = "content")
public class ContentController {


	@PostMapping("/list")
	@ApiOperation(
					value = "获取出版社列表"
	)
	public ResponseEntity list(@RequestBody SubUserListParam subUserList) {

		return new ResponseEntity();

	}
}
