package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.ServerLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface ServerLogRepository {

    ServerLog selectById(@Param("userId") String userId);
    Integer insert(ServerLog serverLog);
    Integer update(ServerLog serverLog);
}
