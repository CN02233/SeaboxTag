/**
 * 
 */
package com.seabox.tagsys.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.seabox.tagsys.sys.entity.Menu;
import com.seabox.tagsys.sys.entity.PageBean;
import org.springframework.stereotype.Service;

import com.seabox.tagsys.base.db.RelationDbTemplate;
import com.seabox.tagsys.sys.service.MenuService;

/**
 * Description
 * @author shibh
 * @create date 2015年12月26日
 * @version 0.0.1
 */
@Service
public class MenuServiceImpl implements MenuService {
	@Resource
	protected RelationDbTemplate relationDbTemplate;

	/* (non-Javadoc)
	 * @see com.seabox.eview.sys.service.MenuService#findMenusByUsername(java.lang.String)
	 */
	@Override
	public List<Menu> findMenusByUsername(String username) {
		List<Menu> list=relationDbTemplate.selectList("menu.findMenuByUsername", username);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.seabox.eview.sys.service.MenuService#findMenuList()
	 */
	@Override
	public List<Menu> findMenuList() {
		List<Menu> list = relationDbTemplate.selectList("menu.findMenuList", null);
		return list;
	}
	@Override
	public Menu findMenuById(Integer id){
		Menu menu=relationDbTemplate.selectOne("menu.findMenuById", id);
		return menu;
	}

	/* (non-Javadoc)
	 * @see com.seabox.eview.sys.service.MenuService#updateMenuState(java.lang.Integer, java.lang.String)
	 */
	@Override
	public void updateMenuState(@SuppressWarnings("rawtypes") Map map) {
		System.out.println("开始执行-=-=-=-=-");
		relationDbTemplate.update("menu.updateMenuState", map);
	}

	/* (non-Javadoc)
	 * @see com.seabox.eview.sys.service.MenuService#findMenuList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public PageBean<Menu> findMenuList(Integer currPage, Integer pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("beginIndex", (currPage-1)*pageSize);
		map.put("pageSize", pageSize);
		List<Menu> list=relationDbTemplate.selectList("menu.findMenuListByPage", map);
		PageBean<Menu> pageBean=new PageBean<Menu>();
		pageBean.setList(list);
		pageBean.setCurrPage(currPage);
		pageBean.setPageSize(pageSize);
		int count = relationDbTemplate.selectOne("menu.findTotalNum");
		pageBean.setTotalNum(count);
		return pageBean;
	}


}
;