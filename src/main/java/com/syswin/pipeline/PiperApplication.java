package com.syswin.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by 115477 on 2018/11/27.
 */
@SpringBootApplication(scanBasePackages = {"com.syswin"})
public class PiperApplication {
	private final static Logger logger = LoggerFactory.getLogger(PiperApplication.class);

	public static void main(String[] args) throws Exception {
		ApplicationContext appContext = SpringApplication.run(PiperApplication.class, args);

	}

}
