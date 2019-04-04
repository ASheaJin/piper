package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Menu;
import com.syswin.pipeline.db.model.MenuExample;
import java.util.List;
import java.util.Map;

public interface MenuRepository {
    long countByExample(MenuExample example);

    int deleteByPrimaryKey(Long menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    List<Menu> selectByExample(MenuExample example);

    Menu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    Map<Long, Long> selectParentIds();
}