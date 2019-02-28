package com.syswin.pipeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by 115477 on 2019/1/9.
 */
@Service
public class PiperContentService {

    @Autowired
    private com.syswin.sub.api.ContentService subContentService;

    public Map<String, Object> listByExample(int pageIndex, int pageSize, String publisherId) {

        return subContentService.list(pageIndex, pageSize, publisherId);
    }
}
