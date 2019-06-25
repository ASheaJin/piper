package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.RoleMenuExample;
import com.syswin.pipeline.db.model.RoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository {
    int deleteByPrimaryKey(RoleMenu key);

    int deleteByRoleId(@Param("roleId")Long roleId);

    int deleteByMenuId(@Param("menuId")Long menuId);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);

    List<RoleMenu> selectByExample(RoleMenuExample example);
}