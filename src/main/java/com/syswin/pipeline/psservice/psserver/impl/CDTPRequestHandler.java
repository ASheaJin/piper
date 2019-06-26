package com.syswin.pipeline.psservice.psserver.impl;

import com.alibaba.fastjson.JSON;
import com.syswin.pipeline.psservice.psserver.BaseStrategy;
import com.syswin.pipeline.psservice.psserver.bean.PsResponseEntity;
import com.syswin.pipeline.service.exception.BusinessException;
import com.syswin.pipeline.utils.FastJsonUtil;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.temail.ps.common.entity.CDTPPacket;
import com.syswin.temail.ps.server.service.RequestService;
import com.syswin.temail.ps.server.utils.SignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * CDTP请求处理器
 * Created by GuoMengnan on 2018/9/26.
 */
@Service
//@Slf4j
public class CDTPRequestHandler implements RequestService {
	private static final Logger logger = LoggerFactory.getLogger(CDTPRequestHandler.class);

	//定义一个Map存储 打折策略对象(策略针对的类型（key）-->对象本身（value）)
	HashMap<Integer,BaseStrategy> map = new HashMap<>();
	//构造函数中，自动注入所有的策略实现对象，List集合形式
	public  CDTPRequestHandler(List<BaseStrategy> baseStrategyList){
		for(BaseStrategy baseStrategy :baseStrategyList){
			logger.info("baseStrategy:"+baseStrategy);
			map.put(baseStrategy.command(),baseStrategy);
		}
	}

	public void handleRequest(CDTPPacket reqPacket, Consumer<CDTPPacket> responseHandler) {
		try {
			logger.info("Recieve CDTP request ：{}" , JSON.toJSONString(reqPacket));
		} catch (Exception e) {
			logger.error("请求参数：{}", JSON.toJSONString(reqPacket));
			throw new BusinessException(e, reqPacket);
		}
		String requestData = StringUtil.byte2Str(reqPacket.getData());
		//记录插入数据时日志
		int command = reqPacket.getCommand();
		logger.info("command:" + command);

		PsResponseEntity responseBean ;
		//判断指令对应的服务是否存在
		if(map.get(command) == null){
			 responseBean = new PsResponseEntity("500","server is not exist",null);
		}else{
			//3.处理业务,并获取返回
			responseBean =map.get(command).sevice(FastJsonUtil.parseObject(requestData ,map.get(command).beanclass()));

		}

		logger.info("responseBean:" + responseBean.toString());
		byte[] data = FastJsonUtil.toJson(responseBean.toString()).getBytes(Charset.forName("UTF-8"));
		reqPacket.setData(data);
		logger.info("reqPacket {}",reqPacket.toString());
		SignatureUtil.resetSignature(reqPacket);
		responseHandler.accept(reqPacket);
	}


}
