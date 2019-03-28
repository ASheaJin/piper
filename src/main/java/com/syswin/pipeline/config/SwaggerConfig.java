package com.syswin.pipeline.config;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
//@ComponentScan("com.syswin.door.api.core.controller")
public class SwaggerConfig {

	//swagger文档是否可用
	private static boolean enable = true;

	public SwaggerConfig() {
		enable = true;
	}

	@Bean
	public Docket piperApi() {
		ApiInfo apiInfo = new ApiInfo(
						"PiperAPI接口",
						"PiperAPI接口",
						"",
						"",
						new Contact("weihongyi", "", "weihongyi@syswin.com"),
						"",
						"",
						Lists.newArrayList()
		);
		Set<String> setProtocol = new HashSet<String>();
		setProtocol.add("http");
		setProtocol.add("https");
		Set<String> setProduce = new HashSet<String>();
		setProduce.add("application/json");
		List<Parameter> listParameter = new ArrayList<Parameter>();
		return new Docket(DocumentationType.SWAGGER_2)
						.select()
						.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
						.paths(PathSelectors.regex("/(admin|ps|publish|subcribe)/.*"))
						.build()
						.groupName("api")
						.pathMapping("/")
						.enable(enable)
						.apiInfo(apiInfo)
						.useDefaultResponseMessages(false)
						.globalOperationParameters(listParameter)
						.protocols(setProtocol)
						.produces(setProduce);
	}


	@Bean
	public Docket managerApi() {
		ApiInfo apiInfo = new ApiInfo(
						"Piper后台接口",
						"Piper后台接口",
						"",
						"",
						new Contact("weihongyi", "", "weihongyi@syswin.com"),
						"",
						"",
						Lists.newArrayList()
		);
		Set<String> setProtocol = new HashSet<String>();
		setProtocol.add("http");
		setProtocol.add("https");
		Set<String> setProduce = new HashSet<String>();
		setProduce.add("application/json");
		List<Parameter> listParameter = new ArrayList<Parameter>();
		return new Docket(DocumentationType.SWAGGER_2)
						.select()
						.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
						.paths(PathSelectors.regex("/manage/.*"))
						.build()
						.groupName("manage")
						.pathMapping("/")
						.enable(enable)
						.apiInfo(apiInfo)
						.useDefaultResponseMessages(false)
						.globalOperationParameters(listParameter)
						.protocols(setProtocol)
						.produces(setProduce);
	}
}