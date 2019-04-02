package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.BrowseRecord;
import com.syswin.pipeline.db.repository.BrowseRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 115477 on 2018/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BrowseRecordServiceTest {

    @Autowired
    private BrowseRecordRepository browseRecordRepository;

    @Test
    public void insert() {
        BrowseRecord browseRecord = new  BrowseRecord();
        browseRecord.setContentId(111l+"");
        browseRecord.setBrowseId(1l+"");
        browseRecord.setUserId("1111@temail.com");
        browseRecord.setBrowseTime((int)(System.currentTimeMillis()/1000));
        browseRecordRepository.insert(browseRecord);
    }

    @Test
    public void update() {
        BrowseRecord browseRecord = new  BrowseRecord();
        browseRecord.setContentId(111l+"");
        browseRecord.setBrowseId(1l+"");
        browseRecord.setUserId("1111@temail.com");
        browseRecord.setBrowseTime((int)(System.currentTimeMillis()/1000));
        browseRecordRepository.update(browseRecord);
    }

    @Test
    public void selectbyId() {

//        System.out.println("browseRecord :" + browseRecordRepository.selectById(1l));
    }

    @Test
    public void select() {

        System.out.println("browseRecordList :" + browseRecordRepository.select().toString());
    }
}