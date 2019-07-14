package com.syswin.pipeline.task;

import com.syswin.pipeline.psservice.MessegerSenderService;
import com.syswin.pipeline.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 监控使用
 *
 * @Author lhz on 2018/10/29.
 */
@Lazy(false)
@Component
@EnableScheduling
public class PollLogTask implements SchedulingConfigurer {

    @Autowired
    private MessegerSenderService messegerSenderService;
    //    private static String cornPeriod = "0 0 */3 * * ?";
    @Value("${log.cornPeriod}")
    private String cornPeriod = "0 */2 * * * ?";

    @Value("${log.sendF}")
    private String sendF = "";

    @Value("${app.ps-app-sdk.user-id}")
    private String reveiveA = "";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtil.isEmpty(sendF)) {
                        return;
                    }
                    Long startTime = System.currentTimeMillis();

                    messegerSenderService.sendSynchronizationTxt(sendF, reveiveA, "server is suc");
                    Long endTime = System.currentTimeMillis();
                    System.out.println("monitor process time：" + (endTime - startTime));

                } catch (Exception ex) {
                }

            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 任务触发，可修改任务的执行周期
                CronTrigger trigger = new CronTrigger(cornPeriod);
                return trigger.nextExecutionTime(triggerContext);
            }
        });
    }
}  
