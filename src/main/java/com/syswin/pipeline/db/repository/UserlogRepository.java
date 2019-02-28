package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Userlog;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface UserlogRepository extends BaseRepository<Userlog> {


    List<Userlog> selectByIdTime(@Param("userId") String userId, @Param("startTime") int startTime, @Param("endTime") int endTime, @Param("start") int start, @Param("end") int end);
}
