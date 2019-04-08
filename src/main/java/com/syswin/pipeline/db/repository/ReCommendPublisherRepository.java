package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.ReCommendPublisher;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface ReCommendPublisherRepository extends BaseRepository<ReCommendPublisher> {

	ReCommendPublisher selectByPublisherId(@Param("publisherId") String publisherId);

	List<ReCommendPublisher> selectByUserId(@Param("userId") String userId);

	List<ReCommendPublisher> seletByPubliserIds(List<String> publisherIds);

	int deleteBypid(@Param("publisherId") String publisherId);
}
