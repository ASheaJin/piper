package com.syswin.pipeline.config;

import com.syswin.ps.sdk.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Connector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.stereotype.Component;

/**
 * @author:lhz
 * @date:2019/5/23 21:20
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


@Component
public class PiperConnectorConfig implements BeanPostProcessor, BeanFactoryAware {
	private static final Logger log = LogManager.getLogger(com.syswin.ps.sdk.admin.config.PxSAdminConnectorConfig.class);
	@Value("${server.port}")
	private Integer port;
	@Value("${server.servlet.context-path}")
	private String contextpath;
	@Autowired
	TomcatServletWebServerFactory tomcatWebServer;
	@Autowired
	ServerProperties serverProperties;

	public PiperConnectorConfig() {
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		serverProperties.setPort(port);
		serverProperties.getServlet().setContextPath(contextpath);
//		tomcatWebServer.setContextPath(contextpath);
	}
}
