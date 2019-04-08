package com.syswin.pipeline.service.content;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.utils.JacksonJsonUtil;
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
public class ContentHandleTest {

    private  ContentHandleJobManager contentHandleJobManager;
    {

        contentHandleJobManager = new ContentHandleJobManager();
        FileManager fileManager = new FileManager();
        fileManager.setDownloadUrlPath("/d");
        fileManager.setFileStoragePath("/opt/file/file");
        fileManager.setTempFilePath("/opt/file/temp");

        contentHandleJobManager.setFileManager(fileManager);
    }
    @Test
    public void content() {
        System.out.println("------------------------>");
        List<String[]> contents = loadData();

        for (String[] c : contents) {
            String contentId = c[0];
            String publisherId = c[2];
            String bodyType = c[4];
            String content = c[5];
            Integer bodyTypeInt = !StringUtils.isEmpty(bodyType) ? Integer.parseInt(bodyType) : null;

            ContentEntity contentEntity = contentHandleJobManager.parseContent(publisherId, contentId, bodyTypeInt, content);
            System.out.println(JacksonJsonUtil.toJson(contentEntity));
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
    public void listContent() {
        List<String[]> contents = loadData();

        for (String[] c : contents) {
            String contentId = c[0];
            String publisherId = c[2];
            String bodyType = c[4];
            String content = c[5];
            Integer bodyTypeInt = !StringUtils.isEmpty(bodyType) ? Integer.parseInt(bodyType) : null;

            ContentEntity contentEntity = contentHandleJobManager.parseContent(publisherId, contentId, bodyTypeInt, content);
            ContentEntity listContent = contentHandleJobManager.parseListContent(contentEntity);
            System.out.println(JacksonJsonUtil.toJson(listContent));
        }

    }
}