package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Role;
import com.syswin.pipeline.db.model.RoleExample;
import java.util.List;

public interface RoleRepository {
    long countByExample(RoleExample example);

    int deleteByPrimaryKey(Long roleId);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}