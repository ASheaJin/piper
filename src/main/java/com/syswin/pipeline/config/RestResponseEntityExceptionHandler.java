package com.syswin.pipeline.config;

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

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<com.syswin.pipeline.service.psserver.bean.ResponseEntity> handleExceptionInternal(
					Exception ex, WebRequest request) {
		ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK);
		return builder.body(new com.syswin.pipeline.service.psserver.bean.ResponseEntity("500", ex.getMessage()));
	}
}