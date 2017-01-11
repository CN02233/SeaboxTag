package com.evergrande.base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller层帮助类
 * @author SongChaoqun
 * @since 2015-12-22
 *
 */
public class ControllerHelper {
	
	private static ControllerHelper helper;
	
	private static Map<String,Object> paramMap ;
	
	private ControllerHelper(){}
	
	/**
	 * 获取帮助类实例,此类作为初始化一些controller相关的通用数据,不考虑个性,为节省资源,将其设为单例模式
	 * @return
	 */
	public static ControllerHelper getInstance(){
		if(helper==null){
			helper = new ControllerHelper();
		}
		return helper;
	}
	
	public Map<String,Object> getSearchDataMap(){
		if(paramMap==null){
			paramMap = new HashMap<String,Object>();
			
			Map<String, String> dateMap = initSearchDate();//初始化查询日期
			paramMap.putAll(dateMap);
		}
				
		return paramMap;
	}
	
	/**
	 * 默认查询条件中的日期,开始时间为当前日期所在年份的第一天,结束日期为当天
	 * @return
	 */
	private Map<String,String> initSearchDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		String endDate = format.format(nowDate);
		Calendar calender = Calendar.getInstance();
		calender.setTime(nowDate);
		int nowYear = calender.get(Calendar.YEAR);
		String startDate = new StringBuffer().append(nowYear).append("-01-01").toString();
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("startDate", startDate);
		resultMap.put("endDate", endDate);
		return resultMap;
	}
	
}
