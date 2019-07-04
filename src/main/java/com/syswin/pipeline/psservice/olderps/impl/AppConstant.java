package com.syswin.pipeline.psservice.olderps.impl;

/**
 * @author by Administrator on 2018/4/9.
 */
public final class AppConstant {

    public static final String DEFAULT_HOST = "url.temail-gateway.host";

    public static final String DEFAULT_PORT = "url.temail-gateway.port";

    public static final String KMS_SERVER = "app.ps-app-sdk.kms-server";

    public static final String AUTH_SERVER = "url.temail-auth.server";
    public static final String REGISTER_SERVER = "url.temail-auth.register";
    public static final String PUBLISH_SERVER = "url.temail-auth.publish";

    public static final String USER_ID = "app.ps-app-sdk.user-id";

    public static final String TENANT_ID = "app.ps-app-sdk.tenant-id";

    public static final String DEVICE_ID = "app.pipeline.deviceId";

    public static final Integer GET_MESSAGE_INFO_FLAG=999;

    public static  final String REPLAY_MESSAGE="we have receive you message,thanks";


    public final static short msgCMS = 1;//单聊消息cms
    public final static short singleChatComId = 1;
    public final static short synsMsgListComId = 2;//获取消息数
    public final static short synsMsgInfoComId = 3;//获取消息详情

}
