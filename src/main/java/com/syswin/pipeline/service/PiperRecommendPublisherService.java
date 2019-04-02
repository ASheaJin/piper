package com.syswin.pipeline.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.db.repository.ReCommendContentRepository;
import com.syswin.pipeline.db.repository.ReCommendPublisherRepository;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class PiperRecommendPublisherService {

	@Autowired
	private ReCommendPublisherRepository reCommendPublisherRepository;

	public PageInfo<ReCommendPublisher> list(int pageIndex, int pageSize) {

		pageIndex = pageIndex < 1 ? 1 : pageIndex;
		pageSize = pageSize > 30 || pageSize < 1 ? 30 : pageSize;

		PageHelper.startPage(pageIndex, pageSize);
		List<ReCommendPublisher> reList = reCommendPublisherRepository.select();
		PageInfo<ReCommendPublisher> pageInfo = new PageInfo<>(reList);
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

	public void delete(String id) {
		if (StringUtils.isNullOrEmpty(id)) {
			throw new BusinessException("用户或出版社Id不能为空");
		}
		ReCommendPublisher reCommendPublisher = reCommendPublisherRepository.selectById(id);
		if (reCommendPublisher == null) {
			throw new BusinessException("该出版社不存在推荐");
		}

		reCommendPublisherRepository.delete(id);
	}

	public List<ReCommendPublisher> seletByPubliserIds(List<String> publisherIds) {
		List<ReCommendPublisher> reCommendPublisherList = reCommendPublisherRepository.seletByPubliserIds(publisherIds);
		return reCommendPublisherList;
	}
}
