package com.syswin.pipeline.manage.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.syswin.pipeline.app.dto.ResponseEntity;
import com.syswin.pipeline.utils.FastJsonUtil;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


/**
 * 无状态授权过滤器
 *
 * @author luanhy
 */
public class StatelessAuthcFilter extends AccessControlFilter {
	private static final Logger logger = LoggerFactory.getLogger(StatelessAuthcFilter.class);

	@Autowired
	private TokenManager tokenManager;

	public TokenManager getTokenManager() {
		return tokenManager;
	}

	public void setTokenManager(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
	                                  ServletResponse response, Object mappedValue) throws Exception {
		if (request instanceof HttpServletRequest) {
			if ("OPTIONS".equals(((HttpServletRequest) request).getMethod().toUpperCase())) {
				return true;
			}
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// 前段token授权信息放在请求头中传入
		String authorization = ((HttpServletRequest) request).getHeader("authorization");
		if (StringUtils.isEmpty(authorization)) {
			onLoginFail(response, "请求头不包含认证信息authorization");
			return false;
		}
		// 获取无状态Token
		StatelessToken accessToken = tokenManager.getToken(authorization);
		try {
			// 委托给Realm进行登录
			getSubject(request, response).login(accessToken);
		} catch (Exception e) {
			logger.error("auth error:" + e.getMessage(), e);
			onLoginFail(response, "auth error:" + e.getMessage()); // 6、登录失败
			return false;
		}
		// 通过isPermitted 才能调用doGetAuthorizationInfo方法获取权限信息
		getSubject(request, response).isPermitted(httpRequest.getRequestURI());
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
	                                 ServletResponse response) throws Exception {
		return false;
	}

	//登录失败时默认返回501状态码
	private void onLoginFail(ServletResponse response, String errorMsg) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.setContentType("application/json");
		httpResponse.setCharacterEncoding("utf-8");

		String json = FastJsonUtil.toJson(new ResponseEntity("501", errorMsg));

		httpResponse.getWriter().write(json);
		httpResponse.getWriter().close();
	}

}

