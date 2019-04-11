package com.syswin.pipeline.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * /d 下载文件服务
 * Created by 115477 on 2019/4/8.
 */
@Configuration
public class FileServerConfig implements WebMvcConfigurer {


    @Value("${file.store.path}")
    private String fileStoragePath;

    @Value("${file.download.path}")
    private String downloadUrlPath;

    @Value("${ffmpeg.home}")
    private String ffmpegHome;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(downloadUrlPath + "/**").addResourceLocations("file:" + fileStoragePath + "/");

        //设置ffmpeg的目录，需要事先拷贝程序文件到此目录
        System.setProperty("ffmpeg.home", ffmpegHome);
    }
}
