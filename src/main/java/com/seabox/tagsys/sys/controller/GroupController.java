package com.seabox.tagsys.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.sys.entity.SysLogModel;
import com.seabox.tagsys.sys.entity.User;
import com.seabox.tagsys.sys.entity.UserGroupModel;
import com.seabox.tagsys.sys.service.GroupService;
import com.seabox.tagsys.sys.service.SysLogService;
import com.seabox.tagsys.sys.service.UserService;
import com.seabox.tagsys.sys.util.ErrorMsg;
import com.seabox.tagsys.sys.util.GroupActiveInd;
import com.seabox.tagsys.sys.util.LogTypeCd;
import com.seabox.tagsys.sys.util.Regex;
import com.seabox.tagsys.sys.util.RoleID;

/**
 * <pre>
 * Description : 用户组管理
 * </pre>
 * @author : xiangwei
 * @date : 2016年1月19日
 * @version : 1.0
 */
@Controller
@RequestMapping("sys/group")
public class GroupController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(GroupController.class);
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private SysLogService sysLogService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("main")
	public String groupMain(HttpServletRequest request) {
		String strCurrPage = request.getParameter("currPage");
		int currPage = 1;
		if(strCurrPage != null){
			currPage = Integer.parseInt(strCurrPage);
		}
		request.setAttribute("currPage", currPage);
		String order_id = request.getParameter("order_id");
		if(order_id == null){
			order_id = "group_id";
		}
		String order_type = request.getParameter("order_type");
		if(order_type == null){
			order_type = "asc";
		}
		request.setAttribute("order_id", order_id);
		request.setAttribute("order_type", order_type);
		return "sys/group/groupMain";
	}

	@RequestMapping("findGroupListByPage")
	@ResponseBody
	public String findGroupListByPage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("request.getParameter(\"currPage\"):" + request.getParameter("currPage"));
		Map<String,Object> paramMap = initRequestParam(request);
		String resultJson = groupService.findGroupListByPage(paramMap);
		return resultJson;
	}
	
	@RequestMapping("groupAddEdit")
	public String groupAddEdit(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		request.setAttribute("action", action);
		String strCurrPage = request.getParameter("currPage");
		int currPage = 1;
		if(strCurrPage != null ){
			currPage = Integer.parseInt(strCurrPage);
		}
		request.setAttribute("currPage", currPage);
		String order_id = request.getParameter("order_id");
		if(order_id == null){
			order_id = "group_id";
		}
		String order_type = request.getParameter("order_type");
		if(order_type == null){
			order_type = "asc";
		}
		request.setAttribute("order_id", order_id);
		request.setAttribute("order_type", order_type);
		if("edit".equals(action)){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			int edit_group_id = Integer.parseInt(request.getParameter("edit_group_id"));
			paramMap.put("group_id", edit_group_id);
			List<User> userList = userService.findUserListByGroupId(paramMap);
			request.setAttribute("userList", userList);
			UserGroupModel editGroup = groupService.findGroupById(paramMap);
			request.setAttribute("editGroup", editGroup);
		} 
		return "sys/group/groupAddEdit";
	}
	
	@RequestMapping("saveOneGroup")
	@ResponseBody
	public Map<String, Object> saveOneGroup(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (user == null) {
			map.put("result", "fail");
			map.put("msg", "请重新登录");
			return map;
		}
		String action = request.getParameter("action");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String group_org = request.getParameter("group_org");
		String group_dep = request.getParameter("group_dep");
		int camp_admin_id = Integer.parseInt(request.getParameter("camp_admin_id"));
		Pattern pattern = Pattern.compile(Regex.GROUP_ORG_NAME, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(group_org);
		if(!matcher.matches()){
			map.put("result", "fail");
			map.put("msg", ErrorMsg.ORG_ERROR_MSG);
			return map;
		}
		pattern = Pattern.compile(Regex.GROUP_DEP_NAME, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(group_dep);
		if(!matcher.matches()){
			map.put("result", "fail");
			map.put("msg", ErrorMsg.DEP_ERROR_MSG);
			return map;
		}
		paramMap.put("group_org", group_org);
		paramMap.put("group_dep", group_dep);
		paramMap.put("camp_admin_id", camp_admin_id);
		paramMap.put("active_ind", GroupActiveInd.ENABLE);
		paramMap.put("create_id", user.getUser_id());
		paramMap.put("update_id", user.getUser_id());
		SysLogModel sysLogModel = new SysLogModel();
		sysLogModel.setUser_id(user.getUser_id());
		sysLogModel.setUser_ip(user.getSign_in_ip());
		if("add".equals(action)){
			UserGroupModel userGroupModel = groupService.findGroupByOrgAndDep(group_org, group_dep);
			if(userGroupModel != null){
				map.put("result", "fail");
				map.put("msg", "用户组已存在");
				return map;
			}
			groupService.saveOneGroup(paramMap);
			sysLogModel.setLog_type_cd(LogTypeCd.CREATE_USER_GROUP);
			sysLogService.addSysLog(sysLogModel);
		} else if("edit".equals(action)){
			int edit_group_id = 0;
			String strEditGroupId = request.getParameter("edit_group_id");
			if(strEditGroupId != null){
				edit_group_id = Integer.parseInt(strEditGroupId);
				paramMap.put("edit_group_id", edit_group_id);
			}
			UserGroupModel userGroupModel = groupService.findGroupByOrgAndDepAndOtherGroupId(edit_group_id, group_org, group_dep);
			if(userGroupModel != null){
				map.put("result", "fail");
				map.put("msg", "用户组已存在");
				return map;
			}
			String strPreAdmin = request.getParameter("preadmin");
			int preAdminId = 0;
			if(strPreAdmin != null){
				preAdminId = Integer.parseInt(strPreAdmin);
			}
			/**
			 * 更改用户组管理员:
			 * 1.修改新管理员的role为活动管理员
			 * 2.修改原管理员的role为活动经理
			 */
			if(preAdminId != camp_admin_id){
				userService.modifyUserRole(camp_admin_id, RoleID.CAMP_ADMIN);
				userService.modifyUserRole(preAdminId, RoleID.CAMP_MANAGER);
			}
			groupService.modifyGroupInfo(paramMap);
			sysLogModel.setLog_type_cd(LogTypeCd.EDIT_USER_GROUP);
			sysLogService.addSysLog(sysLogModel);
		}
		map.put("result", "success");
		map.put("msg", "保存成功");
		return map;
	}
	
	@RequestMapping("modifyGroupActiveInd")
	@ResponseBody
	public String modifyGroupActiveInd(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String strCurrPage = request.getParameter("currPage");
		int currPage = 1;
		if(strCurrPage != null ){
			currPage = Integer.parseInt(strCurrPage);
		}
		request.setAttribute("currPage", currPage);
		String order_id = request.getParameter("order_id");
		if(order_id == null){
			order_id = "group_id";
		}
		String order_type = request.getParameter("order_type");
		if(order_type == null){
			order_type = "asc";
		}
		request.setAttribute("order_id", order_id);
		request.setAttribute("order_type", order_type);
		String active_ind = request.getParameter("active_ind");
		paramMap.put("active_ind", active_ind);
		paramMap.put("group_id", Integer.parseInt(request.getParameter("group_id")));
		groupService.modifyGroupActiveInd(paramMap);
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (user != null) {
			SysLogModel sysLogModel = new SysLogModel();
			sysLogModel.setUser_id(user.getUser_id());
			sysLogModel.setUser_ip(user.getSign_in_ip());
			if (GroupActiveInd.DISABLE.equals(active_ind)) {
				sysLogModel.setLog_type_cd(LogTypeCd.DISABLE_USER_GROUP);
				sysLogService.addSysLog(sysLogModel);
			} else if (GroupActiveInd.ENABLE.equals(active_ind)) {
				sysLogModel.setLog_type_cd(LogTypeCd.ACTIVATION_USER_GROUP);
				sysLogService.addSysLog(sysLogModel);
			}
		}
		return "success";
	}
	
	@RequestMapping("deleteGroupById")
	@ResponseBody
	public String deleteGroupById(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		if (user == null) {
			return "sys/login";
		} 
		if(user.getRole_id() != RoleID.SYS_ADMIN){
			return "无权删除用户组";
		}
		String strCurrPage = request.getParameter("currPage");
		int currPage = 1;
		if(strCurrPage != null ){
			currPage = Integer.parseInt(strCurrPage);
		}
		request.setAttribute("currPage", currPage);
		String order_id = request.getParameter("order_id");
		if(order_id == null){
			order_id = "group_id";
		}
		String order_type = request.getParameter("order_type");
		if(order_type == null){
			order_type = "asc";
		}
		request.setAttribute("order_id", order_id);
		request.setAttribute("order_type", order_type);
		int delete_group_id = Integer.parseInt(request.getParameter("delete_group_id"));
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("delete_group_id", delete_group_id);
		groupService.deleteGroupById(paramMap);
		SysLogModel sysLogModel = new SysLogModel();
		sysLogModel.setLog_type_cd(LogTypeCd.DELETE_USER_GROUP);
		sysLogModel.setUser_id(user.getUser_id());
		sysLogModel.setUser_ip(user.getSign_in_ip());
		sysLogService.addSysLog(sysLogModel);
		return "success";
	}
	
}
