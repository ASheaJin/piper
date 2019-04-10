package com.syswin.pipeline.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.db.repository.ReCommendContentRepository;
import com.syswin.pipeline.db.repository.ReCommendPublisherRepository;
import com.syswin.pipeline.service.ps.util.StringUtil;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class PiperRecommendPublisherService {

	@Autowired
	private ReCommendPublisherRepository reCommendPublisherRepository;
	@Autowired
	private PublisherService publisherService;

	public PageInfo<ReCommendPublisher> list(String userId, Integer pageNo, Integer pageSize) {


		pageNo = PageUtil.getPageNo(pageNo);
		pageSize = PageUtil.getPageSize(pageSize);

		PageHelper.startPage(pageNo, pageSize);

		List<ReCommendPublisher> reList;
		if (StringUtil.isEmpty(userId)) {
			reList = reCommendPublisherRepository.select();
		} else {
			List<Publisher> publisherList = publisherService.getPublisherListByUserId(userId, null);
			List<String> pids = publisherList.stream().map((p) -> p.getPublisherId()).collect(Collectors.toList());
			reList = reCommendPublisherRepository.seletByPubliserIds(pids);
		}
		PageInfo pageInfo = new PageInfo(reList);
		return pageInfo;
	}

	public ReCommendPublisher add(String userId, String publisherId) {
		if (StringUtils.isNullOrEmpty(publisherId)) {
			throw new BusinessException("出版社Id不能为空");
		}
		ReCommendPublisher reCommendPublisher = reCommendPublisherRepository.selectByPublisherId(publisherId);
		if (reCommendPublisher != null) {
			throw new BusinessException("该出版社已经在推荐中");
		}
		reCommendPublisher = new ReCommendPublisher();
		reCommendPublisher.setPublisherId(publisherId);
		reCommendPublisher.setUserId(userId);
		reCommendPublisherRepository.insert(reCommendPublisher);

		return reCommendPublisher;
	}

	public void deleteByPid(String publisherId) {
		if (StringUtils.isNullOrEmpty(publisherId)) {
			throw new BusinessException("出版社Id");
		}
		ReCommendPublisher reCommendPublisher = reCommendPublisherRepository.selectByPublisherId(publisherId);
		if (reCommendPublisher == null) {
			throw new BusinessException("该出版社没有被推荐");
		}

		reCommendPublisherRepository.delete(reCommendPublisher.getId());
	}

	public List<ReCommendPublisher> seletByPubliserIds(List<String> publisherIds) {
		List<ReCommendPublisher> reCommendPublisherList = reCommendPublisherRepository.seletByPubliserIds(publisherIds);
		return reCommendPublisherList;
	}

}
