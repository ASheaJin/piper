package com.syswin.pipeline.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.pipeline.db.repository.ReCommendContentRepository;
import com.syswin.pipeline.manage.dto.output.ContentOutput;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.ContentOutService;
import com.syswin.sub.api.db.model.Content;
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
public class PiperContentService {

	@Autowired
	private ContentOutService contentOutService;
	@Autowired
	private com.syswin.sub.api.ContentService subContentService;

	@Autowired
	private ReCommendContentRepository reCommendContentRepository;

	public PageInfo listManage(String publisherId, String hasRecommend, Integer pageNo, Integer pageSize) {

		PageInfo pageInfo = contentOutService.listByPublisherId(publisherId, pageNo, pageSize);
		List<ContentOut> contentOuts = pageInfo.getList();
		List<ContentOutput> outputs = new ArrayList<>();
		List<String> cids = contentOuts.stream().map(r -> r.getContentId()).collect(Collectors.toList());
		List<ReCommendContent> reCommendContents = null;
		if (cids.size() > 0) {
			reCommendContents = reCommendContentRepository.selectByContentIds(cids);
		}
		for (ContentOut contentOut : contentOuts) {
			ContentOutput output = new ContentOutput();
			output.setContentId(contentOut.getContentId());
			output.setCreateTime(String.valueOf(contentOut.getCreateTime()));
			output.setListdesc(contentOut.getListdesc());
			//Todo 此处要修改为H5 链接
			output.setDecUrl("www.baidu.com/" + contentOut.getContentId());
			output.setPublisherId(contentOut.getPublisherId());
			if (reCommendContents != null) {
				for (ReCommendContent rcd : reCommendContents) {

					if (rcd.getContentId().equals(contentOut.getContentId())) {
						output.setHasRecommend("1");
					}
				}
			}
			//如果查询的不是推荐的，全部加入
			if (StringUtils.isNullOrEmpty(hasRecommend)) {
				outputs.add(output);
			} else {
				//查询条件过滤
				if (output.getHasRecommend().equals(hasRecommend)) {
					outputs.add(output);
				}
			}
		}
		pageInfo.setList(outputs);
		return pageInfo;
	}

	public void delete(String contentId) {

		subContentService.removeContent(contentId);
	}

	public void active(String contentId) {
		subContentService.activeContent(contentId);
	}
}
