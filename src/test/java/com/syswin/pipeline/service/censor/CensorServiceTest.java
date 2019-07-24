package com.syswin.pipeline.service.censor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 115477 on 2019/7/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CensorServiceTest {

    @Autowired
    private CensorService censorService;

    @Test
    public void sendCensor() {
        censorService.sendContentCensor("35128301189857280");
    }

    @Test
    public void sendCensor1() {
    }
}