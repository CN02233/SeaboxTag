package com.seabox.tagsys.sys.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SysLogModel {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int log_id;
	
	private String log_type_nm;
	
	private Date log_ts;
	
	private String str_log_ts;
	
	private String log_type_cd;
	
	private int user_id;
	
	private String user_real_nm;
	
	private String user_ip;

	public int getLog_id() {
		return log_id;
	}

	public void setLog_id(int log_id) {
		this.log_id = log_id;
	}

	public String getLog_type_nm() {
		return log_type_nm;
	}

	public void setLog_type_nm(String log_type_nm) {
		this.log_type_nm = log_type_nm;
	}

	public Date getLog_ts() {
		return log_ts;
	}

	public void setLog_ts(Date log_ts) {
		this.log_ts = log_ts;
	}

	public String getStr_log_ts() {
		if(log_ts != null){
			str_log_ts = dateFormat.format(log_ts);
		}
		return str_log_ts;
	}

	public void setStr_log_ts(String str_log_ts) {
		this.str_log_ts = str_log_ts;
	}

	public String getLog_type_cd() {
		return log_type_cd;
	}

	public void setLog_type_cd(String log_type_cd) {
		this.log_type_cd = log_type_cd;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_real_nm() {
		return user_real_nm;
	}

	public void setUser_real_nm(String user_real_nm) {
		this.user_real_nm = user_real_nm;
	}

	public String getUser_ip() {
		if(user_ip == null){
			user_ip = "";
		}
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	@Override
	public String toString() {
		return "LogModel [dateFormat=" + dateFormat + ", log_id=" + log_id + ", log_type_nm=" + log_type_nm
				+ ", log_ts=" + log_ts + ", str_log_ts=" + str_log_ts + ", log_type_cd=" + log_type_cd + ", user_id="
				+ user_id + ", user_ip=" + user_ip + "]";
	}
	
}
