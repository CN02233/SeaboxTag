/**
 * 
 */
package com.seabox.tagsys.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seabox.tagsys.sys.entity.Menu;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.sys.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seabox.tagsys.base.db.RelationDbTemplate;
import com.seabox.tagsys.sys.service.RoleService;

/**
 * Description
 * @author shibh
 * @create date 2015年12月27日
 * @version 0.0.1
 */
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RelationDbTemplate relationDbTemplate;

	/* (non-Javadoc)
	 * @see com.seabox.eview.sys.service.RoleService#findUserRoles(java.lang.String)
	 */
	@Override
	public List<Role> findUserRoles(String username) {
		List<Role> roleList = relationDbTemplate.selectList("role.findUserRoles", username);
		return roleList;
	}

	/* (non-Javadoc)
	 * @see com.seabox.eview.sys.service.RoleService#findRoleList()
	 */
	@Override
	public List<Role> findRoleList() {
		List<Role> roleList=relationDbTemplate.selectList("role.findRoleList", null);
		if(roleList==null || roleList.size()==0){
			return null;
		}
		for(Role role:roleList){
			List<Menu> menuList=relationDbTemplate.selectList("menu.getMenuByRoleId", role.getRole_id());
			role.setMenuList(menuList);
		}
		return roleList;
	}

	/* (non-Javadoc)
	 * @see com.seabox.eview.sys.service.RoleService#findRoleListByPage()
	 */
	@Override
	public PageBean<Role> findRoleListByPage(Integer currPage, Integer pageSize) {
		PageBean<Role> pageBean = new PageBean<Role>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("beginIndex", (currPage-1)*pageSize);
		map.put("pageSize", pageSize);
		List<Role> list=relationDbTemplate.selectList("role.findRoleListByPage", map);
		pageBean.setList(list);
		pageBean.setCurrPage(currPage);
		pageBean.setPageSize(pageSize);
		Integer count = relationDbTemplate.selectOne("role.findTotalNum");
		pageBean.setTotalNum(count);
		return pageBean;
	}

}
