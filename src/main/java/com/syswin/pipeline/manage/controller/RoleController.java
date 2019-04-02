package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.dto.*;
import com.syswin.pipeline.manage.service.RoleService;
import com.syswin.pipeline.service.psserver.bean.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 115477 on 2019/3/29.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/role")
@Api(tags = "系统管理-角色")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("list")
    public ResponseEntity<PageInfo<RoleOutput>> list(@RequestBody RoleListInput param) {
        PageInfo<RoleOutput> pageInfo = roleService.list(param.getPageNo(), param.getPageSize());

        return new ResponseEntity(pageInfo);
    }

    @PostMapping("save")
    public ResponseEntity save(@RequestBody RoleInput param) {
        roleService.save(param);
        return new ResponseEntity();
    }

    @GetMapping("getMenuesByRoleId")
    public ResponseEntity<List<MenuOutput>> getMenuesByRoleId(@ModelAttribute RoleIdInput param) {
        List<MenuOutput> l = roleService.getMenuesByRoleId(param.getRoleId());
        return new ResponseEntity(l);
    }

    @PostMapping("saveMenuesByRoleId")
    public ResponseEntity saveMenuesByRoleId(@RequestBody RoleMenusInput param) {
        roleService.saveMenuesByRoleId(param.getRoleId(), param.getMenuIds());

        return new ResponseEntity();
    }

    @PostMapping("delete")
    public ResponseEntity delete(@RequestBody RoleIdInput param) {

        roleService.delete(param.getRoleId());
        return new ResponseEntity();
    }
}
