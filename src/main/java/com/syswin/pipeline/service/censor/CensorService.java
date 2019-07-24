package com.syswin.pipeline.service.censor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.syswin.pipeline.db.model.CensorResult;
import com.syswin.pipeline.db.model.CensorResultExample;
import com.syswin.pipeline.db.repository.CensorResultRepository;
import com.syswin.pipeline.enums.BodyTypeEnums;
import com.syswin.pipeline.service.content.entity.ContentEntity;
import com.syswin.pipeline.service.content.entity.MediaContentEntity;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.showType.BaseShow;
import com.syswin.sub.api.db.model.Content;
import com.syswin.sub.http.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 调用内容审核的服务
 * Created by 115477 on 2019/7/23.
 */
@Service
public class CensorService {
    private final static Logger logger = LoggerFactory.getLogger(CensorService.class);

    @Value("${url.censor}")
    private String censorUrl;

    @Autowired
    private com.syswin.sub.api.ContentService subContentService;

    @Autowired
    private CensorResultRepository censorResultRepository;

    public void sendContentCensor(String contentId) {
        List<Content> contents = subContentService.getContentsByCids(Lists.newArrayList(contentId));
        if (!contents.isEmpty()) {
            this.sendContentCensor(contents.get(0));
        }
    }

    @Async("censorThreadPool")
    public void sendContentCensor(Content content) {
        StringBuffer censorTxt = new StringBuffer();
        Integer bodyType = content.getBodyType();

        try {
            ContentEntity allContent = JSON.parseObject(content.getContent(), ContentEntity.class);
            if (BodyTypeEnums.TEXT.getType().equals(bodyType)) {
                censorTxt.append(allContent.getText());
            } else if (BodyTypeEnums.COMPOSE.getType().equals(bodyType)) {
                if (!StringUtils.isEmpty(content.getTitle())) {
                    censorTxt.append(content.getTitle());
                }

                JSONObject jsonObject = JSON.parseObject(content.getContent());
                JSONArray contentArray = jsonObject.getJSONArray("dynamicContentArray");


                for (Iterator<Object> ite = contentArray.iterator(); ite.hasNext(); ) {
                    JSONObject dynamicContentJson = (JSONObject) ite.next();
                    int aBodyType = dynamicContentJson.getInteger("bodyType");
                    MediaContentEntity subContent = dynamicContentJson.getJSONObject("bodyContent").toJavaObject(MediaContentEntity.class);
                    if (BodyTypeEnums.TEXT.getType().equals(aBodyType)) {
                        censorTxt.append(subContent.getText());
                    }
                }
            } else if (BodyTypeEnums.COMPLEX.getType().equals(bodyType)) {
                BaseShow sendShow = JSON.parseObject(content.getContent(), BaseShow.class);

                if (!sendShow.getActions().isEmpty()) {
                    ActionItem actionItem = sendShow.getActions().get(0);
                    censorTxt.append(actionItem.getTitle());
                }
                if (!sendShow.getShowContent().isEmpty()) {

                    Map<String, Object> map = sendShow.getShowContent();
                    if (!StringUtil.isEmpty(map.get("title"))) {
                        censorTxt.append(map.get("title"));
                    }

                    if (!StringUtil.isEmpty(map.get("text"))) {
                        censorTxt.append(map.get("text"));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("内容送审核解析文本时发生异常：{}", content);
            logger.error(e.getMessage(), e);
            return;
        }

        if (censorTxt.length() > 0) {
            sendCensor(content.getContentId(), CensorType.Content, censorTxt.toString());
        }
    }

    @Async("censorThreadPool")
    public void sendCensor(String objId, CensorType type, String content) {
        //判断重复
        CensorResultExample example = new CensorResultExample();
        CensorResultExample.Criteria criteria = example.createCriteria();
        criteria.andObjIdEqualTo(objId).andTypeEqualTo(type.getCode());
        long count = censorResultRepository.countByExample(example);
        if (count > 0) {
            return;
        }

        CensorRequest req = new CensorRequest();
        try {
            long start = System.currentTimeMillis();
            req.setId(objId);
            req.setTexts(new String[] {content});
            String respString = HttpUtils.syncPostString(censorUrl, null, JSON.toJSONString(req));
            CensorResponse resp = JSON.parseObject(respString, CensorResponse.class);
            CensorResponse.CensorResponseResult responseResult = resp.getResult().get(0);
            long cost =  System.currentTimeMillis() - start;

            CensorResult po = new CensorResult();
            po.setObjId(objId);
            po.setType(type.getCode());
            po.setLabel(responseResult.getL());
            po.setScore(new BigDecimal(responseResult.getS()));
            po.setCreateTime(start);
            po.setTimeCost(Long.valueOf(cost).intValue());
            censorResultRepository.insert(po);
        } catch (Exception e) {
            logger.error("内容送审核时发生异常：{}", req);
            logger.error(e.getMessage(), e);
        }
    }
}
