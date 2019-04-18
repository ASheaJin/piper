package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.manage.dto.output.MenuOutput;
import com.syswin.pipeline.manage.service.MenuService;
import com.syswin.pipeline.app.dto.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 115477 on 2019/3/29.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/menu")
@Api(tags = "系统管理-菜单")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("list")
    public ResponseEntity<List<MenuOutput>> list() {
        List<MenuOutput> list = menuService.list();
        return new ResponseEntity<>(list);
    }
}
