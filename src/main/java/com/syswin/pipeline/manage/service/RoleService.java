package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.*;
import com.syswin.pipeline.db.repository.MenuRepository;
import com.syswin.pipeline.db.repository.RoleMenuRepository;
import com.syswin.pipeline.db.repository.RoleRepository;
import com.syswin.pipeline.db.repository.UserRoleRepository;
import com.syswin.pipeline.manage.dto.MenuOut;
import com.syswin.pipeline.manage.dto.RoleOut;
import com.syswin.pipeline.utils.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2019/3/29.
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;
    /**
     * 角色列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<RoleOut> list(int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize <= 30 && pageSize >= 1 ? pageSize : 30;

        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andStatusEqualTo(Byte.valueOf("1"));

        PageHelper.startPage(pageIndex, pageSize);
        List<Role> roleList = this.roleRepository.selectByExample(roleExample);
        List<RoleOut> roleOuts = BeanConvertUtil.mapList(roleList, RoleOut.class);

        return new PageInfo(roleOuts);
    }


    public List<MenuOut> getMenuesByRoleId(String roleId) {
        RoleMenuExample example = new RoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(Long.parseLong(roleId));
        List<RoleMenu> roleMenus = roleMenuRepository.selectByExample(example);

        if (!roleMenus.isEmpty()) {
            List<Long> menuIds = roleMenus.stream().map((ur) -> ur.getMenuId()).collect(Collectors.toList());

            MenuExample menuExample = new MenuExample();
            menuExample.createCriteria().andMenuIdIn(menuIds);
            List<Menu> menus = menuRepository.selectByExample(menuExample);
            List<MenuOut> menuOuts = BeanConvertUtil.mapList(menus, MenuOut.class);
            return menuOuts;
        }
        return new ArrayList<>();
    }
    @Transactional
    public Boolean saveMenuesByRoleId(String roleId, List<String> menuIds) {
        roleMenuRepository.deleteByRoleId(Long.parseLong(roleId));

        for (String menuId : menuIds) {
            RoleMenu rm = new RoleMenu();
            rm.setMenuId(Long.parseLong(menuId));
            rm.setRoleId(Long.parseLong(roleId));
            roleMenuRepository.insert(rm);
        }

        return true;
    }
    @Transactional
    public Boolean deleteRole(String roleId) {
        Long rid = Long.parseLong(roleId);

        roleRepository.deleteByPrimaryKey(rid);

        userRoleRepository.deleteByRoleId(rid);

        roleMenuRepository.deleteByRoleId(rid);

        return true;
    }
}
