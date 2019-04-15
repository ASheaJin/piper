package com.syswin.pipeline.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.sub.api.ContentService;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Content;
import com.syswin.sub.api.db.model.ContentOut;
import com.syswin.sub.api.db.model.Publisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2019/4/15.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentOutServiceTest {


    @Autowired
    private PublisherService publisherService;
    @Autowired
    private com.syswin.sub.api.ContentOutService contentOutService;

    @Test
    public void getAllBodyType() {
        List<Publisher> publishers = publisherService.list(1, 10000, null, null, null);
        for (Publisher publisher : publishers) {
            PageInfo contentOutPageInfo = contentOutService.listByPublisherId(publisher.getPublisherId(), 1, 10000);
            List<ContentOut> contents = contentOutPageInfo.getList();
            contents.stream().forEach((c) -> {
                ContentEntity entity = JacksonJsonUtil.fromJson(c.getListdesc(), ContentEntity.class);
                System.out.println("update  subengine.content set body_type=" + entity.getBodyType() + " where content_id ="+ entity.getContentId() + ";");
            });
        }

    }

}
