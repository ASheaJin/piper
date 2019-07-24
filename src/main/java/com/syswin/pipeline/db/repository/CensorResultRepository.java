package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.CensorResult;
import com.syswin.pipeline.db.model.CensorResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CensorResultRepository {
    long countByExample(CensorResultExample example);

    int deleteByPrimaryKey(Long contentId);

    int insert(CensorResult record);

    int insertSelective(CensorResult record);

    List<CensorResult> selectByExample(CensorResultExample example);

    CensorResult selectByPrimaryKey(Long contentId);

    int updateByExampleSelective(@Param("record") CensorResult record, @Param("example") CensorResultExample example);

    int updateByExample(@Param("record") CensorResult record, @Param("example") CensorResultExample example);

    int updateByPrimaryKeySelective(CensorResult record);

    int updateByPrimaryKey(CensorResult record);
}