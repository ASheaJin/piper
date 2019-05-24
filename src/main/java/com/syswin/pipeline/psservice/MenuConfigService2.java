package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.admin.config.IMenuConfigService;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MenuConfigService2 implements IMenuConfigService {

	private final static Logger logger = LoggerFactory.getLogger(MenuConfigService2.class);

	public List<String> getKey(String accountNo) {
		MsgHeader msgHeader = PsClientKeeper.msgHeader();
		logger.info("msgHeader2" + msgHeader.toString());
		//根据访问者的权限配置菜单 msgHeader 里面有用户的 信息
		List<String> aas = Arrays.asList(getKey(accountNo,"1"),getKey(accountNo,"2"));

		logger.info("aas2" + aas.toString());
		return aas;

	}

	@Override
	public String getKey(String accountNo, String roleType) {


		return accountNo + roleType;
	}

}
