package com.syswin.pipeline.psservice;

import com.syswin.pipeline.PiperApplication;
import com.syswin.ps.sdk.admin.config.IMenuConfigService;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MenuConfigService implements IMenuConfigService {

	private final static Logger logger = LoggerFactory.getLogger(MenuConfigService.class);

	@Value("${app.pipeline.userId}")
	private String apiper;

	public List<String> getKey(String accountNo) {
		MsgHeader msgHeader = PsClientKeeper.msgHeader();
		logger.info("msgHeader" + msgHeader.toString());
		//根据访问者的权限配置菜单 msgHeader 里面有用户的 信息
		List<String> menus = menu(msgHeader, accountNo);
		logger.info("menus" + menus.toString());
		return menus;

	}

	private List<String> menu(MsgHeader msgHeader, String accountNo) {
		List<String> menus = null;

		if ((menus = menuA(msgHeader, accountNo)) == null) {
			menus = menuP(msgHeader, accountNo);
		}
		if (menus == null) {
			menus = Arrays.asList(getKey(accountNo, "nomenu"));

		}
		return menus;
	}

	private List<String> menuP(MsgHeader msgHeader, String accountNo) {
		if(apiper.equals(msgHeader.getReceiver())) {
			List<String> aas = Arrays.asList(getKey(accountNo, "1"), getKey(accountNo, "2"));
		}
		return null;
	}

	private List<String> menuA(MsgHeader msgHeader, String accountNo) {

		List<String> aas = Arrays.asList(getKey(accountNo, "1"), getKey(accountNo, "2"));
		return null;
	}

	@Override
	public String getKey(String accountNo, String roleType) {

		return accountNo + roleType;
	}

}
