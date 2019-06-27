package com.syswin.pipeline.psservice;

import com.syswin.ps.sdk.service.KmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PsAppExampleApplicationTests {

    @Autowired
    KmsService kmsService;

    @Test
    public void contextLoads() {

        kmsService.sign("a.door_syswin@syswin.com","aaaaa");

    }
}

