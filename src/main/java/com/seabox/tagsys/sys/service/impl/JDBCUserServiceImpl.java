/**
 * 
 */
package com.seabox.tagsys.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.sys.entity.Menu;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.sys.entity.SysLogModel;
import com.seabox.tagsys.sys.service.SysLogService;
import com.seabox.tagsys.sys.service.UserService;
import com.seabox.tagsys.sys.util.LogTypeCd;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seabox.tagsys.sys.BaseException;
import com.seabox.tagsys.sys.entity.User;

@Service
public class JDBCUserServiceImpl extends BaseService implements UserService {
	
	private Map<String,String> realOrderColum = new HashMap<String,String>();
	
	@Autowired
	SysLogService sysLogService;
	
	@Override
	public User checkUserExist(String username, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		User user = relationDbTemplate.selectOne("user.checkUserExist", map);
		if (user == null) {
			return null;
		}
		return user;
	}
	
	@Override
	public User findUserByUserId(int user_id) {
		User user = relationDbTemplate.selectOne("user.findUserByUserId", user_id);
		if (user == null) {
			throw new BaseException("用户不存在");
		}
		// roleList=relationDbTemplate.selectList("role.findUserRoles",user_id);
		int role_id = user.getRole_id();
		List<Menu> menuList = relationDbTemplate.selectList("menu.getMenuByRoleId", role_id);
		// user.setRoleList(roleList);
		user.setMenuList(menuList);
		return user;
	}

	@Override
	public Boolean login(String username, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		User user = relationDbTemplate.selectOne("user.checkUserExist", map);
		if (user == null) {
			return false;
		}
		user = this.findUserByUserId(user.getUser_id());
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("user", user);
		return true;
	}

	@Override
	public void logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		User user = (User) session.getAttribute("user");
		if(user != null){
			String user_ip = request.getRemoteAddr();
			SysLogModel sysLogModel = new SysLogModel();
			sysLogModel.setLog_type_cd(LogTypeCd.SIGN_OUT);
			sysLogModel.setUser_id(user.getUser_id());
			sysLogModel.setUser_ip(user_ip);
			sysLogService.addSysLog(sysLogModel);
		}
		session.setAttribute("user", null);
		session.setAttribute("menuList", null);
		subject.logout();
	}

	@Override
	public String findUserListByPage(Map<String, Object> paramMap) {
		paramMap.put("find_sys_admin", 1);
		PageBean<User> pageBean = relationDbTemplate.selectListForPage("user.findUserListByPage", "user.findTotalNum",paramMap);
		String json = objectToJson(pageBean);
		return json;
	}

	@Override
	public String findUserListByPageNoAdmin(Map<String, Object> paramMap) {
		String order_id = (String) paramMap.get("order_id");
		String orderId = realOrderColum(order_id);
		paramMap.put("orderId", orderId);
		PageBean<User> pageBean = relationDbTemplate.selectListForPage("user.findUserListByPage", "user.findTotalNum",paramMap);
		String json = objectToJson(pageBean);
		return json;
	}

	@Override
	public void modifyUserActiveInd(int user_id, String active_ind) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("active_ind", active_ind);
		paramMap.put("user_id", user_id);
		relationDbTemplate.update("user.modifyUserActiveInd", paramMap);
	}

	@Override
	public void saveOneUser(Map<String, Object> paramMap) {
		relationDbTemplate.insert("user.saveOneUser", paramMap);
	}

	@Override
	public int checkUserNameExist(Map<String, Object> paramMap) {
		return relationDbTemplate.selectOne("user.checkUserNameExist", paramMap);
	}

	@Override
	public void deleteUserById(Map<String, Object> paramMap) {
		relationDbTemplate.delete("user.deleteUserById", paramMap);
	}

	@Override
	public void modifyUserInfo(Map<String, Object> paramMap) {
		relationDbTemplate.update("user.modifyUserInfo", paramMap);
	}

	@Override
	public List<User> findAllUser(Map<String, Object> paramMap) {
		List<User> userList = relationDbTemplate.selectList("user.findAllUser");
		return userList;
	}

	@Override
	public List<User> findUserListByGroupId(Map<String, Object> paramMap) {
		List<User> userList = relationDbTemplate.selectList("user.findUserListByGroupId", paramMap);
		return userList;
	}

	@Override
	public int findTotalNumByUserGroupId(Map<String, Object> paramMap) {
		return relationDbTemplate.selectOne("user.findTotalNumByUserGroupId", paramMap);
	}

	@Override
	public void modifyUserPwd(int user_id, String user_pwd) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_pwd", user_pwd);
		paramMap.put("user_id", user_id);
		relationDbTemplate.update("user.modifyUserPwd", paramMap);
	}

	@Override
	public void modifyUserRole(int user_id, int role_id) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_id", user_id);
		paramMap.put("role_id", role_id);
		relationDbTemplate.update("user.modifyUserRole", paramMap);
	}

	@Override
	public User modifyUserProperties(HttpServletRequest request, User user) {
		String sign_in_ip = request.getRemoteAddr();
		user.setSign_in_ip(sign_in_ip);
		int role_id = user.getRole_id();
		List<Menu> menuList = relationDbTemplate.selectList("menu.getMenuByRoleId", role_id);
		user.setMenuList(menuList);
		return user;
	}
	
	private String realOrderColum(String orderId) {
		if (realOrderColum.size() < 1) {
			realOrderColum.put("user_id", "u.user_id");
			realOrderColum.put("user_nm", "u.user_nm");
			realOrderColum.put("group_name", "u.user_group_id");
			realOrderColum.put("user_real_nm", "u.user_real_nm");
			realOrderColum.put("create_name", "create_name");
			realOrderColum.put("create_ts", "u.create_ts");
			realOrderColum.put("update_name", "update_name");
			realOrderColum.put("update_ts", "u.update_ts");
		}
		return realOrderColum.get(orderId);
	}

	@Override
	public void modifyUsersGroupAndRole(int user_group_id, int role_id, List<Integer> user_id_list) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user_group_id", user_group_id);
		paramMap.put("role_id", role_id);
		paramMap.put("user_id_list", user_id_list);
		relationDbTemplate.update("user.modifyUsersGroupAndRole", paramMap);
	}

}
 