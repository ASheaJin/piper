package com.syswin.pipeline.manage.service;

import com.syswin.pipeline.manage.dto.MenuOut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 115477 on 2019/3/29.
 */
@Service
public class MenuService {

    /**
     * 角色列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<MenuOut> list(int pageIndex, int pageSize) {
        return null;
    }

    public Boolean enableMenu(String menuId) {
        return false;
    }
}
