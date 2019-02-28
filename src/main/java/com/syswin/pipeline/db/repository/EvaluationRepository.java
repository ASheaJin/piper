package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Evaluation;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface EvaluationRepository extends BaseRepository<Evaluation> {

    Evaluation selectByUserIdContentId(@Param("userId") String userId, @Param("contentId") Long contentId);
}
