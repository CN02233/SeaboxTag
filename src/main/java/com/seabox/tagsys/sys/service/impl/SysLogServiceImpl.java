package com.seabox.tagsys.sys.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.sys.entity.SysLogModel;
import com.seabox.tagsys.sys.service.SysLogService;
import org.springframework.stereotype.Service;

import com.seabox.tagsys.sys.entity.User;

@Service("sysLogService")
public class SysLogServiceImpl extends BaseService implements SysLogService {

	private Map<String,String> realOrderColum = new HashMap<String,String>();
	
	@Override
	public void addSysLog(SysLogModel sysLogModel) {
		try{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("log_type_cd", sysLogModel.getLog_type_cd());
			paramMap.put("user_id", sysLogModel.getUser_id());
			paramMap.put("user_ip", sysLogModel.getUser_ip());
			relationDbTemplate.insert("log.addSysLog", paramMap);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String findLogListByPage(Map<String, Object> paramMap) {
		String order_id = (String) paramMap.get("order_id");
		String orderId = realOrderColum(order_id);
		paramMap.put("orderId", orderId);
		PageBean<User> pageBean = relationDbTemplate.selectListForPage("log.findLogListByPage", "log.findTotalNumWithMax",
				paramMap);
		String json = objectToJson(pageBean);
		return json;
	}

	@Override
	public int findTotalNumWithMax(Map<String, Object> paramMap) {
		return relationDbTemplate.selectOne("log.findTotalNumWithMax", paramMap);
	}
	
	private String realOrderColum(String orderId) {
		if (realOrderColum.size() < 1) {
			realOrderColum.put("log_id", "log_id");
			realOrderColum.put("log_type", "log_type_cd");
			realOrderColum.put("log_ts", "log_ts");
			realOrderColum.put("user_real_nm", "user_real_nm");
			realOrderColum.put("user_ip", "user_ip");
		}
		return realOrderColum.get(orderId);
	}

}
