package com.seabox.tagsys.sys.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.sys.service.SysLogService;

/**
 * <pre>
 * Description : 日志管理
 * </pre>
 * @author : xiangwei
 * @date : 2016年1月19日
 * @version : 1.0
 */
@Controller
@RequestMapping("sys/log")
public class SysLogController extends BaseController {

	@Autowired
	private SysLogService sysLogService;
	
	@RequestMapping("main")
	public String logMain(HttpServletRequest request) {
		String strCurrPage = request.getParameter("currPage");
		int currPage = 1;
		if(strCurrPage != null){
			currPage = Integer.parseInt(strCurrPage);
		}
		request.setAttribute("currPage", currPage);
		return "sys/log/logMain";
	}
	
	@RequestMapping("findLogListByPage")
	@ResponseBody
	public String findLogListByPage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> paramMap = initRequestParam(request);
		String strPageSize = request.getParameter("pageSize");
		int pageSize = 9;
		if(strPageSize != null){
			pageSize = Integer.parseInt(strPageSize);
		}
		int max_total_num = pageSize * 30;
		paramMap.put("max_total_num", max_total_num);
		String order_id = request.getParameter("order_id");
		if(order_id == null){
			order_id = "log_id";
		}
		String order_type = request.getParameter("order_type");
		if(order_type == null){
			order_type = "desc";
		}
		String resultJson = sysLogService.findLogListByPage(paramMap);
		return resultJson;
	}
	
}
