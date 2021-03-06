/**
 * 
 */
package com.seabox.tagsys.sys.service;

import java.util.List;
import java.util.Map;

import com.seabox.tagsys.sys.entity.Menu;
import com.seabox.tagsys.sys.entity.PageBean;

/**
 * Description 
 * @author shibh
 * @create date 2015年12月26日
 * @version 0.0.1
 */
public interface MenuService {
	/**
	 * 
	 * Description 查询用户对应的菜单权限
	 * @param  
	 * @return List
	 * @throws
	 */
	List<Menu> findMenusByUsername(String username);

	/**
	 * Description
	 * @param  
	 * @return void
	 * @throws 
	 */
	List<Menu> findMenuList();

	/**
	 * Description
	 * @param  
	 * @return Menu
	 * @throws 
	 */
	Menu findMenuById(Integer id);

	/**
	 * @param menu_id2 
	 * Description 修改菜单状态
	 * @param  
	 * @return void
	 * @throws 
	 */
	void updateMenuState(@SuppressWarnings("rawtypes") Map map);

	/**
	 * Description 查找菜单分页
	 * @param  
	 * @return PageBean<Menu>
	 * @throws 
	 */
	PageBean<Menu> findMenuList(Integer currPage, Integer pageSize);
}
