package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.UserRoleExample;
import com.syswin.pipeline.db.model.UserRoleKey;
import java.util.List;

public interface UserRoleRepository {
    int deleteByPrimaryKey(UserRoleKey key);

    int insert(UserRoleKey record);

    int insertSelective(UserRoleKey record);

    List<UserRoleKey> selectByExample(UserRoleExample example);
}