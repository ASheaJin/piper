package com.syswin.pipeline.service.content;

import com.syswin.sub.api.ContentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by 115477 on 2019/4/3.
 */

public class FileManagerTest {

    @Autowired
    private FileManager fileManager;


    @Test
    public void download() {
        FileManager fileManager = new FileManager();
        fileManager.setDownloadUrlPath("/d");
        fileManager.setFileStoragePath("/opt/file/file");
        fileManager.setTempFilePath("/opt/file/temp");

        Object f = fileManager.download("https://ucloud-file.t.email/%2F3565f189c8644dfa8c2d2a2b25793211.zip",
                "54b432aa-9327-4fe5-a590-0f574940fc27", "aaa", "35168368983801856-1");

        System.out.println(f);
    }
}