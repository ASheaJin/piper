package com.syswin.pipeline.manage.controller;

import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.manage.service.UserService;
import com.syswin.pipeline.manage.dto.input.*;
import com.syswin.pipeline.manage.dto.output.MenuOutput;
import com.syswin.pipeline.manage.dto.output.RoleOutput;
import com.syswin.pipeline.manage.dto.output.UserOutput;
import com.syswin.pipeline.app.dto.ResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 115477 on 2019/3/29.
 */
@CrossOrigin
@RestController
@RequestMapping("/manage/user")
@Api(tags = "系统管理-用户")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("list")
    public ResponseEntity<PageInfo<UserOutput>> list(@RequestBody UserListInput param) {
        PageInfo<UserOutput> pageInfo = userService.list(param.getPageNo(), param.getPageSize());

        return new ResponseEntity(pageInfo);
    }

    @PostMapping("save")
    public ResponseEntity save(@RequestBody UserInput param) {

        try {
            userService.save(param);
        } catch (RuntimeException ae) {
            return new ResponseEntity("500", ae.getMessage());
        }
        return new ResponseEntity();
    }

    @PostMapping("delete")
    public ResponseEntity delete(@RequestBody UserIdInput param) {
        userService.delete(param.getUserId());
        return new ResponseEntity();
    }

    @GetMapping("getRolesByUserId")
    public ResponseEntity<List<RoleOutput>> getRolesByUserId(@ModelAttribute UserIdInput param) {
        List<RoleOutput> out = userService.getRolesByUserId(param.getUserId());
        return new ResponseEntity(out);
    }


    @PostMapping("saveRolesByUserId")
    public ResponseEntity saveRolesByUserId(@RequestBody UserRolesInput param) {
        userService.saveRolesByUserId(param.getUserId(), param.getRoleIds());
        return new ResponseEntity();
    }

    @GetMapping("getMenesByUserId")
    public ResponseEntity<List<MenuOutput>> getMenesByUserId(@ModelAttribute UserIdInput param) {
        List<MenuOutput> out = userService.getMenesByUserId(param.getUserId());
        return new ResponseEntity(out);
    }

    @PostMapping("updatePassword")
    public ResponseEntity updatePassword(@RequestBody PasswordInput passwordParam) {
        String loginName = null; //TODO
        userService.updatePassword(loginName, passwordParam);
        return new ResponseEntity();
    }

    @PostMapping("resetPassword")
    public ResponseEntity resetPassword(@RequestBody PasswordInput passwordParam) {
        String loginName = null; //TODO
        userService.resetPassword(loginName);
        return new ResponseEntity();
    }
}
