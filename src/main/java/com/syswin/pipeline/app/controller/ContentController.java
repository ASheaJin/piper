package com.syswin.pipeline.app.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.ContentIdInput;
import com.syswin.pipeline.app.dto.ContentOutput;
import com.syswin.pipeline.app.dto.SubUserListParam;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public ResponseEntity<PageInfo<ContentOutput>> list(@RequestBody SubUserListParam subUserList) {

		return new ResponseEntity();

	}
	@GetMapping("/detail")
	public ResponseEntity<ContentOutput> detail(@ModelAttribute ContentIdInput input) {
		return new ResponseEntity();
	}

	private Map<String, ContentOutput> m = new HashMap(){
		{
			ContentOutput text = new ContentOutput("1");
			text.setBodyType(1);
			text.setText("基于springfox使用swagger非常简单，只需要maven依赖以及少量config配置就可以实现，上面的demo中都有体现，或者直接访问springfox的github上面的demospringfox/springfox-demos 。\n" +
					"springfox更详细配置请参考官方文档Springfox Reference Documentation。\n" +
					"swagger annotation具体使用 Swagger-Core Annotations，这里面有详细的annotation描述。");
			put(text.getContentId(), text);

			ContentOutput voice = new ContentOutput("2");
			put(voice.getContentId(), voice);
		}
	};

}
