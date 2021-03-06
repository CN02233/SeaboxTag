/**
 * 
 */
package com.seabox.tagsys.sys.entity;

import java.util.List;

/**
 * Description
 * @author shibh
 * @create date 2015年12月26日
 * @version 0.0.1
 */
public class Role {

	private int role_id;

	private String role_desc;

	private String role_name;
	
	private List<Menu> menuList;
	
	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	
	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	@Override
	public String toString() {
		return "Role [role_id=" + role_id + ", role_desc=" + role_desc + ", role_name=" + role_name + "]";
	}
	
	
}
