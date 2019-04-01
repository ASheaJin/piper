package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.User;
import com.syswin.pipeline.db.model.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRepository {
    long countByExample(UserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long userId);

    User selectByLoginName(@Param("loginName") String loginName);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}