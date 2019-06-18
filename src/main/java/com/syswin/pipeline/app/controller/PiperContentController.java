package com.syswin.pipeline.app.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.ContentIdInput;
import com.syswin.pipeline.app.dto.ContentListParam;
import com.syswin.pipeline.service.content.ContentHandleJobManager;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.sub.api.ContentOutService;
import com.syswin.sub.api.ContentService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Content;
import com.syswin.sub.api.db.model.ContentOut;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发布内容、查看、评价、搜索
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@RestController
@RequestMapping("/content")
@Api(value = "content", tags = "content")
public class PiperContentController {

	@Autowired
	private PublisherService publisherService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentOutService contentOutService;
	@Autowired
	private ContentHandleJobManager contentHandleJobManager;

	@PostMapping("/list")
	@ApiOperation(
					value = "出版社的历史内容列表"
	)
	public ResponseEntity<PageInfo<ContentEntity>> list(@RequestBody ContentListParam input) {
		int pageNo = !StringUtils.isEmpty(input.getPageNo()) ? Integer.parseInt(input.getPageNo()) : 1;
		int pageSize = !StringUtils.isEmpty(input.getPageSize()) ? Integer.parseInt(input.getPageSize()) : 30;

		PageInfo contentOutPageInfo = contentOutService.listByPublisherId(input.getPublisherId(), pageNo, pageSize);
		List<ContentOut> contents = contentOutPageInfo.getList();
		List<ContentEntity> contentEntities = contents.stream().map((out) -> {
			ContentEntity entity = JacksonJsonUtil.fromJson(out.getListdesc(), ContentEntity.class);
			entity.setPublisherId(out.getPublisherId());
			entity.setPublishTime(out.getCreateTime());
			return entity;
		}).collect(Collectors.toList());
		contentOutPageInfo.setList(contentEntities);

		return new ResponseEntity(contentOutPageInfo);
	}

	@GetMapping("/detail")
	@ApiOperation(
					value = "内容详情"
	)
	public ResponseEntity<ContentEntity> detail(@ModelAttribute ContentIdInput input) {
		String contentId = input.getContentId();
		ContentOut contentOut = contentOutService.getContentOutById(contentId);
		ContentEntity contentEntity = JacksonJsonUtil.fromJson(contentOut.getAllcontent(), ContentEntity.class);
		contentEntity.setPublisherId(contentOut.getPublisherId());
		contentEntity.setPublishTime(contentOut.getCreateTime());

		//出版社名字
		Publisher publisher = publisherService.getPubLisherById(contentOut.getPublisherId());
		if (publisher != null) {
			contentEntity.setPublisherName(publisher.getName());
			contentEntity.setPtemail(publisher.getPtemail());
			if (!publisher.getPtype().equals(PublisherTypeEnums.organize)) {
				contentEntity.setShow(1);
			}
		}


		return new ResponseEntity(contentEntity);
	}


	/**
	 * 按ContentId重新解析内容
	 *
	 * @param input
	 * @return
	 */
	@PostMapping("/c")
	@ApiOperation(hidden = true, value = "按ContentId重新解析内容")
	public ResponseEntity c(@RequestBody ContentIdInput input) {
		List<String> l = new ArrayList();
		l.add(input.getContentId());
		List<Content> contentList = contentService.getContentsByCids(l);
		addContentsJob(contentList);

		return new ResponseEntity();
	}

	private void addContentsJob(List<Content> contentList) {
		for (Content content : contentList) {
			contentHandleJobManager.addJob(content.getPublisherId(), content.getContentId(), content.getBodyType(),
							content.getContent(),
							content.getCreateTime());
		}
	}


	/**
	 * 按publisherId重新解析内容
	 *
	 * @param input
	 * @return
	 */
	@PostMapping("/p")
	@ApiOperation(hidden = true, value = "按publisherId重新解析内容")
	public ResponseEntity p(@RequestBody ContentListParam input) {

		List<Content> contentList = contentService.getMyContentsbyPid(input.getPublisherId(), 1, 10000);
		addContentsJob(contentList);

		return new ResponseEntity();
	}


	/**
	 * 重新解析所有内容
	 *
	 * @return
	 */
	@PostMapping("/pa")
	@ApiOperation(hidden = true, value = "重新解析所有内容")
	public ResponseEntity pa() {
		List<Publisher> publishers = publisherService.list(1, 10000, null, null, null).getList();
		for (Publisher publisher : publishers) {
			List<Content> contentList = contentService.getMyContentsbyPid(publisher.getPublisherId(), 1, 10000);
			addContentsJob(contentList);
		}

		return new ResponseEntity();
	}
}
