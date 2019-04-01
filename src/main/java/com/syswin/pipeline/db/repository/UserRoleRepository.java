package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.UserRoleExample;
import com.syswin.pipeline.db.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleRepository {

    int insert(UserRole record);

    int insertSelective(UserRole record);

    List<UserRole> selectByExample(UserRoleExample example);

    int deleteByPrimaryKey(UserRole key);

    int deleteByUserId(@Param("userId") Long userId);

    int deleteByRoleId(@Param("roleId") Long roleId);
}