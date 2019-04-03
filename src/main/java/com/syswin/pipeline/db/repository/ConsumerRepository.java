package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Consumer;
import com.syswin.pipeline.db.model.ConsumerExample;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface ConsumerRepository extends BaseRepository<Consumer> {

	long countByExample(ConsumerExample example);

	int deleteByPrimaryKey(Integer id);

	int insertSelective(Consumer record);

	List<Consumer> selectByExample(ConsumerExample example);

	Consumer selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") Consumer record, @Param("example") ConsumerExample example);

	int updateByExample(@Param("record") Consumer record, @Param("example") ConsumerExample example);

	int updateByPrimaryKeySelective(Consumer record);

	int updateByPrimaryKey(Consumer record);

}
