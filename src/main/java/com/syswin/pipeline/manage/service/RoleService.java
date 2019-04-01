package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.Role;
import com.syswin.pipeline.manage.dto.MenuOut;
import com.syswin.pipeline.manage.dto.RoleOut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 115477 on 2019/3/29.
 */
@Service
public class RoleService {

    /**
     * 角色列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<RoleOut> list(int pageIndex, int pageSize) {
        return null;
    }


    public List<MenuOut> getMenuesByRoleId(String roleId) {

        return null;
    }

    public Boolean saveMenuesByRoleId(String roleId, List<String> menuIds) {
        return false;
    }

}
