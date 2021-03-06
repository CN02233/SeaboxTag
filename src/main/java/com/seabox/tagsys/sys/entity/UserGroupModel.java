package com.seabox.tagsys.sys.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserGroupModel {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int group_id;
	
	private String group_nm;
	
	private String group_org;
	
	private String group_dep;
	
	private String twoUserNames;
	
	private String allUserNames;
	
	private List<User> userList = new ArrayList<User> ();
	
	private String active_ind;
	
	private int create_id;
	
	private String create_name;
	
	private Date create_ts;
	
	private String str_create_time;
	
	private int update_id;
	
	private String update_name;
	
	private Date update_ts;
	
	private String str_update_time;
	
	private int camp_admin_id;
	
	private String camp_admin_name;

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		group_nm = group_org + "_" + group_dep;
		if(group_nm == null){
			group_nm = "";
		}
		return group_nm;
	}

	public void setGroup_name(String group_name) {
		this.group_nm = group_name;
	}

	public String getGroup_org() {
		return group_org;
	}

	public void setGroup_org(String group_org) {
		this.group_org = group_org;
	}

	public String getGroup_dep() {
		return group_dep;
	}

	public void setGroup_dep(String group_dep) {
		this.group_dep = group_dep;
	}

	public String getTwoUserNames() {
		if(twoUserNames == null){
			twoUserNames = "";
		}
		return twoUserNames;
	}

	public void setTwoUserNames(String twoUserNames) {
		this.twoUserNames = twoUserNames;
	}

	public String getAllUserNames() {
		if(allUserNames == null){
			allUserNames = "";
		}
		return allUserNames;
	}

	public void setAllUserNames(String allUserNames) {
		this.allUserNames = allUserNames;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String getActive_ind() {
		return active_ind;
	}

	public void setActive_ind(String active_ind) {
		this.active_ind = active_ind;
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

	public Date getCreate_time() {
		return create_ts;
	}

	public void setCreate_time(Date create_time) {
		this.create_ts = create_time;
	}

	public String getStr_create_time() {
		if(create_ts != null){
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

	public Date getUpdate_time() {
		return update_ts;
	}

	public void setUpdate_time(Date update_time) {
		this.update_ts = update_time;
	}

	public String getStr_update_time() {
		if(update_ts != null){
			str_update_time = dateFormat.format(update_ts);
		}
		return str_update_time;
	}

	public void setStr_update_time(String str_update_time) {
		this.str_update_time = str_update_time;
	}

	public int getcamp_admin_id() {
		return camp_admin_id;
	}

	public void setcamp_admin_id(int camp_admin_id) {
		this.camp_admin_id = camp_admin_id;
	}

	public String getCamp_admin_name() {
		if(camp_admin_name == null){
			camp_admin_name = "";
		}
		return camp_admin_name;
	}

	public void setCamp_admin_name(String camp_admin_name) {
		this.camp_admin_name = camp_admin_name;
	}

	@Override
	public String toString() {
		return "Group [group_id=" + group_id + ", group_name=" + group_nm + ", group_org=" + group_org
				+ ", group_dep=" + group_dep + ", twoUserNames=" + twoUserNames + ", allUserNames=" + allUserNames
				+ ", userList=" + userList + ", active_ind=" + active_ind + ", create_id=" + create_id + ", create_name="
				+ create_name + ", create_time=" + create_ts + ", str_create_time=" + str_create_time + ", update_id="
				+ update_id + ", update_name=" + update_name + ", update_time=" + update_ts + ", str_update_time="
				+ str_update_time + ", camp_admin_id=" + camp_admin_id + ", camp_admin_name=" + camp_admin_name
				+ "]";
	}
	
}
