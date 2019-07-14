package com.syswin.pipeline.psservice;

import com.alibaba.fastjson.JSONObject;
import com.syswin.pipeline.psservice.impl.CallBackRegister;
import com.syswin.pipeline.psservice.response.ResponeResultData;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.PSUtil;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.admin.common.CollectionUtil;
import com.syswin.ps.sdk.admin.service.impl.PSAccountService;
import com.syswin.ps.sdk.common.CDTPResponse;
import com.syswin.ps.sdk.common.CommonMsg;
import com.syswin.ps.sdk.common.HandlerParam;
import com.syswin.ps.sdk.service.KmsService;
import com.syswin.ps.sdk.service.PsClientService;
import com.syswin.ps.sdk.service.SingleChatService;
import com.syswin.ps.sdk.utils.HttpUtil;
import com.syswin.temail.ps.client.Header;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class RegisterServer {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServer.class);

    public static int sendType = 801;
    @Value("${psserver.path}")
    private String serverPath;
    @Value("${psserver.prex}")
    private String prex;
    @Value("${psserver.server.tmail}")
    private String adm;

    @Autowired
    private PSUtil psUtil;

    //	@Value("${url.temail-auth.server}")
//	private String actUrl;
    @Autowired
    private KmsService kmsService;

    @Autowired
    private PsClientService psClientService;
    @Autowired
    private CallBackRegister callBackRegister;
    @Autowired
    private SingleChatService msgSender;

    @Autowired
    private PSAccountService psAccountService;

    @Value("${url.temail-auth.server}")
    private String authUtl;


    @Value("${url.temail-auth.active}")
    private String activatePath;

    @Async("msgThreadPool")
    public void registerAccout(String temail) {

        // 6、cdtp调用永东接口，返回 激活码
        ResponeResultData rr = registerAccount(temail, adm + temail.split("@")[1]);
        // 7、Http Auth
        if (!StringUtil.isEmpty(rr)) {
            //梦男
            try {
                Boolean b = active(temail, rr.getActiveCode());
            } catch (Exception ex) {
            }
            psAccountService.login(temail);
        }
    }

    /**
     * 注册秘邮号：
     * a.piper给 a.dm 发消息 监听回调消息 获取激活码
     * 找孟祥超 20190516
     *
     * @param temail
     * @return
     */
//    private ResponeResultData registerAccount(String temail) {
//        return registerAccount(temail, adm + temail.split("@")[1]);
//    }

    /**
     * 注册秘邮号：
     * a.piper给 a.dm 发消息 监听回调消息 获取激活码
     * 找孟祥超 20190516
     *
     * @param temail
     * @return
     */
    private ResponeResultData registerAccount(String temail, String adm) {
        Map<String, String> attendanceData = new HashMap<>();
        attendanceData.put("temail", temail);
        String requestId = UUID.randomUUID().toString();
        callBackRegister.register(requestId);
        CDTPResponse<String> cdtpResponse = new CDTPResponse<>();
        try {

            logger.info("registerAccout  requestId {} temail{}", requestId, temail);
            msgSender.sendMsg(commonMsg(prex + temail.split("@")[1], adm, serverPath, attendanceData, requestId));
        } catch (Exception e) {
            log.error("sendMsg err ", e, temail);
        }

        CDTPResponse<String> result = callBackRegister.getResponse(requestId, cdtpResponse.getClass());
        log.info("获取结果,temail={},result={}", temail, result);
        if (StringUtil.isEmpty(result.getData())) {
            // 再次登录a.form
//            psAccountService.login(prex + temail.split("@")[1]);
            throw new BusinessException("调用registerAccount返回数据为null  " + temail + "-:" + result);
        }
//        ResponeResultData rr = parse(JSONObject.parseObject(result.getMessage()).getString("content"));
        ResponeResultData rr = JSONObject.parseObject(JSONObject.toJSONString(result.getData()), ResponeResultData.class);
        log.info("ResponeResultData {}", rr);
        return rr;
    }

    /**
     * 激活邮箱
     * 找郭梦男 20190516
     *
     * @param userId
     * @param activeCode
     * @return
     */

    public Boolean active(String userId, String activeCode) {
        String activeUrl = null;
//        try {
//            activeUrl = this.crossDomainService.getActiveUrl(userId);
//        } catch (Exception e) {
//        }
        log.info("activeUrl {}", activeUrl);
        if (StringUtil.isEmpty(activeUrl)) {
            activeUrl = authUtl + activatePath;
        }
        String pub_key = null;
        try {
            pub_key = this.kmsService.publishKey(new String[]{userId}).get(userId);
        } catch (Exception e) {
        }
        if (StringUtil.isEmpty(pub_key)) {
            pub_key = psUtil.sign(userId);
        }
        Map<String, String> params = CollectionUtil.fastMap("PUBLIC_KEY", pub_key);
        params.put("TeMail", userId);
        params.put("ACTIVATION_CODE", activeCode);
        String result = null;
        try {
            result = HttpUtil.httpRequest(activeUrl, params, "post", "application/x-www-form-urlencoded");
        } catch (Exception e) {
            log.error("调用 activeUrl 异常:{}", activeUrl, userId, activeCode);

            return false;
        }
        log.info("result:{}", result);
        String code = null;
        if (!StringUtil.isEmpty(result)) {
            code = JSONObject.parseObject(result).getString("code");
        }
        return "200".equals(code);
    }


    private <T> CommonMsg commonMsg(String from, String to, String path, T params, String requestId) {
        String msgId = UUID.randomUUID().toString();
        Header header = psClientService.header(from, to, msgId);
        log.info("header:" + header.toString());
        HandlerParam handlerParam = new HandlerParam(requestId, path, null, new HashMap<>(), null, null, params);
        CommonMsg commonMsg = new CommonMsg(header, sendType, handlerParam);
        log.info("commonMsg:" + commonMsg.toString());
        return commonMsg;
    }

}
