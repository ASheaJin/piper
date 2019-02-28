package com.syswin.pipeline.db.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface DataAnalysisRepository {

    int selectAccount(@Param("startTime") int startTime, @Param("endTime") int endTime);

    int selectServerLog(@Param("startTime") int startTime, @Param("endTime") int endTime);

    int selectPublisher(@Param("startTime") int startTime, @Param("endTime") int endTime);

    int selectTransaction(@Param("startTime") int startTime, @Param("endTime") int endTime);


}
