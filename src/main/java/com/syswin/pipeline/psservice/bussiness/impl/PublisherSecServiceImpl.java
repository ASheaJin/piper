package com.syswin.pipeline.psservice.bussiness.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.enums.BodyTypeEnums;
import com.syswin.pipeline.enums.PeriodEnums;
import com.syswin.pipeline.enums.ShowTypeEnums;
import com.syswin.pipeline.psservice.SendMessegeService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.psservice.bussiness.PublisherSecService;
import com.syswin.pipeline.service.content.ContentHandleJobManager;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.service.content.entity.MediaContentEntity;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.JacksonJsonUtil;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.pipeline.utils.SwithUtil;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.showType.BaseShow;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.sub.api.AdminService;
import com.syswin.sub.api.ContentOutService;
import com.syswin.sub.api.SendRecordService;
import com.syswin.sub.api.SubscriptionService;
import com.syswin.sub.api.db.model.*;
import com.syswin.sub.api.enums.PublisherTypeEnums;
import com.syswin.sub.api.utils.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author:lhz
 * @date:2018/12/27 9:23
 */
@Service
@EnableAsync
public class PublisherSecServiceImpl implements PublisherSecService {


    @Value("${app.ps-app-sdk.user-id}")
    private String from;

    @Value("${url.piper}")
    private String urlPiper;

    @Value("${path.content.detail}")
    private String pathDetail;

    @Lazy
    @Autowired
    SendMessegeService sendMessegeService;

    @Autowired
    AdminService adminService;

    @Autowired
    private com.syswin.sub.api.LogService subLogService;

    @Autowired
    private com.syswin.sub.api.ContentService subContentService;

    @Autowired
    private com.syswin.sub.api.PublisherService subPublisherService;

    @Autowired
    private PiperSubscriptionService piperSubscriptionService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SendRecordService subSendRecordService;

    @Autowired
    private ContentHandleJobManager contentHandleJobManager;

    @Autowired
    private LanguageChange languageChange;

    @Autowired
    private ContentOutService contentOutService;

    private final static Logger logger = LoggerFactory.getLogger(PublisherSecServiceImpl.class);
    //过滤能用作发的
    Set<Integer> filterset = new HashSet();
    Set<String> childset = new HashSet();

    public PublisherSecServiceImpl() {
        //http://wiki.syswin.com/pages/viewpage.action?pageId=33689922
        filterset.add(2);//语音
        filterset.add(3);//图片
        filterset.add(10);//视频
        filterset.add(12);//GIF
        filterset.add(14);//文件
        filterset.add(15);//分享
        filterset.add(22);//邮件eml
        filterset.add(23);//命令操作类
        filterset.add(30);//复合消息体
        filterset.add(801);//复杂结构

        childset.add(String.valueOf(PeriodEnums.MenuList.HELP));
        childset.add(String.valueOf(PeriodEnums.MenuList.CREATPUBLISH));
        childset.add(String.valueOf(PeriodEnums.MenuList.ORDERPUBLISH));
        childset.add(String.valueOf(PeriodEnums.MenuList.UNORDERPUBLISH));
        childset.add(String.valueOf(PeriodEnums.MenuList.PUBLISHARTICLE));
    }

    /**
     * insert log
     *
     * @param userId
     * @param bodyType
     * @param text
     */
    private void insertLog(String userId, String to, int bodyType, String text) {
        Log log = new Log();
        log.setBodyType(bodyType);
        log.setText(text);
        log.setToemail(to);
        log.setUserId(userId);

        subLogService.addlog(log);
    }

    @Override
    public Integer dealPushArticle(Publisher publisher, int bodyType, Object show, PublisherTypeEnums publisherTypeEnums) {
        return dealPushArticle(publisher, bodyType, show, null, publisherTypeEnums);
    }

    /**
     * 推送文章
     *
     * @param publisher
     * @param bodyType
     * @param show
     * @param publisherTypeEnums
     */
    @Override
    public Integer dealPushArticle(Publisher publisher, int bodyType, Object show, ContentEntity saveShow, PublisherTypeEnums publisherTypeEnums) {
        if (StringUtil.isEmpty(show)) {
            throw new BusinessException("消息不能为空");
        }

        //生成文章Id
        String contentId = String.valueOf(SnowflakeIdWorker.getInstance().nextId());
        Content content = new Content();
        content.setStatus(1);
        content.setContentId(contentId);

        /*
         * 20190620 新增逻辑：所有文章按801发送，
         * 当 bodyType <> 801 时，将show内容转为BaseShow格式
         */
        //实际发送消息的bodyType
        int sendBodyType = bodyType;
        BaseShow sendShow = null;
        try {
            //个人出版社全转成801
            if (publisher.getPtype().getCode().equals(PublisherTypeEnums.person.getCode())) {
                sendBodyType = BodyTypeEnums.COMPLEX.getType();
                /*
                 *	当 bodyType = 801 时show对象类型是BaseShow， saveShow对象不为空
                 *	否则为json字符，（格式是秘邮消息 参见http://wiki.syswin.com/pages/viewpage.action?pageId=33689922）
                 *	saveShow对象为空
                 */
                if (BodyTypeEnums.COMPLEX.getType().equals(bodyType) && saveShow != null) {
                    sendShow = (BaseShow) show;
                    if (!checkShow(sendShow, bodyType, publisher.getPtemail(), publisher.getUserId())) {
                        return 0;
                    }

                    content.setContent(JSONObject.toJSONString(sendShow));//保存BaseShow的内容
                    saveShow.setContentId(contentId);
                    //contentout内容处理
                    contentHandleJobManager.addJobSaveText(contentId, saveShow, content.getCreateTime());
                } else if (!BodyTypeEnums.COMPLEX.getType().equals(bodyType)) {
                    if (!checkContentJson(show.toString(), bodyType, publisher.getPtemail(), publisher.getUserId())) {
                        return 0;
                    }
                    //先生成contentout
                    //contentout内容处理 传的是原json内容
                    contentHandleJobManager.addJob(publisher.getPublisherId(), contentId, bodyType, show.toString(), content.getCreateTime());

                    //将contentout转为BaseShow格式
                    ContentOut contentOut = contentOutService.getContentOutById(contentId);
                    if (contentOut == null) {
                        //如果contentOut为空，由contentHandleJobManager里的逻辑决定，表示转换失败
                        return 0;
                    }
                    sendShow = convertFromContentOut(contentOut, contentId, publisher.getPublisherId(), publisher.getUserId());
                    content.setContent(JSONObject.toJSONString(sendShow));
                }
                //组织出版社不转格式，按原来逻辑处理
            } else if (publisher.getPtype().getCode().equals(PublisherTypeEnums.organize.getCode())) {
                content.setContent(show.toString());
                //contentout内容处理
                contentHandleJobManager.addJob(publisher.getPublisherId(), contentId, bodyType, content.getContent(), content.getCreateTime());
            }
            content.setBodyType(sendBodyType);
            content.setPublisherId(publisher.getPublisherId());
            subContentService.addContent(content);
        } catch (Exception e) {
            logger.error(content + " 添加失败", e);
        }

        //2、获取订阅该用户的读者列表
        List<String> userIds = subscriptionService.getSubscribers(publisher.getPtemail(), publisherTypeEnums);
        //因为个人出版社的消息转为801了，把作者也加入收件人，能看到转化后的消息
        if (publisher.getPtype().getCode().equals(PublisherTypeEnums.person.getCode()) && !userIds.contains(publisher.getUserId())) {
            userIds.add(0, publisher.getUserId());
        }
        String r = String.format("received：from=%s to=%s cid=%s content=%s", publisher.getUserId(), publisher.getPtemail(), contentId, content);
        logger.info(r);

        int num = 0;
        String msgId = "";
        //3、逐个发文章
        for (String orderUserId : userIds) {
            //  2018/12/17 推送文章
            try {
                String fromTemail = publisher.getPtemail();
                //分别对不同类型的文章进行处理
                if (BodyTypeEnums.TEXT.getType().equals(sendBodyType)) {
                    String cont = JSON.parseObject(show.toString()).getString("text");
                    sendMessegeService.sendTextMessage(cont, orderUserId, fromTemail);

                } else if (BodyTypeEnums.COMPLEX.getType().equals(sendBodyType)) {
                    //需要动态拼接url中的userId
                    if (sendShow.getActions() != null && !sendShow.getActions().isEmpty() && !StringUtils.isEmpty(sendShow.getActions().get(0).getUrl())) {
                        String originUrl = sendShow.getActions().get(0).getUrl();
                        String actionItemUrl = originUrl + (originUrl.indexOf("&") > 0 ? "&" : "?");
                        actionItemUrl += "userId=" + orderUserId;
                        sendShow.getActions().get(0).setUrl(actionItemUrl);
                    }
                    msgId = UUID.randomUUID().toString();
                    PsClientKeeper.newInstance().sendMsg(fromTemail, orderUserId, sendShow);
                } else {
                    msgId = sendMessegeService.sendOtherMessage(show.toString(), sendBodyType, orderUserId, fromTemail);

                }
                String s = String.format("from=%s to=%s cid=%s msgid=%s", fromTemail, orderUserId, contentId, msgId);
                logger.info(s);
                num++;

            } catch (Exception ex) {
                logger.error(publisher.getPtemail() + " send to " + orderUserId + "error----   contentId：" + contentId, ex);
            }

        }
        sendMessegeService.sendTextMessage(num + languageChange.getValueByUserId("msg.hassend", publisher.getUserId()), publisher.getUserId(), 1000, publisher.getPtemail());
        //推送记录
        SendRecord sendRecord = new SendRecord();
        sendRecord.setContentId(contentId);
        sendRecord.setSendnum(num);
        sendRecord.setUserId(publisher.getPtemail());
        subSendRecordService.addSendRecord(sendRecord);
        return num;
    }

    /**
     * 判断收到的show类型消息，
     *
     * @return
     */
    private boolean checkShow(BaseShow show, Integer bodyType, String ptmail, String userId) {
        //检查BaseShow的内容是否正确，由于比较麻烦，不判断了。由发送方保证吧
        return true;
    }

    private boolean checkContentJson(String contentJson, Integer bodyType, String ptmail, String userId) {
        boolean valid = true;
        String error = null;
        JSONObject jsonObject = JSON.parseObject(contentJson);
        if (BodyTypeEnums.TEXT.getType().equals(bodyType)) {
            if (StringUtils.isEmpty(jsonObject.getString("text"))) {
                valid = false;
                error = languageChange.getValueByUserId("ex.content.null", userId);
            }
        }
        if (!valid) {
            this.sendUserIdInvalidContent(error, ptmail, userId);
        }

        return valid;
    }

    /**
     * 如果内容合适不合法，给作者发一条提示
     *
     * @param ptmail
     * @param userId
     */
    private void sendUserIdInvalidContent(String error, String ptmail, String userId) {
        sendMessegeService.sendTextMessage(languageChange.getValueByUserId("ex.content.error", userId) + error, userId, ptmail);
    }

    private BaseShow convertFromContentOut(ContentOut s, String contentId, String publisherId, String userId) {
        if (s == null) {
            return null;
        }
        String title = null;
        String imageUrl = null;
        String text = null;

        ContentEntity e = JacksonJsonUtil.fromJson(s.getAllcontent(), ContentEntity.class);
        int bodyType = e.getBodyType();
        if (BodyTypeEnums.TEXT.getType().equals(bodyType)) {
            title = limitIntro(e.getText(), 50);
        }
        if (BodyTypeEnums.VOICE.getType().equals(bodyType)) {
            title = languageChange.getValueByUserId("msg.content.voice", userId);
        }
        if (BodyTypeEnums.PIC.getType().equals(bodyType)) {
            title = languageChange.getValueByUserId("msg.content.pic", userId);
            imageUrl = e.getUrl();
        }
        if (BodyTypeEnums.VIDEO.getType().equals(bodyType)) {
            title = languageChange.getValueByUserId("msg.content.video", userId);
            imageUrl = e.getThumbnail();
        }
        if (BodyTypeEnums.FILE.getType().equals(bodyType)) {
            title = languageChange.getValueByUserId("msg.content.file", userId);
        }
        if (BodyTypeEnums.URL.getType().equals(bodyType)) {
            title = languageChange.getValueByUserId("msg.content.url", userId);
        }

        if (BodyTypeEnums.COMPOSE.getType().equals(bodyType)) {
            List<MediaContentEntity> contentArray = e.getContentArray();
            if (!StringUtils.isEmpty(e.getTitle())) {
                title = limitIntro(e.getTitle(), 50);
            }
            for (MediaContentEntity media : contentArray) {
                if (BodyTypeEnums.TEXT.getType().equals(media.getBodyType()) && StringUtils.isEmpty(title)) {
                    title = limitIntro(media.getText(), 50);
                    continue;
                }
                if (BodyTypeEnums.PIC.getType().equals(media.getBodyType()) && StringUtils.isEmpty(imageUrl)) {
                    imageUrl = media.getUrl();
                    continue;
                }
                if (BodyTypeEnums.VIDEO.getType().equals(media.getBodyType()) && StringUtils.isEmpty(imageUrl)) {
                    imageUrl = media.getThumbnail();
                    continue;
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        if (!StringUtil.isEmpty(title)) {
            map.put("title", title);
        }
        if (!StringUtil.isEmpty(imageUrl)) {
            map.put("imageUrl", imageUrl);
        }
        if (!StringUtil.isEmpty(text)) {
            map.put("text", text);
        }

        String url = urlPiper + String.format(pathDetail, contentId, publisherId);
        List<ActionItem> infoList = Stream.of(new ActionItem(languageChange.getValueByUserId("msg.content.detail", userId), url)
        ).collect(Collectors.toList());

        TextShow show = new TextShow(ShowTypeEnums.COMPLEX.getType(), map, infoList);

        return show;
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
                introText = intro.substring(0, limitLength + limitLength / 4);
            }
            return introText;
        }
    }

    //个人出版社订阅
    public void monitorP(String userId, String ptemail, int bodyType, Object show) {

//		Publisher publisher = subPublisherService.getPubLisherByuserId(userId, PublisherTypeEnums.person);
        // 为测试二用
//		Publisher publisher = publisherRepository.selectByPtemail(ptemail);
//		ptemail = publisher.getPtemail();
        Publisher publisher = subPublisherService.getPubLisherByPublishTmail(ptemail, PublisherTypeEnums.person);
        if (publisher == null) {
            return;
        }

        //如果用户输入的是文本

//TODO 订阅转移自动订阅了

//		Subscription subscription = subscriptionService.getSub(userId, publisher.getPublisherId());
//		if (subscription == null && !publisher.getUserId().equals(userId)) {
//			if (bodyType == 1) {
//				String txt = StringUtils.filterStr(show.toString());
//				if ("subscribe".equals(txt) || "订阅".equals(txt) || "1".equals(txt)) {
//					piperSubscriptionService.subscribe(userId, ptemail, publisher.getPtype());
//					return;
//				} else {
//					sendMessegeService.sendTextMessage(languageChange.getValueByUserId("msg.ordertip", userId), userId, 0, publisher.getPtemail());
//				}
//			}
//		}
        //判断出版社是否存在
        if (userId.equals(publisher.getUserId())) {
//			if (!filterset.contains(body_type)) {
//				sendMessegeService.sendTextMessage(MessageUtil.sendCreateHelpTip("请发送文件、语音、图片、视频"), userId, 1000, ptemail);
//				return;
//			}
            dealPushArticle(publisher, bodyType, show, PublisherTypeEnums.person);
        } else {
            sendMessegeService.sendTextMessage(languageChange.getValueByUserId("msg.noreply", userId), userId, 1000, ptemail);

        }


    }

    //处理组织消息
    @Override
    public void monitor(String userId, String ptemail, int bodyType, Object show) {

        if (StringUtil.isEmpty(show)) {
            throw new BusinessException("消息体不能为空");
        }
        //判断发送格式
        insertLog(userId, ptemail, bodyType, bodyType > 30 ? "非业务指令" : show.toString());

        if (bodyType > 90) {
            return;
        }
        if (ptemail.equals(from)) {
            return;
        }

        Publisher publisher = subPublisherService.getPubLisherByPublishTmail(ptemail, PublisherTypeEnums.organize);

        //判断该出版社是组织出版社还是个人出版社
        if (publisher == null) {
            //个人出版社
            monitorP(userId, ptemail, bodyType, show);
            return;
        }
        //组织出版社
        if (userId.equals(publisher.getUserId())) {
            dealPushArticle(publisher, bodyType, show, PublisherTypeEnums.organize);
        } else {
            sendMessegeService.sendTextMessage(languageChange.getValueByUserId("msg.nopermission", userId), userId, 1000, ptemail);
        }

    }

}
