/**
 * 
 */
package com.seabox.tagsys.sys.controller;

import java.util.List;

import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.sys.entity.Role;
import com.seabox.tagsys.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description
 * @author Lenovo
 * @create date 2015年12月26日
 * @version 0.0.1
 */
@Controller
@RequestMapping("sys/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	@RequestMapping("main")
	public String roleManage(ModelMap modelMap){
		List<Role> roleList = roleService.findRoleList();
		modelMap.addAttribute(roleList);
		return "sys/role/roleManage";
	}
	@RequestMapping("findRoleList")
	@ResponseBody
	public PageBean<Role> findRoleList(Integer currPage, Integer pageSize, ModelAndView mv){
		PageBean<Role> pageBean = roleService.findRoleListByPage(currPage,pageSize);
		return pageBean;
	}
}
