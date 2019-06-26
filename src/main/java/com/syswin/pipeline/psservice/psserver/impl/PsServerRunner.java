package com.syswin.pipeline.psservice.psserver.impl;

import com.syswin.temail.ps.common.codec.SimpleBodyExtractor;
import com.syswin.temail.ps.common.packet.PacketEncryptor;
import com.syswin.temail.ps.common.packet.PacketSigner;
import com.syswin.temail.ps.common.packet.PublicKeyPacketVerifier;
import com.syswin.temail.ps.server.PsServer;
import com.syswin.temail.ps.server.service.RequestService;
import com.syswin.temail.ps.server.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by GuoMengnan on 2018/9/27.
 */

@Configuration
public class PsServerRunner implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(PsServerRunner.class);


	private PsServer psServer;
	@Autowired
	CDTPProperties cDTPProperties;

	/**
	 * 初始化CDTP监听
	 *
	 * @param sessionService
	 * @param requestService
	 * @return
	 */
	@Bean
	PsServer psServer(SessionService sessionService, RequestService requestService) {
		psServer = new PsServer(
						sessionService, requestService,
						cDTPProperties.getPort(), cDTPProperties.getIdleTimeSeconds(),
						SimpleBodyExtractor.INSTANCE,
						PacketSigner.NoOp,
						new PublicKeyPacketVerifier(),
						PacketEncryptor.NoOp);
		return psServer;
	}

	/**
	 * Callback used to run the bean.
	 *
	 * @param args incoming application arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//暂时不用ps先关闭
		this.psServer.start();
		logger.info("psserver--- start");
	}
}
