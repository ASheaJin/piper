package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.User;
import com.syswin.pipeline.db.model.UserExample;
import com.syswin.pipeline.db.repository.UserRepository;
import com.syswin.pipeline.db.repository.UserRoleRepository;
import com.syswin.pipeline.manage.dto.RoleOut;
import com.syswin.pipeline.manage.dto.UserOut;
import com.syswin.pipeline.manage.dto.UserParam;
import com.syswin.pipeline.utils.BeanConvertUtil;
import com.syswin.pipeline.utils.MD5Coder;
import com.syswin.pipeline.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by 115477 on 2019/3/29.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

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
     * @return
     */
    public Boolean saveUser(UserParam userParam){
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

        } else {

        }

        return false;
    }

    /**
     * 重设密码
     */
     public Boolean resetPassword(String userId) {
     return false;
     }

     /**
     * 删除用户
     * @param userId
     * @return
     */
    public Boolean deleteUser(String userId) {
        return false;
    }

    /**
     * 修改用户可用状态
     * @param status
     * @return
     */
    public Boolean enableUser(int status) {
        return false;
    }

    public List<RoleOut> getRolesByUserId(String userId) {

        return null;
    }

    public Boolean saveRolesByUserId(String userId, List<String> roleIds) {
        return false;
    }
}
