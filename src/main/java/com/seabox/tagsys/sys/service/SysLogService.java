package com.seabox.tagsys.sys.service;

import java.util.Map;

import com.seabox.tagsys.sys.entity.SysLogModel;

public interface SysLogService {

	/**
	 * Description 增加一条新日志
	 * @param paramMap void
	 * throws
	 */
	void addSysLog(SysLogModel sysLogModel);
	
	/**
	 * Description 分页取日志列表
	 * @param paramMap
	 * @return List<SysLogModel>
	 * throws
	 */
	String findLogListByPage(Map<String, Object> paramMap);
	
	/**
	 * Description 获取日志总数量，有上限值
	 * @param paramMap
	 * @return int
	 * throws
	 */
	int findTotalNumWithMax(Map<String, Object> paramMap);
	
}
