package com.syswin.pipeline.psservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author:lhz
 * @date:2019/6/3 17:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterServerTest {

    @Autowired
    RegisterServer registerServer;

    @Test
    public void registerAccount() {
        //TODO 待验证
        registerServer.registerAccout("p.xxxxxddd@msgseal.com");
    }

}
