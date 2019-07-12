package com.syswin.pipeline.app.controller;

import com.github.pagehelper.util.StringUtil;
import com.syswin.pipeline.app.dto.*;
import com.syswin.pipeline.app.dto.output.ResEntity;
import com.syswin.pipeline.psservice.MessegerSenderService;
import com.syswin.pipeline.psservice.RegisterServer;
import com.syswin.pipeline.service.org.IOrgService;
import com.syswin.pipeline.service.org.OrgOut;
import com.syswin.pipeline.utils.PSUtil;
import com.syswin.pipeline.utils.SwithUtil;
import com.syswin.sub.api.db.model.Publisher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 115477 on 2018/12/18.
 */
@CrossOrigin
@RestController
@RequestMapping("/ps")
@Api(value = "ps", tags = "ps")
public class PSClientController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PSClientController.class);

    @Autowired
    private MessegerSenderService messegerSenderService;
    @Autowired
    private IOrgService orgService;

    @Autowired
    com.syswin.sub.api.SubscriptionService scriptionService;

    @Autowired
    private com.syswin.sub.api.PublisherService subPublisherService;

    @Value("${app.ps-app-sdk.user-id}")
    private String piperUserId;

    @Autowired
    private RegisterServer psServerService;
    @Autowired
    private PSUtil psUtil;


    @PostMapping("/getPubKey")
    @ApiOperation(
            value = "获取pubKey"
    )
    public ResEntity getPubKey(@RequestBody String senderTemail, HttpServletRequest request) {
        String publicKey = psUtil.publickey(senderTemail);
        if (StringUtil.isEmpty(publicKey)) {
            publicKey = psUtil.sign(senderTemail);
            return suc("get suc", publicKey);
        } else {
            return suc("register suc", publicKey);
        }

    }


    @GetMapping("/config")
    @ApiOperation(
            value = "config"
    )
    public String config(String config) {

        SwithUtil.tt = Integer.parseInt(config);

        return "suc";
    }

    @GetMapping("/createTest")
    @ApiOperation(
            value = "批量生成密机测试"
    )
    public List createTest() {

        List<String> createUserList = new ArrayList<>();
        String sendertt = "INSERT INTO `user_temail` (temail, user_id, ALGORITHM, TYPE, domain, create_time, update_time) VALUES ('%s', '%s', 1, 4, 'support2technical.me', UNIX_TIMESTAMP(NOW())*1000, UNIX_TIMESTAMP(NOW())*1000);";
//			String pub = psClientService.getTemailPublicKey(senderTemail);
        String tt = String.format(sendertt, "a.piper@" + piperUserId.split("@")[1], "111");
        createUserList.add(tt);
        for (int i = 1; i < 301; i++) {
//			senderTemail="p."+(10000000+i)+"@systoontest.com";
//			String pub = psClientService.getTemailPublicKey(senderTemail);
            tt = String.format(sendertt, ("p." + (10000000 + i) + "@" + piperUserId.split("@")[1].trim()), "111");
            createUserList.add(tt);
        }

        return createUserList;
    }

    class CreateUser {
        public String mail;
        public String pk;
    }

    @PostMapping("/sendOthermessage")
    @ApiOperation(
            value = "测试接口。发送消息"
    )
    public String sendOthermessage(@RequestBody PublishMessageParam message) throws Exception {
        String content = "{\"w\":540,\"h\":960,\"isOriginal\":0,\"suffix\":\".png\",\"url\":\"http:\\/\\/temail-test.cn-bj.ufileos.com\\/mediabank%2Fdd8da1b9f51444be842411fd79cbfd8a.zip\",\"size\":28419,\"pwd\":\"7B13B225-10BC-4141-87D9-FD1139FCCF52\"}";
//		String content = "{\"text\":\"vhh\"}";
        messegerSenderService.sendSynchronizationContent(message.getFromTemail(), message.getPtemail(), UUID.randomUUID().toString(), Integer.parseInt(message.getBodyType()), message.getContent());
        return "success";
    }

    @GetMapping("/setCard")
    @ApiOperation(
            value = "发送名片"
    )
    public void setCard(String temail, String to, String nick) throws Exception {

        messegerSenderService.sendCard(temail, to, nick);
    }

    @PostMapping("/sendCards")
    @ApiOperation(
            value = "批量发名片"
    )
    public ResponseEntity sendCards(@RequestBody PsSubOrgListParam modify) {
        Publisher publisher = subPublisherService.getPubLisherById(modify.getPublisherId());
        if (publisher == null) {
            return new ResponseEntity("500", "该出版社不存在");
        }
        List<String> userList = scriptionService.getSubscribers(publisher.getPtemail(), null);
        for (String userId : userList) {
            if (com.syswin.pipeline.utils.StringUtils.isNullOrEmpty(psUtil.publickey(userId))) {
                continue;
            }
            try {
                //判断是否自己订阅自己
                if (userId.equals(publisher.getUserId())) {
                    messegerSenderService.sendCard(publisher.getPtemail(), userId, "* " + modify.getName(), modify.getIconUrl());
                } else {
                    messegerSenderService.sendCard(publisher.getPtemail(), userId, modify.getName(), modify.getIconUrl());
                }
            } catch (Exception ex) {
            }

        }
        return new ResponseEntity();

    }


    @GetMapping("/getOrg")
    @ApiOperation(
            value = "登陆出版社监控"
    )
    public OrgOut getOrg(HttpServletRequest request, String temail) {
        OrgOut orgOut = orgService.getOrgByVersion(temail, 0);
        return orgOut;
    }


    @PostMapping("/registerAccount")
    @ApiOperation(
            value = "注册账号"
    )
    public ResponseEntity registerAccount(@RequestBody RegisterParam rp) {
        psServerService.registerAccount(rp.getTemail());
        return new ResponseEntity();
    }


    @PostMapping("/registerAccount3")
    @ApiOperation(
            value = "注册账号3"
    )
    public ResponseEntity registerAccount3(@RequestBody RegisterParam rp) {
        psServerService.registerAccount(rp.getTemail(), rp.getServer());
        return new ResponseEntity();
    }

    @PostMapping("/activeAccount")
    @ApiOperation(
            value = "激活账户"
    )
    public ResponseEntity activeAccount(@RequestBody ActiveParam ap) {
        psServerService.activeAccout(ap.getTemail(), ap.getCode());
        return new ResponseEntity();
    }
}
