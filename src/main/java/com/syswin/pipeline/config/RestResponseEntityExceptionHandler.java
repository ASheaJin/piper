package com.syswin.pipeline.config;

import com.syswin.pipeline.service.psserver.impl.BusinessException;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.sub.api.exceptions.SubException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author:lhz
 * @date:2019/3/7 17:18
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler
				extends ResponseEntityExceptionHandler {

	private final static Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

	@Autowired
	LanguageChange languageChange;

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<com.syswin.pipeline.service.psserver.bean.ResponseEntity> handleExceptionInternal(
					Exception ex, WebRequest request) {
		ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK);
		String lang = request.getHeader("lang");
		String msg = "";
		try {
			msg = languageChange.getLangByStr(ex.getMessage(), lang);
		} catch (Exception e) {
			msg = ex.getMessage();
		}
		if (ex instanceof SubException || ex instanceof BusinessException) {

			logger.info("SubException | BusinessException: 业务异常  " + msg);

			return builder.body(new com.syswin.pipeline.service.psserver.bean.ResponseEntity("500", msg));
		} else {
			msg = languageChange.getLangByStr("ex.system.err", lang);
			logger.error("系统异常  :" + (msg == null ? ex.getMessage() : msg), ex);

			return builder.body(new com.syswin.pipeline.service.psserver.bean.ResponseEntity("501", msg == null ? ex.getMessage() : msg));
		}
	}
}