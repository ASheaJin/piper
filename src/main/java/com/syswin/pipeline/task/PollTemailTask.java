package com.syswin.pipeline.task;

import com.syswin.pipeline.service.PiperPublisherService;
import com.syswin.pipeline.utils.SwithUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 轮询获取发送列表。
 * 支持disconf修改时间周期
 *
 * @Author lhz on 2018/10/29.
 */
@Lazy(false)
@Component
//@EnableScheduling
public class PollTemailTask implements SchedulingConfigurer {

    private static final String CORN = "cron";

    @Autowired
    PiperPublisherService publisherService;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                if ("no".equals(SwithUtil.TURNPS)) {
                    /**
                     * 轮询 消息
                     * 复杂度 ： （1）1次获取列表+ 1次查数据库 多次缓存获取sender
                     * （2）n个管理员获取消息* (k次处理消息、密机解密未算 +1次更新库  日志)
                     * 2个人（没有消息时80~100ms， 有1个人消息200~1200ms ）
                     * bug：发消息乱码、解析、健康监测、推送消息时间搓
                     * 潜在bug：更新数据库与轮询冲突
                     */
                    Long startTime = System.currentTimeMillis();
                    Long endTime = System.currentTimeMillis();
                    System.out.println("monitor process time：" + (endTime - startTime));

                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
//                String corn = routeAdapterService.getRouteUrl(CORN);
//                if(!StringUtils.isEmpty(corn)) {
//                    SwithUtil.cornPeriod = corn;
//                }
                // 任务触发，可修改任务的执行周期
                CronTrigger trigger = new CronTrigger(SwithUtil.cornPeriod);
                return trigger.nextExecutionTime(triggerContext);
            }
        });
    }
}  
