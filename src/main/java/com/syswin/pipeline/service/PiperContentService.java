package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.sub.api.db.model.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class PiperContentService {

	@Autowired
	private com.syswin.sub.api.ContentService subContentService;

	public PageInfo<Content> list(String publisherId, String keyword, Integer pageNo, Integer pageSize) {

		return subContentService.list(publisherId, keyword, pageNo, pageSize);
	}

	public void delete(String contentId) {

		subContentService.removeContent(contentId);
	}

	public void active(String contentId) {
		subContentService.activeContent(contentId);
	}
}
