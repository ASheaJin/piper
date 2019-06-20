package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.app.dto.SendComplexInfoParam;
import com.syswin.pipeline.app.dto.SendParam;
import com.syswin.pipeline.enums.BodyTypeEnums;
import com.syswin.pipeline.enums.ShowTypeEnums;
import com.syswin.pipeline.psservice.bean.SaveText;
import com.syswin.pipeline.psservice.bussiness.PublisherSecService;
import com.syswin.pipeline.service.PiperSpiderTokenService;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.service.content.entity.MediaContentEntity;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Publisher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by 115477 on 2018/12/18.
 */
@CrossOrigin
@RestController
@RequestMapping("/send")
@Api(value = "send", tags = "send")
public class PiperSendController {

	@Autowired
	PublisherSecService publisherSecService;
	@Autowired
	PiperSpiderTokenService spiderTokenService;
	@Autowired
	private PublisherService publisherService;

	@PostMapping("/send")
	@ApiOperation(
					value = "发送消息"
	)
	public ResponseEntity send(@RequestBody SendParam msg) {
		/**
		 * 爬虫发文本
		 */
		if (spiderTokenService.getSpiderToken(msg.getToken(), msg.getPiperTemail()) == null) {
			throw new BusinessException("ex.tokenInvalid");
		}
		Publisher publisher = publisherService.getPubLisherByPublishTmail(msg.getPiperTemail(), null);
		if (publisher == null) {
			throw new BusinessException("ex.publisher.null");
		}
		String txt = "{\"text\":\"" + msg.getContent() + "\"}";
		Integer num = publisherSecService.dealpusharticle(publisher, BodyTypeEnums.TEXT.getType(), txt, publisher.getPtype());
		return new ResponseEntity(num);
	}


	@PostMapping("/sendComplexInfo")
	@ApiOperation(
					value = "爬虫发复杂的消息体"
	)
	public ResponseEntity sendComplexInfo(@RequestBody SendComplexInfoParam msg) {
		/**
		 * 爬虫发文本
		 */
		if (spiderTokenService.getSpiderToken(msg.getToken(), msg.getPiperTemail()) == null) {
			throw new BusinessException("ex.tokenInvalid");
		}
		Publisher publisher = publisherService.getPubLisherByPublishTmail(msg.getPiperTemail(), null);
		if (publisher == null) {
			throw new BusinessException("ex.publisher.null");
		}

		Map<String, Object> map = new HashMap<>();
		if (!StringUtil.isEmpty(msg.getTitle())) {
			map.put("title", msg.getTitle());
		}
		if (!StringUtil.isEmpty(msg.getImgUrl())) {
			map.put("imageUrl", msg.getImgUrl());
		}
		map.put("text", msg.getImgTxt());

		List<ActionItem> infoList = Stream.of(new ActionItem(msg.getTxt(), msg.getUrl())
		).collect(Collectors.toList());

		TextShow show = new TextShow(ShowTypeEnums.COMPLEX.getType(), map, infoList);
		//构造历史消息的输出

		Integer num = publisherSecService.dealpusharticle(publisher, BodyTypeEnums.COMPLEX.getType(), show, getContentEntity(msg, publisher), publisher.getPtype());
		return new ResponseEntity(num);
	}

	private ContentEntity getContentEntity(SendComplexInfoParam msg, Publisher publisher) {

		ContentEntity contentEntity = new ContentEntity();
		contentEntity.setBodyType(BodyTypeEnums.COMPOSE.getType());
		contentEntity.setTitle(msg.getTitle());
		contentEntity.setPublisherName(publisher.getName());
		contentEntity.setPublisherId(publisher.getPublisherId());
		List<MediaContentEntity> list = new ArrayList<>();

		if (!StringUtil.isEmpty(msg.getImgUrl())) {

			MediaContentEntity mPngInfo = new MediaContentEntity();
			mPngInfo.setText(msg.getImgTxt());
			mPngInfo.setBodyType(BodyTypeEnums.TEXT.getType());
			list.add(mPngInfo);

			MediaContentEntity mPng = new MediaContentEntity();
			mPng.setUrl(msg.getImgUrl());
			mPng.setBodyType(BodyTypeEnums.PIC.getType());
			list.add(mPng);
		}
		MediaContentEntity m = new MediaContentEntity();
		m.setText(msg.getTxt());
		m.setBodyType(BodyTypeEnums.TEXT.getType());
		list.add(m);
		MediaContentEntity mUrl = new MediaContentEntity();
		mUrl.setText(msg.getUrl());
		mUrl.setBodyType(1);
		list.add(mUrl);


		contentEntity.setContentArray(list);
		return contentEntity;
	}
}
