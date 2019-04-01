package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.ReCommendContent;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface ReCommendContentRepository extends BaseRepository<ReCommendContent> {

	ReCommendContent selectByContentId(@Param("contentId") String contentId);

	List<ReCommendContent> selectByUserId(@Param("userId") String userId);

}
