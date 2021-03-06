/**
 * 
 */
package com.seabox.tagsys.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seabox.tagsys.sys.entity.Menu;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description
 * 
 * @author Lenovo
 * @create date 2015年12月26日
 * @version 0.0.1
 */
@Controller
@RequestMapping("sys/menu")
public class MenuController {
	@Autowired
	private MenuService menuService;

	@RequestMapping("main")
	public String menuManage(ModelMap modelMap) {
		List<Menu> menuList = menuService.findMenuList();
		modelMap.addAttribute(menuList);
		return "sys/menu/menuManage";
	}

	@RequestMapping("updateMenuState")
	@ResponseBody
	public Map<String, Object> updateMenuState(Integer parent_menu_id, Integer menu_id, String isable) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parent_menu_id", parent_menu_id);
		paramMap.put("menu_id", menu_id);
		paramMap.put("isable", isable);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			menuService.updateMenuState(paramMap);
			System.out.println("-----------this is MenuController.updateMenuState---------------");
			map.put("result", "success");
			map.put("msg", "操作成功");
			return map;
		} catch (Exception e) {
			map.put("result", "error");
			map.put("msg", "操作失败");
			e.printStackTrace();
			return map;
		}
	}

	@RequestMapping("findMenuList")
	@ResponseBody
	public PageBean<Menu> findMenuList(Integer currPage, Integer pageSize, ModelAndView mv) {
		PageBean<Menu> pageBean = menuService.findMenuList(currPage, pageSize);
		System.out.println("+++++++++pagebean++++++++++");
		return pageBean;
	}

}
