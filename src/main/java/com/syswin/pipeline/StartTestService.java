package com.syswin.pipeline;

import com.syswin.pipeline.service.ps.impl.PSClientServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class StartTestService implements ApplicationRunner {

	private org.apache.logging.log4j.Logger logger = LogManager.getLogger(StartTestService.class);

	@Autowired
	private PSClientServiceImpl psclientService;

	@Override
	public void run(ApplicationArguments args) {
//		logger.info("start pipeline==={}", new Date());
//
//		psclientService.init();
	}
}
