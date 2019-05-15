package com.syswin.pipeline.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.dto.output.ReComPublisherVO;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.db.repository.ReCommendPublisherRepository;
import com.syswin.pipeline.manage.dto.output.PublisherManageVO;
import com.syswin.pipeline.service.ps.util.StringUtil;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.db.model.Subscription;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
	@Autowired
	private SubscriptionService subscriptionService;

	public PageInfo listManage(String userId, Integer pageNo, Integer pageSize) {


		pageNo = PageUtil.getPageNo(pageNo);
		pageSize = PageUtil.getPageSize(pageSize);


		List<ReCommendPublisher> reList;
		List<String> pids = null;
		if (StringUtil.isEmpty(userId)) {
			PageHelper.startPage(pageNo, pageSize);
			reList = reCommendPublisherRepository.select();
			pids = reList.stream().map((p) -> p.getPublisherId()).collect(Collectors.toList());
		} else {
			List<Publisher> publisherList = publisherService.getPublisherListByUserId(userId, null);
			pids = publisherList.stream().map((p) -> p.getPublisherId()).collect(Collectors.toList());
			PageHelper.startPage(pageNo, pageSize);
			reList = reCommendPublisherRepository.seletByPubliserIds(pids);
		}
		List<ReComPublisherVO> pmVOList = new ArrayList<>();
		if (pids.size() > 0) {
			List<Publisher> publisherList = publisherService.selectListByIds(pids, pageNo, pageSize);
			for (Publisher publisher : publisherList) {
				ReComPublisherVO pmVO = new ReComPublisherVO();
				pmVO.setName(publisher.getName());
				pmVO.setPtemail(publisher.getPtemail());
				pmVO.setPublisherId(publisher.getPublisherId());
				pmVO.setPiperType(publisher.getPtype().getName());
				pmVOList.add(pmVO);
			}
		}
		PageInfo pageInfo = new PageInfo(reList);
		pageInfo.setList(pmVOList);

		return pageInfo;
	}


	public PageInfo listApi(String userId, Integer pageNo, Integer pageSize) {


		pageNo = PageUtil.getPageNo(pageNo);
		pageSize = PageUtil.getPageSize(pageSize);

		List<ReCommendPublisher> reList;
		List<String> pids = null;
		PageHelper.startPage(pageNo, pageSize);
		reList = reCommendPublisherRepository.select();
		PageInfo pageInfo = new PageInfo(reList);

		pids = reList.stream().map((p) -> p.getPublisherId()).collect(Collectors.toList());
		List<Publisher> publisherList = null;
		List<PublisherManageVO> pmVOList = new ArrayList<>();

		if (pids.size() > 0) {
			publisherList = publisherService.selectListByIds(pids, pageNo, pageSize);
			for (Publisher publisher : publisherList) {
				PublisherManageVO pmVO = new PublisherManageVO();
				pmVO.setCreatTime(pmVO.getCreatTime());
				pmVO.setUserId(publisher.getUserId());
				pmVO.setName(publisher.getName());
				pmVO.setPtemail(publisher.getPtemail());
				pmVO.setPublisherId(publisher.getPublisherId());
				pmVO.setPiperType(publisher.getPtype().getName());
				pmVO.setHasRecommend("1");

				Subscription sub = subscriptionService.getSub(userId, publisher.getPublisherId());
				if (sub != null) {
					pmVO.setHasSub("1");
				}
				pmVOList.add(pmVO);
			}
		}


		pageInfo.setList(pmVOList);
		return pageInfo;
	}

	public ReCommendPublisher add(String userId, String publisherId) {
		if (StringUtils.isNullOrEmpty(publisherId)) {
			throw new BusinessException("ex.publisherid.null");
		}
		ReCommendPublisher reCommendPublisher = reCommendPublisherRepository.selectByPublisherId(publisherId);
		if (reCommendPublisher != null) {
			throw new BusinessException("ex.hascommend");
		}
		Publisher publisher = publisherService.getPubLisherById(publisherId);
		if (publisher == null) {
			throw new BusinessException("ex.publisher.null");
		}
		if (publisher.getPtype().equals(PublisherTypeEnums.organize)) {
			throw new BusinessException("ex.nosupport");
		}
		reCommendPublisher = new ReCommendPublisher();
		reCommendPublisher.setPublisherId(publisherId);
		reCommendPublisher.setUserId(userId);
		reCommendPublisherRepository.insert(reCommendPublisher);

		return reCommendPublisher;
	}

	public void deleteByPid(String publisherId) {
		if (StringUtils.isNullOrEmpty(publisherId)) {
			throw new BusinessException("ex.publisherid.null");
		}
		ReCommendPublisher reCommendPublisher = reCommendPublisherRepository.selectByPublisherId(publisherId);
		if (reCommendPublisher == null) {
			return;
		}

		reCommendPublisherRepository.delete(reCommendPublisher.getId());
	}

	public List<ReCommendPublisher> seletByPubliserIds(List<String> publisherIds) {
		List<ReCommendPublisher> reCommendPublisherList = reCommendPublisherRepository.seletByPubliserIds(publisherIds);
		return reCommendPublisherList;
	}

}
