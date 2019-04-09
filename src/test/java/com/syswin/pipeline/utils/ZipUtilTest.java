package com.syswin.pipeline.utils;

import net.lingala.zip4j.exception.ZipException;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import static org.junit.Assert.*;

/**
 * Created by 115477 on 2019/4/8.
 */
public class ZipUtilTest {

    @Test
    public void unZip() throws ZipException {
        String zipFile = "D:\\temp\\temp\\35051784212578304\\35164553206300672";
        String unZipFilePath = "D:\\temp\\zip\\35051784212578304\\35164553206300672-1";
        String pwd = "C4367A2A-7AE7-4B07-9C36-EAC3BF88E5B8";
            ZipUtil.unZip(zipFile, unZipFilePath, pwd);

        zipFile = "D:\\temp\\temp\\35051784212578304\\35077141013200896";
        unZipFilePath = "D:\\temp\\zip\\35051784212578304\\35077141013200896-1";
        pwd = "ADDEA84C-FE7D-4B3F-99BA-CFA48B85345E";
            ZipUtil.unZip(zipFile, unZipFilePath, pwd);


        zipFile = "D:\\temp\\temp\\35048086303145984\\35049426938232832";
        unZipFilePath = "D:\\temp\\zip\\35048086303145984\\35049426938232832-1";
        pwd = "47CF2E98-EEC4-4283-A34B-B426C09D5F45";
        ZipUtil.unZip(zipFile, unZipFilePath, pwd);
    }



    @Test
    public void unZipCharset() throws ZipException {
        String zipFile = "D:\\temp\\temp\\/35004715488247808\\35189586670387200-0";
        String unZipFilePath = "D:\\temp\\zip\\35004715488247808\\35189586670387200-0-1";
        String pwd = "5666afde-72a1-4de2-8510-60fc5154b19b";
        ZipUtil.unZip(zipFile, unZipFilePath, pwd);

    }

    @Test
    public void unZipCharset1() throws ZipException {
        String zipFile = "D:\\temp\\temp\\/35048086303145984\\35049426938232832";
        String unZipFilePath = "D:\\temp\\zip\\35048086303145984\\35049426938232832-1";
        String pwd = "47CF2E98-EEC4-4283-A34B-B426C09D5F45";
        ZipUtil.unZipWithCharset(zipFile, unZipFilePath, pwd, "UTF-8");

    }



    @Test
    public void unzipEml() throws Exception {

        String zipFile = "d:\\temp\\temp\\34890954153918464\\35165511403700224";
        String unZipFilePath = "d:\\temp\\temp\\34890954153918464\\35165511403700224-1";
        String pwd = "1553496978606";
        ZipUtil.unZip(zipFile, unZipFilePath, pwd);
    }
}