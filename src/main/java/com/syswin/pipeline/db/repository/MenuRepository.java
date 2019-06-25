package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Evaluation;
import com.syswin.pipeline.db.model.Menu;
import com.syswin.pipeline.db.model.MenuExample;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuRepository extends BaseRepository<Menu> {
    long countByExample(MenuExample example);

    int deleteByPrimaryKey(Long menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    List<Menu> selectByExample(MenuExample example);

    Menu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    @MapKey("menuId")
    Map<Long, Menu> selectParentIds();
}