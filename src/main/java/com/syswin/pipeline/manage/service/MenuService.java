package com.syswin.pipeline.manage.service;

import com.syswin.pipeline.db.model.Menu;
import com.syswin.pipeline.db.model.MenuExample;
import com.syswin.pipeline.db.repository.MenuRepository;
import com.syswin.pipeline.db.repository.RoleMenuRepository;
import com.syswin.pipeline.manage.dto.MenuOutput;
import com.syswin.sub.api.utils.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 115477 on 2019/3/29.
 */
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;
    /**
     * 菜单树
     *
     * @return
     */
    public List<MenuOutput> list() {
        MenuExample menuExample = new MenuExample();
        List<Menu> menus = menuRepository.selectByExample(menuExample);
        List<MenuOutput> menuOuts = BeanConvertUtil.mapList(menus, MenuOutput.class);
        return menuOuts;
    }
    @Transactional
    public Boolean deleteMenu(String menuId) {
        Long mid = Long.parseLong(menuId);

        menuRepository.deleteByPrimaryKey(mid);

        roleMenuRepository.deleteByRoleId(mid);

        return false;
    }
}
