package com.syswin.pipeline.manage.shiro;

import java.util.UUID;

/**
 * Created by 115477 on 2019/4/1.
 */
public class DefaultTokenManagerImpl  extends AbstractTokenManager{

    @Override
    public String createStringToken(String userCode) {
        //创建简易的32为uuid
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public boolean checkToken(StatelessToken model) {
        return super.checkMemoryToken(model);
    }
 }
