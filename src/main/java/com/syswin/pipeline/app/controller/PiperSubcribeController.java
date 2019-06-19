package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.*;
import com.syswin.pipeline.app.dto.output.MarkVO;
import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.StringUtils;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 出版社的创建 查看 订阅 搜索
 * Created by 115477 on 2018/11/27.
 */
@CrossOrigin
@RestController
@RequestMapping("/subcribe")
@Api(value = "subcribe", tags = "subcribe")
public class PiperSubcribeController {


	@Autowired
	private PiperSubscriptionService subscriptionService;

	@Autowired
	private LanguageChange languageChange;

	@PostMapping("/checkSub")
	public ResponseEntity checkSub(@RequestBody CheckSubInput cis) {
		//京交会根据内容判断是否订阅
		return new ResponseEntity(subscriptionService.getsubscribeByUidCid(cis.getUserId(), cis.getPublisherId()));
	}


	@PostMapping("/getmysubsion")
	public ResponseEntity getmysubsion(@RequestBody RecomListParam upm) {
		Integer pageNo = StringUtils.isNullOrEmpty(upm.getPageNo()) ? 1 : Integer.parseInt(upm.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(upm.getPageSize()) ? 20 : Integer.parseInt(upm.getPageSize());


		return new ResponseEntity(subscriptionService.getPersonSubscribtions(upm.getUserId(), pageNo, pageSize));
	}

	@PostMapping("/getPageMarks")
	public ResponseEntity getPageMarks(HttpServletRequest request, @RequestBody UserIdParam user) {
		String lang = request.getHeader("lang");
		List<MarkVO> marks = new ArrayList<>();
		Integer numbperson = subscriptionService.getSubCount(user.getUserId(), PublisherTypeEnums.person);
		if (numbperson > 0) {
			String person = languageChange.getLangByStr("mark.a.PersonSubsions", lang);
			MarkVO mark = new MarkVO();
			mark.setMark(person);
			mark.setInterfaceStr("getPersonSubsions");
			marks.add(mark);
		}
		Integer numborg = subscriptionService.getSubCount(user.getUserId(), PublisherTypeEnums.organize);
		if (numborg > 0) {
			String org = languageChange.getLangByStr("mark.a.OrgSubsions", lang);
			MarkVO mark = new MarkVO();
			mark.setMark(org);
			mark.setInterfaceStr("getOrgSubsions");
			marks.add(mark);
		}

		return new ResponseEntity(marks);
	}

	@PostMapping("/getPersonSubsions")
	public ResponseEntity getPersonSubsions(@RequestBody RecomListParam upm) {
		Integer pageNo = StringUtils.isNullOrEmpty(upm.getPageNo()) ? 1 : Integer.parseInt(upm.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(upm.getPageSize()) ? 20 : Integer.parseInt(upm.getPageSize());


		return new ResponseEntity(subscriptionService.getPersonSubscribtions(upm.getUserId(), pageNo, pageSize));
	}

	@PostMapping("/getOrgSubsions")
	public ResponseEntity getOrgSubsions(@RequestBody RecomListParam upm) {
		Integer pageNo = StringUtils.isNullOrEmpty(upm.getPageNo()) ? 1 : Integer.parseInt(upm.getPageNo());
		Integer pageSize = StringUtils.isNullOrEmpty(upm.getPageSize()) ? 20 : Integer.parseInt(upm.getPageSize());


		return new ResponseEntity(subscriptionService.getOrgSubscribtions(upm.getUserId(), pageNo, pageSize));
	}

	@RequestMapping(value = "/getPubSubsions", method = RequestMethod.POST)
	public ResponseEntity getPublisersubsions(@RequestBody SubSearchParam subSearchParam) {
		int pageno = StringUtils.getInteger(subSearchParam.getPageNo()) == 0 ? 1 : StringUtils.getInteger(subSearchParam.getPageNo());
		int pagesize = StringUtils.getInteger(subSearchParam.getPageSize()) == 0 ? 20 : StringUtils.getInteger(subSearchParam.getPageSize());

		return new ResponseEntity(subscriptionService.getSubscribersByUserId(subSearchParam.getKeyword(), subSearchParam.getUserId(), subSearchParam.getPublisherId(), PublisherTypeEnums.organize, pageno, pagesize));
	}
}
