package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.BrowseRecord;
import com.syswin.pipeline.db.model.BrowseRecordExample;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface BrowseRecordRepository extends BaseRepository< BrowseRecord> {

    Long countByExample(BrowseRecordExample publisherExample);

    List<BrowseRecord> selectByExample(BrowseRecordExample publisherExample);
}
