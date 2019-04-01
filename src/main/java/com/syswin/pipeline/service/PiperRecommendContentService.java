package com.syswin.pipeline.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.repository.ReCommendContentRepository;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.db.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class PiperRecommendContentService {

	@Autowired
	private ReCommendContentRepository reCommendContentRepository;

	public PageInfo<ReCommendContent> list(int pageIndex, int pageSize) {

		pageIndex = pageIndex < 1 ? 1 : pageIndex;
		pageSize = pageSize > 30 || pageSize < 1 ? 30 : pageSize;

		PageHelper.startPage(pageIndex, pageSize);
		List<ReCommendContent> reList = reCommendContentRepository.select();
		PageInfo<ReCommendContent> pageInfo = new PageInfo<>(reList);
		return pageInfo;
	}

	public ReCommendContent add(String userId, String contentId) {
		if (StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(contentId)) {
			throw new BusinessException("用户或内容Id不能为空");
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
