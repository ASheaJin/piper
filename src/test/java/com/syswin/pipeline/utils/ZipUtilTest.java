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
        String unZipFilePath = "D:\\temp\\temp\\35051784212578304\\35164553206300672-1";
        String pwd = "C4367A2A-7AE7-4B07-9C36-EAC3BF88E5B8";
            ZipUtil.unZip(zipFile, unZipFilePath, pwd);

        zipFile = "D:\\temp\\temp\\35051784212578304\\35077141013200896";
        unZipFilePath = "D:\\temp\\temp\\35051784212578304\\35077141013200896-1";
        pwd = "ADDEA84C-FE7D-4B3F-99BA-CFA48B85345E";
            ZipUtil.unZip(zipFile, unZipFilePath, pwd);
    }

    @Test
    public void unzipEml() throws Exception {

        String zipFile = "d:\\temp\\temp\\34890954153918464\\35165511403700224";
        String unZipFilePath = "d:\\temp\\temp\\34890954153918464\\35165511403700224-1";
        String pwd = "1553496978606";
        ZipUtil.unZip(zipFile, unZipFilePath, pwd);
    }
}