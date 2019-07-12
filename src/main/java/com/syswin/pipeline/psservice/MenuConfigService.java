package com.syswin.pipeline.psservice;

import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.enums.PermissionEnums;
import com.syswin.pipeline.service.PiperConsumerService;
import com.syswin.pipeline.service.PiperSubscriptionService;
import com.syswin.pipeline.utils.LanguageChange;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.admin.config.IMenuConfigService;
import com.syswin.ps.sdk.common.MsgHeader;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.sub.api.PublisherService;
import com.syswin.sub.api.db.model.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuConfigService implements IMenuConfigService {

    private final static Logger logger = LoggerFactory.getLogger(MenuConfigService.class);


    @Value("${app.ps-app-sdk.user-id}")
    private String apiper;
    @Autowired
    private PiperConsumerService consumerService;
//	private String myRole = PermissionEnums.Reader.name;

    @Autowired
    LanguageChange languageChange;
    @Autowired
    private MessegerSenderService messegerSenderService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private PiperSubscriptionService piperSubscriptionService;

    private static String common = "common";
    private static String person = "person";
    private static String guest = "guest";
    private static String reader = "reader";
    private static String org = "org";

    private static String nomenu = "nomenu";

    public List<String> getKey(String accountNo) {
        MsgHeader msgHeader = PsClientKeeper.msgHeader();
        //根据访问者的权限配置菜单 msgHeader 里面有用户的 信息
        List<String> menus = menu(msgHeader, accountNo);
        logger.debug("获取菜单：sender:{},receive:{},platformInfo:{},menus:{}", msgHeader.getSender(), msgHeader.getReceiver(), msgHeader.getPlatformInfo(), menus.toString());
        return menus;

    }

    private List<String> menu(MsgHeader msgHeader, String accountNo) {
        List<String> menus = null;

        if ((menus = menuA(msgHeader, accountNo)) == null) {
            menus = menuP(msgHeader, accountNo);
        }
        if (menus == null) {
            menus = Arrays.asList(getKey(accountNo, nomenu));
        }
        return menus;
    }

    /**
     * 拉取a.piper的菜单
     *
     * @param header
     * @param accountNo
     * @return
     */
    private List<String> menuA(MsgHeader header, String accountNo) {
        if (!apiper.equals(accountNo)) {
            return null;
        }
        String userId = header.getSender();
        String myRole = consumerService.getAMenuRole(userId);
        //初始时创建
        if (!consumerService.getUserVersion(header.getSender(), header.getReceiver())) {
//			sendMessegeService.sendCard(apiper, header.getSender(), "Piper");
            String pdfInfo = "{\"format\":\"application/pdf\",\"url\":\"https://ucloud-file.t.email/%2Fceca224cce52468dabc22390f2289e97.zip\",\"pwd\":\"EB04F13C-E30B-492E-90FA-E5300139041E\",\"suffix\":\".pdf\",\"desc\":\"Piper操作手册1.1.pdf\",\"size\":255784,\"percent\":100}";
            try {
                messegerSenderService.sendSynchronizationContent(apiper, userId, UUID.randomUUID().toString(), 14, pdfInfo);
            } catch (Exception ex) {
            }
        }
        //保持角色，用于判断是否发pdf引导
        consumerService.getUserVersion(header.getSender(), header.getReceiver(), "1", myRole);
        String lang = header.getPlatformInfo().getLanguage();

        List menus = new ArrayList();

        if (PermissionEnums.OrgPerson.name.equals(myRole)) {
            menus.add(getKey(accountNo, getLang(lang, person)));
            menus.add(getKey(accountNo, getLang(lang, common)));
            menus.add(getKey(accountNo, getLang(lang, org)));


        }
        if (PermissionEnums.OnlyOrg.name.equals(myRole)) {
            menus.add(getKey(accountNo, getLang(lang, guest)));
            menus.add(getKey(accountNo, getLang(lang, common)));
            menus.add(getKey(accountNo, getLang(lang, org)));

        }
        //个人管理者，订阅者
        if (PermissionEnums.Person.name.equals(myRole)) {
            menus.add(getKey(accountNo, getLang(lang, person)));
            menus.add(getKey(accountNo, getLang(lang, common)));
        }
        //游客，订阅者
        if (PermissionEnums.Reader.name.equals(myRole)) {
            menus.add(getKey(accountNo, getLang(lang, guest)));
            menus.add(getKey(accountNo, getLang(lang, common)));
        }

        return menus;
    }


    private List<String> menuP(MsgHeader header, String accountNo) {
        if (apiper.equals(header.getReceiver())) {
            return null;
        }
        try {
            //每次进入进行订阅（只订阅个人出版社）
            piperSubscriptionService.subscribeInner(header.getSender(), header.getReceiver());
        } catch (Exception e) {
            logger.debug(header.getSender() + "订阅" + header.getReceiver() + "失败了 :" + languageChange.getLangByStr(e.getMessage(), ""));
        }
        if (!consumerService.getUserVersion(header.getSender(), header.getReceiver())) {
            Publisher p = publisherService.getPubLisherByPublishTmail(accountNo, null);

//			if (p != null) {
//				if (p.getUserId().equals(header.getSender())) {
//					sendMessegeService.sendCard(header.getReceiver(), header.getSender(), "*" + p.getName());
//				} else {
//					sendMessegeService.sendCard(header.getReceiver(), header.getSender(), p.getName());
//				}
//			}
        }

        String lang = header.getPlatformInfo().getLanguage();
        String userId = header.getSender();
        String myRole = consumerService.getPiperMenuRole(userId, accountNo);
        consumerService.getUserVersion(header.getSender(), header.getReceiver(), "1", myRole);
        List menus = new ArrayList();

        menus.add(getKey(accountNo, getLang(lang, common)));


        //判断是否是组织出版社
        if (PermissionEnums.Reader.name.equals(myRole)) {
            menus.add(getKey(accountNo, getLang(lang, reader)));
        }
        //判断是否是组织出版社
        if (PermissionEnums.Person.name.equals(myRole)) {
            menus.add(getKey(accountNo, getLang(lang, person)));
        }

        if (PermissionEnums.OnlyOrg.name.equals(myRole)) {
            menus.add(getKey(accountNo, getLang(lang, org)));
        }

        return menus;
    }


    private String getLang(String lang, String org) {
        return (!StringUtil.isEmpty(lang) && lang.contains("en")) ? org + "-en" : org;
    }

    @Override
    public String getKey(String accountNo, String roleType) {

        return accountNo + roleType;
    }


    @Override
    public boolean checkChange(Object extraData) {
        if (null == extraData) {
            System.out.println("extraData is empty");
            return false;
        } else {
            String r = (String) ((JSONObject) extraData).get("role");
            MsgHeader header = PsClientKeeper.msgHeader();
            String roleValue = "0";
            if (header.getReceiver().equals(apiper)) {
                roleValue = consumerService.getAMenuRole(header.getSender());

            } else {
                roleValue = consumerService.getPiperMenuRole(header.getSender(), header.getReceiver());
            }
            logger.debug("checkChange" + String.valueOf(extraData));
            if (StringUtil.isEmpty(r) || r.equals(roleValue)) {
                return false;
            }
            return true;
        }
    }

    /**
     * 重新设置新角色
     *
     * @param extraData
     * @return
     */
    @Override
    public Object getChangeInfo(Object extraData) {
        MsgHeader header = PsClientKeeper.msgHeader();
        String roleValue = "0";
        if (header.getReceiver().equals(apiper)) {
            roleValue = consumerService.getAMenuRole(header.getSender());

        } else {
            roleValue = consumerService.getPiperMenuRole(header.getSender(), header.getReceiver());
        }
        logger.debug(" getChangeInfo" + String.valueOf(roleValue));
        Map map = new HashMap();
        map.put("role", roleValue);
        return map;
    }

}
