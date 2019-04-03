package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.SpiderToken;
import com.syswin.pipeline.db.model.SpiderTokenExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpiderTokenRepository {
    long countByExample(SpiderTokenExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SpiderToken record);

    int insertSelective(SpiderToken record);

    List<SpiderToken> selectByExample(SpiderTokenExample example);

    SpiderToken selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SpiderToken record, @Param("example") SpiderTokenExample example);

    int updateByExample(@Param("record") SpiderToken record, @Param("example") SpiderTokenExample example);

    int updateByPrimaryKeySelective(SpiderToken record);

    int updateByPrimaryKey(SpiderToken record);
}