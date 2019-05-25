package com.syswin.pipeline.manage.controller;


import com.syswin.pipeline.manage.service.ManageService;
import com.syswin.pipeline.utils.SpringContextUtils;
import com.syswin.pipeline.utils.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;


/**
 * 基础Action
 * 反射匹配不同Entity 实现多页面统配
 *
 * @param <T>
 * @author: lhz
 * @date: 2019年1月8日 下午5:07:51
 */
public class BaseAction<T> {

	// model's Class
	protected Class<T> entityClass;

	protected String entityClassName;


	//它对被批注的代码元素内部的某些警告保持静默。 
	@SuppressWarnings("unchecked")
	public BaseAction() {
		super();
		// 通过反射取得Entity的Class.
		try {
			entityClass = (Class<T>) ((ParameterizedType) getClass()
							.getGenericSuperclass()).getActualTypeArguments()[0];
			entityClassName = entityClass.getSimpleName();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前beanSeavice的实现
	 * @return
	 */
	protected ManageService<T> getBaseService() {
		ManageService<T> baseService = SpringContextUtils.getBean(getlowBeanName() + "ManageServiceImpl");
		return baseService;
	}

	protected String getlowBeanName() {
		return entityClassName.substring(0, 1).toLowerCase() + entityClassName.substring(1);
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) {

		model.addAllAttributes(getBaseService().list(request,  model));
		reBuildparam(request, model);

		return "piper/" + getlowBeanName() + "/list";
	}

	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, Model model) {

		model.addAllAttributes(getBaseService().list(request,  model));
		reBuildparam(request, model);

		return "piper/" + getlowBeanName() + "/edit";
	}

	@RequestMapping("/add")
	public String add(HttpServletRequest request, Model model) {

		reBuildparam(request, model);
		model.addAllAttributes(getBaseService().list(request,model));

		return "piper/" + getlowBeanName() + "/edit";
	}


	private void reBuildparam(HttpServletRequest request, Model model) {

		int pageIndex = StringUtils.getParam(request, "pageIndex", 1);
		int pageSize = StringUtils.getParam(request, "pageSize", 10);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);

	}



}
