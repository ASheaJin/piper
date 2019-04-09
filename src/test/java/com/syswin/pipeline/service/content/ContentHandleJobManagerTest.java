package com.syswin.pipeline.service.content;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 115477 on 2019/4/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentHandleJobManagerTest {

    @Autowired
    private ContentHandleJobManager contentHandleJobManager;
//    {
//
//        contentHandleJobManager = new ContentHandleJobManager();
//        FileManager fileManager = new FileManager();
//        fileManager.setDownloadUrlPath("http://localhost/d");
//        fileManager.setFileStoragePath("d:\\temp\\file");
//        fileManager.setTempFilePath("d:\\temp\\temp");
//
//        contentHandleJobManager.setFileManager(fileManager);
//    }

    @Test
    public void addData() {
        System.out.println("------------------------>");
        List<String[]> contents = loadData();

        for (String[] c : contents) {
            String contentId = c[0];
            String createTime = c[1];
            String publisherId = c[2];
            String bodyType = c[4];
            String content = c[5];
            Integer bodyTypeInt = !StringUtils.isEmpty(bodyType) ? Integer.parseInt(bodyType) : null;
//
            contentHandleJobManager.addJob(publisherId, contentId, bodyTypeInt, content, Integer.parseInt(createTime));
        }

        while(true) {
            try {
                Thread.sleep(10000);
                System.out.println("...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    List<String[]> loadData() {
        String csvFile = "d:\\temp\\content_34890954153918464-1.csv";
        String charset = "UTF-8";
        CSVReader reader = null;
        try (
                InputStream fileInputStream = new FileInputStream(new File(csvFile));
        ) {
            //处理读取中的反斜线
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new InputStreamReader(fileInputStream, charset)).withCSVParser(rfc4180Parser);

            reader = csvReaderBuilder.build();

            List<String[]> list = reader.readAll();
            if (reader != null) {
                reader.close();
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Test
    public void testParseEml() {

        String file = "D:\\temp\\temp\\34890954153918464\\35165511403700224-1\\7530119032500579928_2019.03.25 145555_12965b_weihongyi@syswin.com.eml";
        ContentEntity allContent = new ContentEntity();
        contentHandleJobManager.parseEml(allContent);

        System.out.println(allContent);
    }

}

