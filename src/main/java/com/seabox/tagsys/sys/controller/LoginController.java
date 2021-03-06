package com.seabox.tagsys.sys.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seabox.tagsys.sys.Constants;
import com.seabox.tagsys.sys.entity.UserGroupModel;
import com.seabox.tagsys.sys.service.GroupService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seabox.base.utils.CaptchaUtil;
import com.seabox.tagsys.sys.entity.SysLogModel;
import com.seabox.tagsys.sys.entity.User;
import com.seabox.tagsys.sys.service.SysLogService;
import com.seabox.tagsys.sys.service.UserService;
import com.seabox.tagsys.sys.util.ErrorMsg;
import com.seabox.tagsys.sys.util.GroupActiveInd;
import com.seabox.tagsys.sys.util.LogTypeCd;
import com.seabox.tagsys.sys.util.Regex;
import com.seabox.tagsys.sys.util.RoleID;
import com.seabox.tagsys.sys.util.UserActiveInd;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Description : 登录相关
 * @author : xiangwei
 * @date : 2016年1月16日
 */
@Controller
@RequestMapping("sys")
public class LoginController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	private ImageCaptchaService captchaService;
	
	@Autowired
	private SysLogService sysLogService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
	public LoginController() {
		if (captchaService == null) {
			//jcaptcha安全框架生成验证码
			captchaService = CaptchaUtil.captchaService();
		}
	}

	/**
	 * Description 校验用户名密码 
	 * @return String 
	 * @throws
	 */
	@RequestMapping("login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		logger.debug("user login begin:" + username);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//验证用户名密码是否正确
			/*
			 * if("".equals(captcha)||captcha==null||!checkCaptcha(captcha,
			 * request)){ map.put("result", "errorcaptcha"); map.put("msg",
			 * "验证码错误"); return map; }
			 */
			if (username == null || password == null || "".equals(username) || "".equals(password)) {
				map.put("result", "errorpwd");
				map.put("msg", "用户名或密码不能为空");
				return map;
			}
			User user = userService.checkUserExist(username, password);
			if(user == null ){
				map.put("result", "errorpwd");
				map.put("msg", "用户名或密码错误，请重新输入");
			} else {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("group_id", user.getUser_group_id());
				UserGroupModel userGroupModel = groupService.findGroupById(paramMap);
				if (UserActiveInd.DISABLE.equals(user.getActive_ind())){
					map.put("result", "errorpwd");
					map.put("msg", "用户被禁用");
				} else if (user.getRole_id() != RoleID.SYS_ADMIN && user.getUser_group_id() == 0){
					map.put("result", "errorpwd");
					map.put("msg", "用户未分配到用户组，不能进行登录");
				} else if (userGroupModel != null && GroupActiveInd.DISABLE.equals(userGroupModel.getActive_ind())) {
					map.put("result", "errorpwd");
					map.put("msg", "所在用户组被禁用，不能进行登录");
				} else {
					map.put("result", "success");
					map.put("msg", "登录成功");
					user = userService.modifyUserProperties(request, user);
					Session session = SecurityUtils.getSubject().getSession();
					session.setAttribute("user", user);
					SysLogModel sysLogModel = new SysLogModel();
					sysLogModel.setLog_type_cd(LogTypeCd.SIGN_IN);
					sysLogModel.setUser_id(user.getUser_id());
					sysLogModel.setUser_ip(user.getSign_in_ip());
					sysLogService.addSysLog(sysLogModel);
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("user login fail .." + username, e);
			map.put("result", "fail");
			map.put("msg", "登录失败");
		}
		logger.debug("user login success .." + username);
		return map;
	}

	/**
	 * Description 登出
	 * @return String 登出后跳转至登录页面
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request) {
		try {
			userService.logout(request);
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute("user", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("user logout fail ..", e);
		}
		logger.debug("user logout success..");
		return "sys/login";
	}
	
	/**
	 * Description 跳转至登录页面
	 * @return String
	 */
	@RequestMapping("toLogin")
	public String toLogin() {
		return "sys/login";
	}
	
	@RequestMapping("toLogout")
	public String toLogout() {
		return "sys/logout";
	}

	/**
	 * Description 跳转至首页
	 *  @param 
	 *  @return String @throws
	 */
	@RequestMapping("mainframe")
	public String main(ModelMap modelMap) {
		User user = null;
		try {
			//将用户菜单列表传入页面
			user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			if (user == null) {
				return "WEB-INF/jsp/sys/login";
			}
			modelMap.addAttribute("menuList", user.getMenuList());
		} catch (Exception e) {
			logger.debug("not login and turn to login.jsp", e);
			return "WEB-INF/jsp/sys/login";
		}
		logger.debug("login success and redirect to main.jsp");
		return "main";
	}

	/**
	 * Description 游客登陆
	 * @param 
	 * @return String 
	 * @throws
	 */
	@RequestMapping("guestMain")
	public String guestMain() {
		return "main";
	}
	
	/**
	 * Description 获取页面验证码
	 * @param 
	 * @return void 
	 * @throws
	 */
	@RequestMapping("captcha")
	public void getCaptcha(String resId, HttpServletRequest req, HttpServletResponse response) {
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		BufferedImage c = captchaService.getImageChallengeForID(req.getSession().getId(), req.getLocale());
		try {
			ImageIO.write(c, Constants.CAPTCHA_IMAGE_FORMAT, jpegOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		// flush it in the response
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/" + Constants.CAPTCHA_IMAGE_FORMAT);
		ServletOutputStream responseOutputStream;
		try {
			responseOutputStream = response.getOutputStream();
			responseOutputStream.write(captchaChallengeAsJpeg);
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Description 修改密码
	 * @param request
	 * @return Map<String,Object>
	 * throws
	 */
	@RequestMapping("modifyUserPwd")
	@ResponseBody
	public Map<String, Object> modifyUserPwd(HttpServletRequest request) {
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		String repeatpwd = request.getParameter("repeatpwd");
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (user == null) {
			map.put("result", "fail");
			map.put("msg", ErrorMsg.LOGIN_OVERDUE);
			return map;
		} 
		User oldPwduser = userService.checkUserExist(user.getUser_nm(), oldpwd);
		if (oldPwduser == null) {
			map.put("result", "fail");
			map.put("msg", "旧密码输入错误");
			return map;
		} 
		Pattern pattern = Pattern.compile(Regex.PASSWORD, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(newpwd);
		if(!matcher.matches()){
			map.put("result", "fail");
			map.put("msg", ErrorMsg.PASSWORD_ERROR_MSG);
			return map;
		}
		if(!newpwd.equals(repeatpwd)){
			map.put("result", "fail");
			map.put("msg", ErrorMsg.TWO_PWD_DIF);
			return map;
		}
		userService.modifyUserPwd(user.getUser_id(), newpwd);
		SysLogModel sysLogModel = new SysLogModel();
		sysLogModel.setLog_type_cd(LogTypeCd.CHANGE_PASSWORD);
		sysLogModel.setUser_id(user.getUser_id());
		sysLogModel.setUser_ip(user.getSign_in_ip());
		sysLogService.addSysLog(sysLogModel);
		map.put("result", "success");
		map.put("msg", "密码修改成功");
		return map;
	}
	
	@RequestMapping("goModifyPassword")
	public String goModifyPassword(HttpServletRequest request, HttpServletResponse response) {
		return "sys/userPwdModify";
	}
	
	@RequestMapping("resetPwd")
	public String resetPwd(HttpServletRequest request, HttpServletResponse response) {
		return "sys/resetPwd";
	}

}
