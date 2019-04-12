package com.syswin.pipeline.service.content;

import com.syswin.pipeline.utils.ZipUtil;
import lombok.Data;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 负责下载和另存文件
 * Created by 115477 on 2019/4/3.
 */
@Component
public class FileManager {
    private final static Logger logger = LoggerFactory.getLogger(FileManager.class);

    private static final  String SEPARATOR = "/";

    @Value("${url.piper}")
    private String piperUrl;

    @Value("${file.temp.path}")
    private String tempFilePath ;

    @Value("${file.store.path}")
    private String fileStoragePath;

    @Value("${file.download.path}")
    private String downloadUrlPath;

    public DownloadResult download(String url, String pwd , String relativePath, String fileName) {

        //下载zip到临时目录
        String tempPath = tempFilePath + File.separator + relativePath + File.separator + fileName;
        File zipTempFile = new File(tempPath);

        if (!zipTempFile.exists()) {
            try {
                //connectionTimeout, readTimeout = 10 seconds
                FileUtils.copyURLToFile(new URL(url), new File(tempPath), 10000, 10000);

            } catch (IOException e) {
                logger.error("下载zip出错 url=" + url, e);
                return null;
            }
        }
        //解压zip到指定目录
        String storePath = fileStoragePath + File.separator + relativePath + File.separator + fileName;
        try {
            ZipUtil.unZip(tempPath, storePath, pwd);
        } catch (ZipException e) {
            logger.error("解压zip出错 zip文件 zip=" + tempPath, e);
            return null;
        }
        //删除zip
//        try {
//            FileUtils.forceDelete(new File(tempPath));
//        } catch (IOException e) {
//            logger.error("删除zip出错", e);
//        }

        //返回文件的相对路径
        File storePathDirectory = new File(storePath);
        File[] f = storePathDirectory.listFiles();
//        return f.length > 0 ? SEPARATOR + relativePath + SEPARATOR + fileName + f[0].getName() : "";
        if (f.length == 0) {
            return null;
        }

        String downloadFile = piperUrl + downloadUrlPath + SEPARATOR + relativePath + SEPARATOR + fileName + SEPARATOR + f[0].getName();
        String storeFile = fileStoragePath + File.separator + relativePath + File.separator + fileName + File.separator + f[0].getName();
        return new DownloadResult(downloadFile, storeFile);
    }

    public String getDownloadUrlPath() {
        return downloadUrlPath;
    }

    public void setDownloadUrlPath(String downloadUrlPath) {
        this.downloadUrlPath = downloadUrlPath;
    }

    public String getFileStoragePath() {
        return fileStoragePath;
    }

    public void setFileStoragePath(String fileStoragePath) {
        this.fileStoragePath = fileStoragePath;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }


    @Data
    static class DownloadResult {
        private String downloadFile;
        private String storeFile;

        public DownloadResult(String downloadFile, String storeFile) {
            this.downloadFile = downloadFile;
            this.storeFile = storeFile;
        }
    }
}
