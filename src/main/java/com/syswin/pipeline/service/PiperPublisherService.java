package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.pipeline.manage.dto.output.PublisherManageVO;
import com.syswin.pipeline.psservice.APPPublisherService;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.psservice.UpdateMenuService;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.utils.ValidationUtil;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.PermissionUtil;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.ContentService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.utils.BeanConvertUtil;
import com.syswin.sub.api.utils.EnumsUtil;
import com.syswin.sub.api.vo.PublisherVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 出版社和内容发布的方法
 * Created by lhz on 2018/11/27.
 */
@Service
public class PiperPublisherService {

	private static final Logger logger = LoggerFactory.getLogger(PiperPublisherService.class);
	@Value("${app.pipeline.userId}")
	private String from;
	@Autowired
	private PSClientService psClientService;
	@Autowired
	private APPPublisherService appPublisherService;
	@Autowired
	private SendMessegeService sendMessegeService;

	@Autowired
	private PiperRecommendPublisherService piperRecommendPublisherService;

	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private ContentService subcontentService;
	@Autowired
	private AdminService adminService;

	@Autowired
	private LanguageChange languageChange;
	@Autowired
	private UpdateMenuService updateMenuService;

	@Autowired
	private PublisherService subPublisherService;

	@Value("${domain.promission}")
	private String domain;

	/**
	 * 创建出版社
	 *
	 * @param userId
	 * @param name
	 */
	public Publisher addPublisher(String userId, String name, String pmail, Integer ptype) {

		if (StringUtils.isNullOrEmpty(name)) {
			throw new BusinessException("ex.name.null");
		}
		if (!ValidationUtil.isChineseCharNum(name)) {
			throw new BusinessException("ex.name.invalid");
		}
		//域过滤
		if (!PermissionUtil.getdomains(domain, userId)) {
			throw new BusinessException(languageChange.getLangByUserId("ex.demain.err", new String[]{userId, domain}, userId));
		}
		// TODO: 2019/3/29 后期加入靓号处理
		String ptemail = pmail;
		if (StringUtils.isNullOrEmpty(pmail)) {
			ptemail = "p.10000001@" + from.split("@")[1];


			//获取最新创建的出版社
			Publisher lastPublisher = subPublisherService.getLastPublisher();
			if (lastPublisher != null) {
				int lastInt = Integer.parseInt(PatternUtils.getSubUtilSimple(lastPublisher.getPtemail(), from.split("@")[1]));
				ptemail = PatternUtils.getEmail(String.valueOf(lastInt + 1), from.split("@")[1]);
			}
		}
		//获取中间的数字

		Publisher publisher = subPublisherService.addPublisher(userId, name, ptemail, EnumsUtil.getPubliserTypeEnums(ptype));
		//调用新的登录
		appPublisherService.addPiperAcount(ptemail);
		//更新菜单Piper
		updateMenuService.updateMenu(from, userId);
		//		psClientService.loginTemail(ptemail);
		try {
			psClientService.sendTextmessage(languageChange.getLangByUserId("msg.publisherhascreate", new String[]{name}, userId), userId, 0);
			psClientService.sendTextmessage(ptemail, userId, 200);

			sendMessegeService.sendCard(ptemail, userId, "* " + name);
			//注册了出版社后登陆下
			sendMessegeService.sendTextMessage(languageChange.getLangByUserId("msg.pcreatetip", new String[]{name}, userId), userId, 0, ptemail);

		} catch (Exception e) {
			logger.error(e.getMessage() + "PS连接异常", ptemail, e);
		}
		return publisher;
	}

	public Publisher getLastPublisher() {
		return subPublisherService.getLastPublisher();
	}


	public Publisher getPubLisherById(String publisherId) {
		return subPublisherService.getPubLisherById(publisherId);
	}

	@Transactional
	public void delete(String publisherId) {
		subPublisherService.delete(publisherId);
		piperRecommendPublisherService.deleteByPid(publisherId);
	}

	/**
	 * 获取当前temail的个人出版社
	 *
	 * @param userId
	 * @return
	 */
	public PublisherVO getPubLisherByuserId(String userId) {
		PublisherVO publisherVO = BeanConvertUtil.map(subPublisherService.getPublisherByUserId(userId, PublisherTypeEnums.person), PublisherVO.class);
		publisherVO.setContents(subcontentService.getContentCount(publisherVO.getPublisherId()));
		publisherVO.setSubcribs(subscriptionService.getSubscribeCount(publisherVO.getPublisherId()));
		return publisherVO;
	}


	//================================ manage方法 =========================================>

	public PageInfo list(int pageNo, int pageSize, String keyword, String hasRecommend, String piperType, String userId) {
		PublisherTypeEnums pType = null;
		if (!StringUtils.isNullOrEmpty(piperType) && !"0".equals(piperType)) {
			pType = EnumsUtil.getPubliserTypeEnums(Integer.parseInt(piperType));
		}
		List<Publisher> publisherList = subPublisherService.list(pageNo, pageSize, keyword, pType, userId).getList();
		List<PublisherManageVO> pmVOList = new ArrayList<>();
		List<String> publisherIds = publisherList.stream().map((p) -> p.getPublisherId()).collect(Collectors.toList());
		List<ReCommendPublisher> reCommendPublisherList = null;
		if (publisherIds.size() > 0) {
			reCommendPublisherList = piperRecommendPublisherService.seletByPubliserIds(publisherIds);
		}
		if (publisherList == null) {
			throw new BusinessException("ex.publisher.null");
		}
		for (Publisher publisher : publisherList) {
			PublisherManageVO pmVO = new PublisherManageVO();
			pmVO.setCreatTime(pmVO.getCreatTime());
			pmVO.setUserId(publisher.getUserId());
			pmVO.setName(publisher.getName());
			pmVO.setPtemail(publisher.getPtemail());
			pmVO.setPublisherId(publisher.getPublisherId());
			pmVO.setPiperType(publisher.getPtype().getName());
			pmVO.setCreatTime(String.valueOf(publisher.getCreateTime()));
			if (reCommendPublisherList != null) {
				for (ReCommendPublisher rp : reCommendPublisherList) {
					if (publisher.getPublisherId().equals(rp.getPublisherId())) {
						pmVO.setHasRecommend("1");
					}
				}

			}
			if (StringUtils.isNullOrEmpty(hasRecommend)) {
				pmVOList.add(pmVO);
			} else {
				if (pmVO.getHasRecommend().equals(hasRecommend)) {
					pmVOList.add(pmVO);
				}
			}
		}

		PageInfo pageInfo = new PageInfo(publisherList);
//		pageInfo.setTotal(pmVOList.size());
//		if (pageSize != 0) {
//			pageInfo.setPages(1 + pmVOList.size() / pageSize);
//		}
//		pageInfo.setNextPage(pageInfo.getPages() > pageNo ? pageNo + 1 : pageNo);

		if (pmVOList != null) {
			pageInfo.setList(pmVOList);
		}
		return pageInfo;
	}

	//获取我创建的组织出版社
	public PageInfo getMyOrgPublisherList(String keyword, String userId, int pageNo, int pageSize) {
		Admin admin = adminService.getAdmin(userId, PublisherTypeEnums.organize);
		if (admin == null || admin.getStatus() == 0) {
			return new PageInfo();
		}
//		return subPublisherService.getOrgPublisherByuserId(keyword, userId);
		return subPublisherService.getOrgPublisherByuserId(keyword, userId, pageNo, pageSize);
	}

	public Publisher changeOrgPublishUser(String publishId, String curUserId, String changeUserId) {

		if (!ValidationUtil.isEmail(changeUserId)) {
			throw new BusinessException("ex.email.invalid");
		}
		Admin admin = adminService.getAdmin(changeUserId, PublisherTypeEnums.organize);

		Publisher publisher = subPublisherService.getPubLisherById(publishId);
		if (publisher == null) {
			throw new BusinessException("ex.publisher.null");
		}
//		curUserId = publisher.getUserId();
		if (admin == null) {
			adminService.add(curUserId, changeUserId, PublisherTypeEnums.organize);
		}
		if (!curUserId.equals(publisher.getUserId())) {
			throw new BusinessException("msg.nopermission");
		}
		//// TODO: 2019/5/28 此处需要清理该账号缓存,更新时间戳
		updateMenuService.updateMenu(publisher.getPtemail(), changeUserId);


		publisher.setUserId(changeUserId);
		int r = subPublisherService.update(publisher);
		if (r == 1) {
			sendMessegeService.sendCard(publisher.getPtemail(), curUserId, publisher.getName());
			sendMessegeService.sendTextMessage(languageChange.getLangByUserId("msg.changetip", new String[]{changeUserId}, curUserId), curUserId, 0, publisher.getPtemail());

			sendMessegeService.sendCard(publisher.getPtemail(), changeUserId, "* " + publisher.getName());
			sendMessegeService.sendTextMessage(languageChange.getLangByUserId("msg.changetip", new String[]{changeUserId}, changeUserId), changeUserId, 0, publisher.getPtemail());
		} else {
			throw new BusinessException("ex.sql.err");
		}
		return publisher;
	}
}
