package com.syswin.pipeline.psservice.psserver;

import com.syswin.pipeline.psservice.psserver.bean.PsResponseEntity;

/**
 * 策略接口
 * 通过继承该接口，统一根据command处理业务
 * @param <T> 调用的接口传的参数
 *  ResponseBean 调用的接口返回的参数
 */
public interface BaseStrategy<T> {

    /**
     * @return 与前端调用对应的指令command
     */
    int command();

    /**
     * @return 与前端定义传入的Bean类型
     */
    Class beanclass();

    /**
     *业务处理
     * @param t 传入参数，类型和 beanclass一样
     * @return 返回业务处理响应
     */
    PsResponseEntity sevice(T t);

}
