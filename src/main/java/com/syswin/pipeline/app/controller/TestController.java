package com.syswin.pipeline.app.controller;

import com.syswin.pipeline.app.dto.TokenOutParam;
import com.syswin.pipeline.db.model.Token;
import com.syswin.pipeline.service.TokenService;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.sub.api.exceptions.SubException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by 115477 on 2018/11/28.
 */
@CrossOrigin
@RestController
@RequestMapping("/test")
@Api(value = "test", tags = "test")
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@GetMapping("getValue")
	@ApiOperation(
					value = "获取Token"
	)
	public String getToken(HttpServletRequest request, String aa) {

		String value = messageSource.getMessage("name", new String[]{aa}, localeResolver.resolveLocale(request));//获取转换后的字符。需要在messages.properties,messages_enUS.properties,messages.properties 中配置。
		return value;
	}


	@GetMapping("test")
	@ApiOperation(
					value = "获取Token"
	)
	public String test() {

		throw new SubException("ex.userid.null");
	}


}
