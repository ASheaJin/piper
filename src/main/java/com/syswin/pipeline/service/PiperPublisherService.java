package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.app.controller.PSSeverController;
import com.syswin.pipeline.manage.vo.output.AdminManageVO;
import com.syswin.pipeline.manage.vo.output.PublisherManageVO;
import com.syswin.pipeline.service.bussiness.impl.SendMessegeService;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.util.ValidationUtil;
import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.utils.MessageUtil;
import com.syswin.pipeline.utils.PatternUtils;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.db.model.Admin;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.exceptions.SubException;
import com.syswin.sub.api.utils.BeanConvertUtil;
import com.syswin.sub.api.utils.EnumsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 出版社和内容发布的方法
 * Created by lhz on 2018/11/27.
 */
@Service
public class PiperPublisherService {

	private static final Logger logger = LoggerFactory.getLogger(PSSeverController.class);
	@Value("${app.pipeline.userId}")
	private String from;
	@Autowired
	PSClientService psClientService;
	@Autowired
	SendMessegeService sendMessegeService;
	@Autowired
	AdminService adminService;

	@Autowired
	private com.syswin.sub.api.PublisherService subPublisherService;

	/**
	 * 创建出版社
	 *
	 * @param userId
	 * @param name
	 */
	public Publisher addPublisher(String userId, String name, String pmail, Integer ptype) {

		if (StringUtils.isNullOrEmpty(name)) {
			throw new BusinessException("名称不能为空");
		}
		if (!ValidationUtil.isChineseCharNum(name)) {
			throw new BusinessException("名称只能是中文、字母、数字及组合");
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
		psClientService.loginTemail(ptemail);
		try {
			psClientService.sendTextmessage(MessageUtil.sendCreatePublisher(ptemail, name), userId, 0);
			psClientService.sendTextmessage(MessageUtil.shareNewTip(ptemail), userId, 200);

			sendMessegeService.sendCard(ptemail, userId, "* " + name);
			//注册了出版社后登陆下

			sendMessegeService.sendTextmessage("<" + name + ">创建成功，您发的所有消息都会同步到订阅者", userId, 0, ptemail);

		} catch (Exception e) {
			throw new SubException("PS连接异常");
		}
		return publisher;
	}

	public Publisher getLastPublisher() {
		return subPublisherService.getLastPublisher();
	}

	/**
	 * 发布文章
	 *
	 * @param userId
	 * @param article
	 * @param bodyType 1文本 2语音 3图片 10视频 14文件  see http://wiki.syswin.com/pages/viewpage.action?pageId=33689922
	 */
	@Deprecated
	public void publishContent(String userId, String article, int bodyType) {

	}

	public Publisher getPubLisherById(String publisherId) {
		return subPublisherService.getPubLisherById(publisherId);
	}

	public void delete(String publisherId) {
		subPublisherService.delete(publisherId);
	}

	/**
	 * 获取当前temail的出版社
	 *
	 * @param userId
	 * @return
	 */
	public Publisher getPubLisherByuserId(String userId) {
		return subPublisherService.getPersonPublisherByuserId(userId);
	}


	//================================ manage方法 =========================================>

	public PageInfo<Publisher> list(int pageIndex, int pageSize, String keyword, String piperType, String userId) {
		PublisherTypeEnums pType = null;
		if (!StringUtils.isNullOrEmpty(piperType) && !"0".equals(piperType)) {
			pType = EnumsUtil.getPubliserTypeEnums(Integer.parseInt(piperType));
		}
		List<Publisher> publisherList = subPublisherService.list(pageIndex, pageSize, keyword, pType, userId);
		List<PublisherManageVO> pmVOList = new ArrayList<>();
		for (Publisher publisher : publisherList) {
			PublisherManageVO pmVO = new PublisherManageVO();
			pmVO.setCreatTime(pmVO.getCreatTime());
			pmVO.setUserId(publisher.getUserId());
			pmVO.setName(publisher.getName());
			pmVO.setPtemail(publisher.getPtemail());
			pmVO.setPublisherId(publisher.getPublisherId());
			pmVO.setPiperType(publisher.getPtype().getName());
			pmVOList.add(pmVO);
		}

		PageInfo pageInfo = new PageInfo<>(publisherList);
		pageInfo.setList(pmVOList);
		return pageInfo;
	}

	//获取我创建的组织出版社
	public List<Publisher> getMyOrgPublisherList(String keyword, String userId, int pageNo, int pageSize) {
		Admin admin = adminService.getAdmin(userId);
		if (admin == null || admin.getStatus() == 0) {
			return new ArrayList<>();
		}
//		return subPublisherService.getOrgPublisherByuserId(keyword, userId);
		return subPublisherService.getOrgPublisherByuserId(keyword, userId, pageNo, pageSize);
	}
}
