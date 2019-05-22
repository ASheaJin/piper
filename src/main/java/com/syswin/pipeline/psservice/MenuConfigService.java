package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.admin.config.IMenuConfigService;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MenuConfigService implements IMenuConfigService {
    @Override
    public List<String> getKey(String accountNo) {
        MsgHeader msgHeader=PsClientKeeper.msgHeader();
        //根据访问者的权限配置菜单 msgHeader 里面有用户的 信息

        return Arrays.asList(accountNo+"1");

    }

    @Override
    public String getKey(String accountNo, String roleType) {
        return accountNo+roleType;
    }

}
