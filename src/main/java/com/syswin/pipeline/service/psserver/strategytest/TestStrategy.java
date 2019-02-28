package com.syswin.pipeline.service.psserver.strategytest;

import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.psserver.BaseStrategy;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 测试策略
 * @author:lhz
 * @date:2018/11/14 17:34
 */
@Service
public class TestStrategy implements BaseStrategy<ChatMsg> {
    private static final Logger logger = LoggerFactory.getLogger(TestStrategy.class);

    @Override
    public int command() {
        return 1;
    }

    @Override
    public  Class beanclass(){
        return ChatMsg.class;
    }

    @Override
    public ResponseEntity sevice(ChatMsg chatMsg) {
        logger.info("Test chatMsg {}",chatMsg.toString());
        return new ResponseEntity("200","success","aaaa");
    }
}
