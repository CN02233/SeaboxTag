/**
 * 
 */
package com.seabox.tagsys.sys.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.seabox.tagsys.sys.Constants;
import com.seabox.tagsys.sys.http.HttpClientUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seabox.tagsys.sys.entity.User;

public class HDIAUTHUserServiceImpl{
	
	private static Logger logger = LoggerFactory.getLogger(HDIAUTHUserServiceImpl.class);

	public Boolean login(String username, String password) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("username", username);
		reqMap.put("password", password);
		try {
			Map<String, Object> resMap = HttpClientUtil.postMethod(Constants.AUTHLOGIN_POST_URL, reqMap);
			String returnCode = (String) resMap.get("code");
			System.out.println(returnCode);
			if (returnCode != "20000") {
				return false;
			}
			User user = new User();
			user.setUser_real_nm(username);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("单点登录系统认证失败" + e);
			return false;
		}
		return true;
	}

	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		session.setAttribute("user", null);
		session.setAttribute("menuList", null);
		subject.logout();
		String token = (String) session.getAttribute("token");
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("token", token);
		try {
			HttpClientUtil.deleteMethod(Constants.AUTHLOGOUT_DELETE_URL, reqMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("单点登录系统登出失败" + e);
		}
	}

	public User findUserByUserId(int user_id) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("user_id", user_id);
		reqMap.put("tenant_id", Constants.TENANT_ID);
		try {
			Map<String, Object> permissionMap = HttpClientUtil.getMethod(Constants.GETPERMISSION_GET_URL, reqMap);
			if (permissionMap.get("code") != "20000") {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public User checkUserExist(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public String findUserListByPageNoAdmin(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
