package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.User;
import com.syswin.pipeline.db.model.UserExample;
import java.util.List;

public interface UserRepository {
    long countByExample(UserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}