package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.*;
import com.syswin.pipeline.db.repository.*;
import com.syswin.pipeline.manage.dto.*;
import com.syswin.pipeline.utils.BeanConvertUtil;
import com.syswin.pipeline.utils.MD5Coder;
import com.syswin.pipeline.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 115477 on 2019/3/29.
 */
@Service
public class UserService {

    private String defaultPwd = "syswin#123";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private MenuRepository menuRepository;

    public PageInfo<UserOut> list(int pageIndex, int pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize <= 30 && pageSize >= 1 ? pageSize : 30;

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andStatusEqualTo(Byte.valueOf("1"));

        PageHelper.startPage(pageIndex, pageSize);
        List<User> userList = this.userRepository.selectByExample(userExample);
        List<UserOut> userOuts = BeanConvertUtil.mapList(userList, UserOut.class);

        return new PageInfo(userOuts);
    }

    /**
     * 新增和修改用户
     *
     * @return
     */
    public Boolean saveUser(UserParam userParam) {
        String userId = userParam.getUserId();
        if (StringUtils.isEmpty(userId)) {
            if (StringUtils.isEmpty(userParam.getLoginName())) {
                throw new RuntimeException("登录名为空");
            }
            if (StringUtils.isEmpty(userParam.getPassword())) {
                throw new RuntimeException("密码为空");
            }

            String salt = TokenUtil.randString(5);
            String encodePwd = MD5Coder.MD5(userParam.getPassword() + salt);

            User user = new User();
            user.setUserId(Long.parseLong(userId));
            user.setLoginName(userParam.getLoginName());
            user.setPassword(encodePwd);
            user.setSalt(salt);
            user.setUserName(userParam.getUserName());
            user.setEmail(userParam.getEmail());
            user.setRemark(userParam.getRemark());
            userRepository.insert(user);
        } else {
            User user = new User();
            user.setUserId(Long.parseLong(userId));
            user.setLoginName(userParam.getLoginName());
            user.setUserName(userParam.getUserName());
            user.setEmail(userParam.getEmail());
            user.setRemark(userParam.getRemark());
            userRepository.updateByPrimaryKeySelective(user);
        }

        return true;
    }

    public Boolean updatePassword(PasswordParam passwordParam) {
        String userId = passwordParam.getUserId();
        String oldPwd = passwordParam.getOldPassword();
        String newPwd = passwordParam.getNewPassword();

        User user = userRepository.selectByPrimaryKey(Long.parseLong(userId));
        if (user == null) {
            throw new RuntimeException("用户id错误 " + userId );
        }
        String encodePwd = MD5Coder.MD5(oldPwd + user.getSalt());
        if (encodePwd.equals(user.getPassword())) {
            throw new RuntimeException("原密码错误 " + userId );
        }

        User saveUser = new User();
        String newEncodePwd = MD5Coder.MD5(newPwd + user.getSalt());
        saveUser.setUserId(Long.parseLong(userId));
        saveUser.setPassword(newEncodePwd);
        userRepository.updateByPrimaryKeySelective(saveUser);

        return true;
    }

    /**
     * 重设密码
     */
    public Boolean resetPassword(String userId) {
        User user = userRepository.selectByPrimaryKey(Long.parseLong(userId));
        if (user == null) {
            throw new RuntimeException("用户id错误 " + userId );
        }
        //TODO defaultPwd
        String encodePwd = MD5Coder.MD5(defaultPwd + user.getSalt());

        User saveUser = new User();
        saveUser.setUserId(Long.parseLong(userId));
        saveUser.setPassword(encodePwd);
        userRepository.updateByPrimaryKeySelective(saveUser);

        return true;
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    public Boolean deleteUser(String userId) {

        User user = userRepository.selectByPrimaryKey(Long.parseLong(userId));
        if (user == null) {
            throw new RuntimeException("用户id错误 " + userId );
        }
        User saveUser = new User();
        saveUser.setUserId(Long.parseLong(userId));
        saveUser.setStatus(Byte.valueOf("0"));
        userRepository.updateByPrimaryKeySelective(saveUser);

        return true;
    }

    /**
     * 修改用户可用状态
     *
     * @param status
     * @return
     */
    public Boolean enableUser(String userId, int status) {
        User user = userRepository.selectByPrimaryKey(Long.parseLong(userId));
        if (user == null) {
            throw new RuntimeException("用户id错误 " + userId );
        }
        User saveUser = new User();
        saveUser.setUserId(Long.parseLong(userId));
        saveUser.setStatus(Byte.valueOf(String.valueOf(status)));
        userRepository.updateByPrimaryKeySelective(saveUser);

        return true;
    }

    public List<RoleOut> getRolesByUserId(String userId) {
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(Long.parseLong(userId));
        List<UserRole> userRoles = userRoleRepository.selectByExample(example);

        if (!userRoles.isEmpty()) {
            List<Long> roleIds = userRoles.stream().map((ur) -> ur.getRoleId()).collect(Collectors.toList());

            RoleExample roleExample = new RoleExample();
            roleExample.createCriteria().andRoleIdIn(roleIds);
            List<Role> roles = roleRepository.selectByExample(roleExample);
            List<RoleOut> roleOuts = BeanConvertUtil.mapList(roles, RoleOut.class);
            return roleOuts;
        }
        return new ArrayList<>();
    }

    public List<MenuOut> getMenesByUserId(String userId) {
        //先实现一个用户只有一个角色
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(Long.parseLong(userId));
        List<UserRole> userRoles = userRoleRepository.selectByExample(example);
        if (!userRoles.isEmpty()) {
            Long roleId = userRoles.get(0).getRoleId();

            RoleMenuExample roleMenuExample = new RoleMenuExample();
            roleMenuExample.createCriteria().andRoleIdEqualTo(roleId);
            List<RoleMenu> roleMenus = roleMenuRepository.selectByExample(roleMenuExample);

            if (!roleMenus.isEmpty()) {
                List<Long> menuIds = roleMenus.stream().map((ur) -> ur.getMenuId()).collect(Collectors.toList());

                MenuExample menuExample = new MenuExample();
                menuExample.createCriteria().andMenuIdIn(menuIds);
                List<Menu> menus = menuRepository.selectByExample(menuExample);
                List<MenuOut> menuOuts = BeanConvertUtil.mapList(menus, MenuOut.class);
                return menuOuts;
            }
        }
        return new ArrayList<>();
    }

    @Transactional
    public Boolean saveRolesByUserId(String userId, List<String> roleIds) {
        userRoleRepository.deleteByUserId(Long.parseLong(userId));

        for (String roleId : roleIds) {
            UserRole ur = new UserRole();
            ur.setUserId(Long.parseLong(userId));
            ur.setRoleId(Long.parseLong(roleId));
            userRoleRepository.insert(ur);
        }
        return false;
    }
}
