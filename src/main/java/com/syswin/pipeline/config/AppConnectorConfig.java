package com.syswin.pipeline.config;

import com.syswin.ps.sdk.utils.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


//@Component
//@Log4j2
public class AppConnectorConfig implements BeanPostProcessor, BeanFactoryAware {

	@Value("${server.servlet.context-path}")
	private String path;

	@Autowired
	TomcatServletWebServerFactory tomcatWebServer;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return null;
	}


	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		tomcatWebServer.setContextPath(path);
	}


}
