package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.db.model.DataAnalysis;
import com.syswin.pipeline.db.repository.DataAnalysisRepository;
import com.syswin.pipeline.enums.UrlEnums;
import com.syswin.pipeline.service.ps.util.StringUtil;
import com.syswin.pipeline.utils.DateUtil;
import com.syswin.pipeline.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容举报操作、删帖
 * Created by 115477 on 2018/11/28.
 */
@Controller
@RequestMapping("/manage")
public class ManageIndexController {

	@Autowired
	DataAnalysisRepository dataAnalysisRepository;

	@RequestMapping("/index")
	public String index(Model model) {
//		ModelAndView mav = new ModelAndView("/index");
//		List list = new ArrayList();
//		Map map =  new HashMap();
//		PageInfo<Person> pageInfo = new PageInfo<Person>(list);
//		model.addAttribute("pageInfo",pageInfo);
		return "piper/index";
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) {

		String createTime = StringUtils.getParam(request, "createTime", null);
		if (StringUtil.isNotEmpty(createTime)) {
			model.addAttribute("createTime", createTime);
		}
		Map<String, Object> map = new HashMap<>();
		List<DataAnalysis> dataAnalysisList = new ArrayList<>();
/**
 * 日志管理：
 *  xxx 时间内
 * 新增   人数 跳转到人具体登录
 * 活跃   人数
 * 出版社  发布  订阅
 * 交易   充值
 */
		for (UrlEnums urlEnum : UrlEnums.values()) {
			DataAnalysis dataAnalysis = new DataAnalysis();
			dataAnalysis.setType(urlEnum.getCode());
			dataAnalysis.setUrl(urlEnum.getUrl());
			dataAnalysis.setName(urlEnum.getName());
			dataAnalysis.setDatacount(switchCount(urlEnum.getCode(), createTime));
			dataAnalysisList.add(dataAnalysis);
		}
		int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
		int pageSize = StringUtils.getParam(request, "pageSize", 10);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		map.put("total", 10);
		map.put("data", dataAnalysisList);
		model.addAllAttributes(map);
		return "list";
	}

	public int switchCount(int code, String createTime) {

		int startTime = 0, endTime = 0;
		if (StringUtil.isNotEmpty(createTime)) {
			String[] times = createTime.split(" 至 ");
			startTime = DateUtil.switchDate(times[0]);
			endTime = DateUtil.switchDate(times[1]);
		}
		int count = 0;
		switch (code) {
			case 1:
				count = dataAnalysisRepository.selectAccount(startTime, endTime);
				break;
			case 2:
				count = dataAnalysisRepository.selectServerLog(startTime, endTime);
				break;
			case 3:
				count = dataAnalysisRepository.selectPublisher(startTime, endTime);
				break;
			case 4:
				count = dataAnalysisRepository.selectTransaction(startTime, endTime);
				break;
		}
		return count;
	}
}
