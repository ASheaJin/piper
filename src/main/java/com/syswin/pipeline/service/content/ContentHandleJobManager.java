package com.syswin.pipeline.service.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.enums.BodyTypeEnums;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.service.content.entity.MediaContentEntity;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.sub.api.ContentOutService;
import com.syswin.sub.api.utils.BeanConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 处理消息内容的类
 * 从content字段解析，获取zip地址，下载zip解压并把文件放到指定地址
 * Created by 115477 on 2019/4/3.
 */
@Component
public class ContentHandleJobManager {
    private final static Logger logger = LoggerFactory.getLogger(ContentHandleJobManager.class);

    private static final int INTRO_MAX_LENGTH = 100;

    private static ExecutorService servicePool = Executors.newFixedThreadPool(10);


    @Autowired
    private FileManager fileManager;

    @Autowired
    private ContentOutService contentOutService;


    /**
     * 对外方法
     * @param publisherId
     * @param contentId
     * @param bodyType
     * @param content
     * @param createTime
     */
    public void addJob(String publisherId, String contentId, Integer bodyType, String content, int createTime) {
        servicePool.execute(() -> {
            ContentEntity contentEntity = parseContent(publisherId, contentId, bodyType, content);
            if (contentEntity == null) {
                return;
            }
//            contentEntity.setPublishTime(createTime);
            ContentEntity listContent = parseListContent(contentEntity);

            contentOutService.add(contentId, publisherId,
                    JacksonJsonUtil.toJson(listContent), JacksonJsonUtil.toJson(contentEntity), createTime);
        });
    }

    /**
     * 解析成列表内容
     * @return
     */
    public ContentEntity parseListContent(ContentEntity contentEntity) {
        if (contentEntity == null) {
            return null;
        }
        ContentEntity listContent = BeanConvertUtil.map(contentEntity, ContentEntity.class);
        listContent.setTitle(limitIntro(listContent.getTitle(), 20));
        if (BodyTypeEnums.TEXT.getType().equals(listContent.getBodyType()) ) {
            listContent.setText(limitIntro(listContent.getText()));
        }

        if (BodyTypeEnums.MAIL.getType().equals(listContent.getBodyType()) ) {
            //暂不处理
        }

        if (BodyTypeEnums.COMPOSE.getType().equals(listContent.getBodyType()) ) {
            listContent.setContentArray(null);
            List<MediaContentEntity> contentArray = contentEntity.getContentArray();

            String intro = null;
            String url = null;
            Integer mediaBodyType = null;
            for (MediaContentEntity media : contentArray) {
                if (BodyTypeEnums.TEXT.getType().equals(media.getBodyType()) && StringUtils.isEmpty(intro)) {
                    intro = media.getText();
                    listContent.setIntro(limitIntro(intro));
                    continue;
                }
                if ((BodyTypeEnums.VOICE.getType().equals(media.getBodyType())
                    || BodyTypeEnums.PIC.getType().equals(media.getBodyType())
                    || BodyTypeEnums.VIDEO.getType().equals(media.getBodyType())
                    )  && StringUtils.isEmpty(url)) {
                    url = media.getUrl();
                    mediaBodyType = media.getBodyType();
                    listContent.setUrl(url);
                    listContent.setThumbnail(media.getThumbnail());
                    listContent.setMediaBodyType(mediaBodyType);
                    continue;
                }
            }
        }
        return listContent;
    }

    private String limitIntro(String intro) {
        return limitIntro(intro, INTRO_MAX_LENGTH);
    }

    private String limitIntro(String intro, int limitLength) {
        if (StringUtils.isEmpty(intro)) {
            return null;
        }

        //截取摘要
        if (limitLength >= intro.length()) {
            return intro;
        } else {
            String introText = intro.substring(0, limitLength);
            int byteLen = introText.getBytes(StandardCharsets.UTF_8).length;
            //防止全英文字符 不够长
            if (byteLen < (limitLength + limitLength / 4)) {
                introText = intro.substring(0, limitLength + limitLength / 2);
            } else if (byteLen < (limitLength + limitLength / 2)) {
                introText = intro.substring(0, limitLength +limitLength / 4);
            }
            return introText;
        }
    }

    /**
     * 解析成详情内容
     * @return
     */
    protected ContentEntity parseContent(String publisherId, String contentId, Integer bodyType, String content) {
        JSONObject jsonObject = JSON.parseObject(content);
        bodyType = guessBodyType(bodyType, jsonObject);
        if (BodyTypeEnums.CARD.getType().equals(bodyType) ||
                BodyTypeEnums.MAP.getType().equals(bodyType)||
                BodyTypeEnums.SHARE.getType().equals(bodyType)||
                BodyTypeEnums.SYSTEM.getType().equals(bodyType)||
                BodyTypeEnums.MAIL.getType().equals(bodyType)||
                BodyTypeEnums.OP.getType().equals(bodyType)) {
            //不支持以上类型
            return null;
        }


        ContentEntity allContent = JSON.parseObject(content, ContentEntity.class);
        allContent.setContentId(contentId);
        allContent.setBodyType(bodyType);

        String url = allContent.getUrl();
        String pwd = allContent.getPwd();
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(pwd)) {
            FileManager.DownloadResult downloadResult = download(url, pwd, publisherId, contentId);
            if (downloadResult == null) {
                return null;
            }
            allContent.setUrl(downloadResult.getDownloadFile());
//            allContent.setFilePath(downloadResult.getStoreFile());
            convertMedia(allContent, downloadResult.getStoreFile());
        }

        if (BodyTypeEnums.MAIL.getType().equals(bodyType)) {
            //解析eml文件 暂不处理


        } else  if (BodyTypeEnums.COMPOSE.getType().equals(bodyType)) {
            allContent.setContentArray(new ArrayList<>());

            JSONArray contentArray = jsonObject.getJSONArray("dynamicContentArray");

            int contentCount = 0;
            for (Iterator<Object> ite = contentArray.iterator(); ite.hasNext();) {
                JSONObject dynamicContentJson = (JSONObject)ite.next();
                int aBodyType = dynamicContentJson.getInteger("bodyType");
                MediaContentEntity subContent = dynamicContentJson.getJSONObject("bodyContent").toJavaObject(MediaContentEntity.class);
                subContent.setBodyType(aBodyType);

                String subUrl = subContent.getUrl();
                String subPwd = subContent.getPwd();
                if (!StringUtils.isEmpty(subUrl) && !StringUtils.isEmpty(subPwd)) {
                    FileManager.DownloadResult downloadResult = download(subUrl, subPwd, publisherId, contentId + "-" + contentCount);
                    if (downloadResult == null) {
                        continue;
                    }
                    subContent.setUrl(downloadResult.getDownloadFile());
                    convertMedia(subContent, downloadResult.getStoreFile());
                }
                subContent.setPwd(null);
                allContent.getContentArray().add(subContent);
                contentCount++;
            }
        }
        allContent.setPwd(null);
        return allContent;
    }

    protected void parseEml(ContentEntity allContent) {

    }

    private FileManager.DownloadResult download(String url, String pwd , String relativePath, String fileName) {
        if (false) {
            return new FileManager.DownloadResult(null, null);
        }

        FileManager.DownloadResult file = fileManager.download(url, pwd, relativePath, fileName);

        return file;
    }

    /**
     * 根据content猜测bodyType，从字段是否存在和字段内容
     * @param bodyType
     * @param jsonObject
     * @return
     */
    public Integer guessBodyType(Integer bodyType, JSONObject jsonObject) {
        if (bodyType != null && !bodyType.equals(0)) {
            return bodyType;
        }

        if (jsonObject.get("text") != null) {
            return BodyTypeEnums.TEXT.getType();
        } else if ("application/eml".equals(jsonObject.get("format"))){
            return BodyTypeEnums.MAIL.getType();
        } else if (jsonObject.get("dynamicContentArray") != null){
            return BodyTypeEnums.COMPOSE.getType();
        } else if ("singleVideoAudio".equals(jsonObject.get("type"))) {
            return BodyTypeEnums.OP.getType();
        } else if (jsonObject.get("lat") != null){
            return BodyTypeEnums.MAP.getType();
        } else if (jsonObject.get("feedId") != null){
            return BodyTypeEnums.CARD.getType();
        } else if (jsonObject.get("desc") != null){
            return BodyTypeEnums.FILE.getType();
        } else if (jsonObject.get("time") != null && jsonObject.get("url") != null && jsonObject.get("w") == null){
            return BodyTypeEnums.VOICE.getType();
        } else if (".mp4".equals(jsonObject.get("suffix")) ||
                (jsonObject.get("time") != null && jsonObject.get("url") != null && jsonObject.get("w") != null)){
            return BodyTypeEnums.VIDEO.getType();
        } else if (".png".equals(jsonObject.get("suffix")) || ".jpeg".equals(jsonObject.get("suffix"))  || ".jpg".equals(jsonObject.get("suffix")) ||
                (jsonObject.get("time") == null && jsonObject.get("url") != null && jsonObject.get("w") != null)){
            return BodyTypeEnums.PIC.getType();
        }

        return null;
    }

    /**
     * 处理媒体相关的内容：
     * 1 amr转成mp3
     * 2 mp4文件获取首帧
     *
     * @param content
     */
    protected void convertMedia(MediaContentEntity content, String filePath) {
        if (content == null) {
            return;
        }
        Integer bodyType = content.getBodyType();

        if (BodyTypeEnums.VOICE.getType().equals(bodyType)) {
            //amr to mp3
            try {
                String format = "mp3";
                String mp3 = MediaUtil.amrToMp3(filePath, format);
                content.setUrl(MediaUtil.targetPath(content.getUrl(), format));
            } catch (Exception e) {
                logger.error("mp3转换出错 " + filePath, e);
            }
        } else if (BodyTypeEnums.VIDEO.getType().equals(bodyType)) {
            //mp4文件获取首帧
            try {
                String format = "png";
                MediaUtil.thumbnail(filePath, format);
                content.setThumbnail(MediaUtil.targetPath(content.getUrl(), format));
            } catch (Exception e) {
                logger.error("mp4获取首帧出错 " + filePath, e);
            }
        }
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
