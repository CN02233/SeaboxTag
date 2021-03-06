/**
 * 
 */
package com.seabox.tagsys.sys.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class User {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private int user_id;

	private String user_nm;

	private String user_real_nm;

	private String user_pwd;

	private String active_ind;

	private int superior_id;

	private String sign_in_ip;

	private int user_group_id;

	private String group_name;

	private int role_id;

	private int create_id;

	private String create_name;

	private Date create_ts;

	private String str_create_time;

	private int update_id;

	private String update_name;

	private Date update_ts;

	private String str_update_time;

	private List<Menu> menuList;

	private List<Role> roleList;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_nm() {
		if (user_nm == null) {
			user_nm = "";
		}
		return user_nm;
	}

	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}

	public String getUser_real_nm() {
		return user_real_nm;
	}

	public void setUser_real_nm(String user_real_nm) {
		this.user_real_nm = user_real_nm;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getActive_ind() {
		return active_ind;
	}

	public void setActive_ind(String active_ind) {
		this.active_ind = active_ind;
	}

	public int getSuperior_id() {
		return superior_id;
	}

	public void setSuperior_id(int superior_id) {
		this.superior_id = superior_id;
	}

	public String getSign_in_ip() {
		return sign_in_ip;
	}

	public void setSign_in_ip(String sign_in_ip) {
		this.sign_in_ip = sign_in_ip;
	}

	public int getUser_group_id() {
		return user_group_id;
	}

	public void setUser_group_id(int user_group_id) {
		this.user_group_id = user_group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public int getCreate_id() {
		return create_id;
	}

	public void setCreate_id(int create_id) {
		this.create_id = create_id;
	}

	public String getCreate_name() {
		return create_name;
	}

	public void setCreate_name(String create_name) {
		this.create_name = create_name;
	}

	public Date getCreate_ts() {
		return create_ts;
	}

	public void setCreate_ts(Date create_ts) {
		this.create_ts = create_ts;
	}

	public String getStr_create_time() {
		if (create_ts != null) {
			str_create_time = dateFormat.format(create_ts);
		}
		return str_create_time;
	}

	public void setStr_create_time(String str_create_time) {
		this.str_create_time = str_create_time;
	}

	public int getUpdate_id() {
		return update_id;
	}

	public void setUpdate_id(int update_id) {
		this.update_id = update_id;
	}

	public String getUpdate_name() {
		return update_name;
	}

	public void setUpdate_name(String update_name) {
		this.update_name = update_name;
	}

	public Date getUpdate_ts() {
		return update_ts;
	}

	public void setUpdate_ts(Date update_ts) {
		this.update_ts = update_ts;
	}

	public String getStr_update_time() {
		if (update_ts != null) {
			str_update_time = dateFormat.format(update_ts);
		}
		return str_update_time;
	}

	public void setStr_update_time(String str_update_time) {
		this.str_update_time = str_update_time;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_nm=" + user_nm + ", user_real_nm=" + user_real_nm + ", user_pwd="
				+ user_pwd + ", active_ind=" + active_ind + ", superior_id=" + superior_id + ", sign_in_ip="
				+ sign_in_ip + ", user_group_id=" + user_group_id + ", group_name=" + group_name + ", role_id="
				+ role_id + ", create_id=" + create_id + ", create_name=" + create_name + ", create_ts=" + create_ts
				+ ", str_create_time=" + str_create_time + ", update_id=" + update_id + ", update_name=" + update_name
				+ ", update_ts=" + update_ts + ", str_update_time=" + str_update_time + ", menuList=" + menuList
				+ ", roleList=" + roleList + "]";
	}

}
