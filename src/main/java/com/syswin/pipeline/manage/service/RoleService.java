package com.syswin.pipeline.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syswin.pipeline.db.model.*;
import com.syswin.pipeline.db.repository.MenuRepository;
import com.syswin.pipeline.db.repository.RoleMenuRepository;
import com.syswin.pipeline.db.repository.RoleRepository;
import com.syswin.pipeline.db.repository.UserRoleRepository;
import com.syswin.pipeline.manage.dto.MenuOutput;
import com.syswin.pipeline.manage.dto.RoleInput;
import com.syswin.pipeline.manage.dto.RoleOutput;
import com.syswin.sub.api.utils.BeanConvertUtil;
import com.syswin.sub.api.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
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
	 *
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PageInfo<RoleOutput> list(int pageIndex, int pageSize) {
		pageIndex = pageIndex < 1 ? 1 : pageIndex;
		pageSize = pageSize <= 30 && pageSize >= 1 ? pageSize : 30;

		RoleExample roleExample = new RoleExample();
		RoleExample.Criteria criteria = roleExample.createCriteria();
		criteria.andStatusEqualTo(Byte.valueOf("1"));

		PageHelper.startPage(pageIndex, pageSize);
		List<Role> roleList = this.roleRepository.selectByExample(roleExample);
		List<RoleOutput> roleOuts = BeanConvertUtil.mapList(roleList, RoleOutput.class);

		PageInfo pageInfo = new PageInfo(roleList);
		pageInfo.setList(roleOuts);

		return pageInfo;
	}

	public Boolean save(RoleInput param) {
		String roleId = param.getRoleId();
		if (StringUtils.isEmpty(roleId)) {
			Role role = BeanConvertUtil.map(param, Role.class);
			role.setRoleId(SnowflakeIdWorker.getInstance().nextId());
			role.setStatus(Byte.valueOf("1"));
			roleRepository.insertSelective(role);
		} else {
			Role role = new Role();
			role.setRoleId(Long.parseLong(roleId));
			role.setRoleName(param.getRoleName());
			role.setRemark(param.getRemark());
			int c = roleRepository.updateByPrimaryKeySelective(role);
			if (c == 0) {
				throw new RuntimeException("角色id错误 " + roleId);
			}
		}
		return true;
	}


	public List<MenuOutput> getMenuesByRoleId(String roleId) {
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(Long.parseLong(roleId));
		List<RoleMenu> roleMenus = roleMenuRepository.selectByExample(example);

		if (!roleMenus.isEmpty()) {
			List<Long> menuIds = roleMenus.stream().map((ur) -> ur.getMenuId()).collect(Collectors.toList());

			MenuExample menuExample = new MenuExample();
			menuExample.createCriteria().andMenuIdIn(menuIds);
			List<Menu> menus = menuRepository.selectByExample(menuExample);
			List<MenuOutput> menuOuts = BeanConvertUtil.mapList(menus, MenuOutput.class);
			return menuOuts;
		}
		return new ArrayList<>();
	}

	@Transactional
	public Boolean saveMenuesByRoleId(String roleId, List<String> menuIds) {
		roleMenuRepository.deleteByRoleId(Long.parseLong(roleId));

		//页面只传过来叶子节点的id，这里重新获得父节点id
		Map<Long, Menu> parentIds = menuRepository.selectParentIds();

		Set<Long> existsPids = new HashSet<>();
		for (String menuId : menuIds) {
			Long pid = parentIds.get(Long.parseLong(menuId)).getParentId();
			if (pid != null && !existsPids.contains(pid)) {
				RoleMenu prm = new RoleMenu();
				prm.setMenuId(pid);
				prm.setRoleId(Long.parseLong(roleId));

				roleMenuRepository.insert(prm);
				existsPids.add(pid);
			}

			RoleMenu rm = new RoleMenu();
			rm.setMenuId(Long.parseLong(menuId));
			rm.setRoleId(Long.parseLong(roleId));
			roleMenuRepository.insert(rm);
		}
		return true;
	}

	@Transactional
	public Boolean delete(String roleId) {
		Long rid = Long.parseLong(roleId);

		roleRepository.deleteByPrimaryKey(rid);

		userRoleRepository.deleteByRoleId(rid);

		roleMenuRepository.deleteByRoleId(rid);

		return true;
	}
}
