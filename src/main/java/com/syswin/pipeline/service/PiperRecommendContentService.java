package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.repository.ReCommendContentRepository;
import com.syswin.pipeline.manage.dto.output.ContentOutput;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.ContentOutService;
import com.syswin.sub.api.ContentService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.ContentOut;
import com.syswin.sub.api.db.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class PiperRecommendContentService {

	@Autowired
	private ReCommendContentRepository reCommendContentRepository;

	@Autowired
	private PublisherService publisherService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private ContentOutService contentOutService;

	public PageInfo list(String userId, Integer pageNo, Integer pageSize) {

		List<ReCommendContent> reCommendContents = reCommendContentRepository.selectByUserId(userId);
		List<String> ids = reCommendContents.stream().map(r -> r.getContentId()).collect(Collectors.toList());
		PageInfo pageInfo = contentOutService.listByContentIds(ids, pageNo, pageSize);
		//BO转VO
		List<ContentOut> contentOuts = pageInfo.getList();
		List<ContentOutput> outputs = new ArrayList();

		//获取Publiser
		List<String> pids = contentOuts.stream().map(r -> r.getPublisherId()).collect(Collectors.toList());
		List<Publisher> publisers = null;
		if (pids.size() > 0) {
			publisers = publisherService.selectListByIds(pids, 0, 0);
		}
		for (ContentOut contentOut : contentOuts) {
			ContentOutput output = new ContentOutput();
			output.setContentId(contentOut.getContentId());
			output.setCreateTime(String.valueOf(contentOut.getCreateTime()));
			output.setListdesc(contentOut.getListdesc());
			output.setHasRecommend("1");
			//Todo 此处要修改为H5 链接
			output.setDecUrl("www.baidu.com/" + contentOut.getContentId());
			output.setPublisherId(contentOut.getPublisherId());
			if (publisers != null) {
				for (Publisher p : publisers) {
					if (contentOut.getPublisherId().equals(p.getPublisherId())) {
						output.setPublisherName(p.getName());
					}
				}
			}
			outputs.add(output);
		}
		pageInfo.setList(outputs);

		//重置pageInfo参数
		pageInfo.setTotal(outputs.size());
		pageInfo.setPages(1 + outputs.size() / pageSize);
		pageInfo.setNextPage(pageInfo.getPages() > pageNo ? pageNo + 1 : pageNo);

		return pageInfo;
	}

	public ReCommendContent add(String userId, String contentId) {
		if (StringUtils.isNullOrEmpty(contentId)) {
			throw new BusinessException("内容Id不能为空");
		}
		ReCommendContent reCommendContent = reCommendContentRepository.selectByContentId(contentId);
		if (reCommendContent != null) {
			throw new BusinessException("该内容已经在推荐中");
		}
		reCommendContent = new ReCommendContent();
		reCommendContent.setContentId(contentId);
		reCommendContent.setUserId(userId);
		reCommendContentRepository.insert(reCommendContent);
		return reCommendContent;
	}

	public void delete(String userId, String id) {
		if (StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(id)) {
			throw new BusinessException("用户或内容Id不能为空");
		}
		ReCommendContent reCommendContent = reCommendContentRepository.selectById(id);
		if (reCommendContent == null) {
			throw new BusinessException("该内容不存在推荐");
		}
		reCommendContentRepository.delete(id);
	}
}
