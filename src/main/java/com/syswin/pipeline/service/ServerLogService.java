package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.ServerLog;
import com.syswin.pipeline.db.repository.ServerLogRepository;
import com.syswin.pipeline.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 记录日志
 * Created by lhz on 2019/1/4.
 */
@Service
public class ServerLogService {
	@Autowired
	ServerLogRepository serverLogRepository;

	/**
	 * 插入服务器日志
	 *
	 * @param command     指令
	 * @param userId      发送人userId
	 * @param requestData 接受执行
	 * @return ServerLog 返回当前日志对象
	 */
	public ServerLog inserLog(int command, String userId, String requestData) {

		ServerLog serverLog = new ServerLog();
		serverLog.setCommand(command);
		serverLog.setUserId(userId);
		serverLog.setCreateTime(String.valueOf(System.currentTimeMillis()));
		serverLog.setRequestData(requestData);
		String logId = String.valueOf(SnowflakeIdWorker.getInstance().nextId());
		serverLog.setId(logId);
		serverLogRepository.insert(serverLog);
		return serverLog;
	}

	/**
	 * 修改返回的用户日志
	 *
	 * @param serverLog
	 * @param responseData
	 */
	public void updateLog(ServerLog serverLog, String responseData) {

		serverLog.setEndTime(System.currentTimeMillis());
		serverLog.setResponseData(responseData);
		serverLogRepository.update(serverLog);
	}
}
