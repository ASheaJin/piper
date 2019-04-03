package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.*;
import com.syswin.pipeline.db.repository.*;
import com.syswin.pipeline.manage.dto.*;
import com.syswin.pipeline.utils.MD5Coder;
import com.syswin.pipeline.utils.TokenUtil;
import com.syswin.sub.api.utils.BeanConvertUtil;
import com.syswin.sub.api.utils.SnowflakeIdWorker;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
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

    public PageInfo<UserOutput> list(int pageNo, int pageSize) {
        pageNo = pageNo < 1 ? 1 : pageNo;
        pageSize = pageSize <= 30 && pageSize >= 1 ? pageSize : 30;

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andStatusEqualTo(Byte.valueOf("1"));

        PageHelper.startPage(pageNo, pageSize);
        List<User> userList = this.userRepository.selectByExample(userExample);
        List<UserOutput> userOuts = BeanConvertUtil.mapList(userList, UserOutput.class);
        PageInfo pageInfo = new PageInfo(userList);
        pageInfo.setList(userOuts);

        return pageInfo;
    }

    /**
     * 新增和修改用户
     *
     * @return
     */
    public Boolean save(UserInput userParam) {
        String userId = userParam.getUserId();
        if (StringUtils.isEmpty(userId)) {
            if (StringUtils.isEmpty(userParam.getLoginName())) {
                throw new RuntimeException("登录名为空");
            }
            if (StringUtils.isEmpty(userParam.getPassword())) {
                throw new RuntimeException("密码为空");
            }

            User existUser = userRepository.selectByLoginName(userParam.getLoginName());
            if (existUser != null) {
                throw new RuntimeException("用户 " + userParam.getLoginName() + " 已存在");
            }

            //新用户都用默认密码
            String salt = TokenUtil.randString(5);
            String encodePwd = MD5Coder.MD5(defaultPwd + salt);

            User user = new User();
            user.setUserId(SnowflakeIdWorker.getInstance().nextId());
            user.setLoginName(userParam.getLoginName());
            user.setPassword(encodePwd);
            user.setSalt(salt);
            user.setUserName(userParam.getUserName());
            user.setEmail(userParam.getEmail());
            user.setRemark(userParam.getRemark());
            user.setStatus(Byte.valueOf("1"));
            userRepository.insertSelective(user);
        } else {
            User user = new User();
            user.setUserId(Long.parseLong(userId));
            user.setLoginName(userParam.getLoginName());
            user.setUserName(userParam.getUserName());
            user.setEmail(userParam.getEmail());
            user.setRemark(userParam.getRemark());
            int c = userRepository.updateByPrimaryKeySelective(user);
            if (c == 0) {
                throw new RuntimeException("用户id错误 " + userId );
            }
        }

        return true;
    }

    public Boolean updatePassword(PasswordInput passwordParam) {
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
     * 登录方法
     * @param loginParam
     * @return
     */
    public LoginOutput login(LoginInput loginParam) {
        String loginName = loginParam.getLoginName();
        String pwd = loginParam.getPassword();

        User user = this.getUserByLoginName(loginName);
        if (user == null) {
            throw new UnknownAccountException("用户名或密码错误！");
        }

        String encodePwd = MD5Coder.MD5(pwd + user.getSalt());
        if (!encodePwd.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        return new LoginOutput(String.valueOf(user.getUserId()), user.getLoginName(), null);
    }

    /**
     * 根据登录名获得用户
     * @param loginName
     * @return
     */
    public User getUserByLoginName(String loginName) {
        return userRepository.selectByLoginName(loginName);
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    public Boolean delete(String userId) {

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

    public List<RoleOutput> getRolesByUserId(String userId) {
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(Long.parseLong(userId));
        List<UserRole> userRoles = userRoleRepository.selectByExample(example);

        if (!userRoles.isEmpty()) {
            List<Long> roleIds = userRoles.stream().map((ur) -> ur.getRoleId()).collect(Collectors.toList());

            RoleExample roleExample = new RoleExample();
            roleExample.createCriteria().andRoleIdIn(roleIds);
            List<Role> roles = roleRepository.selectByExample(roleExample);
            List<RoleOutput> roleOuts = BeanConvertUtil.mapList(roles, RoleOutput.class);
            return roleOuts;
        }
        return new ArrayList<>();
    }

    public List<MenuOutput> getMenesByUserId(String userId) {
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
                List<MenuOutput> menuOuts = BeanConvertUtil.mapList(menus, MenuOutput.class);
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
