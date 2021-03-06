package com.seabox.tagsys.camp.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * 活动管理service层
 * @author SongChaoqun
 * @since 2016-01-12
 *
 */
public interface CampManagerService {
	/**
	 * 新建活动保存
	 * @return
	 */
	boolean addSave(Map<String,Object> paramMap);
	
	boolean submitCamp(Map<String,Object> paramMap);

	String viewTestMsg(Map<String, Object> paramMap);

	List<Map<String,Object>> sendTestMsg(Map<String, Object> paramMap);
	
	int getCampId();

	/**
	 * 从短信接口取短信模板
	 * @param paramMap
	 * @return
	 */
	Map getSmsCont(Map<String, Object> paramMap);
}
