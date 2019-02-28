package com.syswin.pipeline.manage.service;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**

 */
public interface ManageService<T> {

	Map<String, Object> list(HttpServletRequest request, Model model);
	T edit(T t);
	boolean add(T t);
	boolean remove(T t);
}
