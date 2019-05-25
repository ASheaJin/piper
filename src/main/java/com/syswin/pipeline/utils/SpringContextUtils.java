package com.syswin.pipeline.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by 601154 on 2018/5/4.
 */

@Component
@Configuration
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context; // Spring应用上下文环境

    public SpringContextUtils(){
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        SpringContextUtils.context = context;
    }
    public static ApplicationContext getContext(){
        return context;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) context.getBean(name);
    }


}
