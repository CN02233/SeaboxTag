/**
 * 
 */
package com.seabox.tagsys.sys.entity;


/**
 * Description
 * @author shibh
 * @create date 2015年12月26日
 * @version 0.0.1
 */
public class Menu {
	private int parent_menu_id;
	
	private String parent_menu_name;
	
	private int menu_id;

	private String menu_ch_name;
	
	private String menu_en_name;

	private String isable;

	private String menu_img_url;

	private String menu_level;


	private String menu_url;


	public int getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}

	public String getIsable() {
		return isable;
	}

	public void setIsable(String isable) {
		this.isable = isable;
	}

	public String getMenu_ch_name() {
		return menu_ch_name;
	}

	public void setMenu_ch_name(String menu_ch_name) {
		this.menu_ch_name = menu_ch_name;
	}

	public String getMenu_img_url() {
		return menu_img_url;
	}

	public void setMenu_img_url(String menu_img_url) {
		this.menu_img_url = menu_img_url;
	}

	public String getMenu_level() {
		return menu_level;
	}

	public void setMenu_level(String menu_level) {
		this.menu_level = menu_level;
	}



	public String getMenu_url() {
		return menu_url;
	}

	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}

	public int getParent_menu_id() {
		return parent_menu_id;
	}

	public void setParent_menu_id(int parent_menu_id) {
		this.parent_menu_id = parent_menu_id;
	}

	public String getParent_menu_name() {
		return parent_menu_name;
	}

	public void setParent_menu_name(String parent_menu_name) {
		this.parent_menu_name = parent_menu_name;
	}

	public String getMenu_en_name() {
		return menu_en_name;
	}

	public void setMenu_en_name(String menu_en_name) {
		this.menu_en_name = menu_en_name;
	}

	@Override
	public String toString() {
		return "Menu [parent_menu_id=" + parent_menu_id + ", parent_menu_name=" + parent_menu_name + ", menu_id="
				+ menu_id + ", menu_ch_name=" + menu_ch_name + ", menu_en_name=" + menu_en_name + ", isable=" + isable
				+ ", menu_img_url=" + menu_img_url + ", menu_level=" + menu_level + ", menu_url=" + menu_url + "]";
	}



	
}
