package com.syswin.pipeline.psservice.impl;

import com.syswin.ps.sdk.common.CDTPResponse;
import com.syswin.ps.sdk.message.ICallBackRegister;
import com.syswin.ps.sdk.utils.FastJsonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class CallBackRegister implements ICallBackRegister {

    public  Map<String, BlockingQueue<String>> callBack = new ConcurrentHashMap<>();

    @Override
    public void register(String requestId) {
        callBack.putIfAbsent(requestId, new ArrayBlockingQueue<>(1));
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(callBack.get(requestId)==null){
                    return;
                }
                if(callBack.get(requestId).isEmpty()){
                    try {
                        CDTPResponse cdtpResponse =new  CDTPResponse();
                        cdtpResponse.setCode("-1");
                        cdtpResponse.setMsg("wait time out");
                        callBack.get(requestId).put(FastJsonUtil.toJson(cdtpResponse));
                        log.error("注册账号失败={}",requestId);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },6*1000);

    }

    @Override
    public Boolean hasRequestId(String requestId) {
        return callBack.containsKey(requestId);
    }

    @Override
    public void callBack(String requestId, String response){
        try {
            callBack.get(requestId).put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //TODO 记录日志
        }
    }

    @Override
    public <T> T getResponse(String requestId, Class<T> responseType) {
        try {
            String result=callBack.get(requestId).take();
            callBack.remove(requestId);
            return FastJsonUtil.fromJson(result,responseType);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //TODO 记录日志
        }
        return null;
    }





}
