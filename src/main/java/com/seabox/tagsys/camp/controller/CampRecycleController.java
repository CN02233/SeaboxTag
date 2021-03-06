package com.seabox.tagsys.camp.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.camp.service.CampRecycleService;
import com.seabox.tagsys.sys.entity.User;


@Controller
@RequestMapping("camp/campRec")
public class CampRecycleController extends BaseController{
	
	@Autowired
	private CampRecycleService campRecycleService;
	
	@RequestMapping("recycle")
	public String showRecycleMain(HttpServletRequest request){
//		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String nowDateString = format.format(new Date());
//		String campId = new StringBuffer().append(user.getUser_id()).append(nowDateString).toString();
//		request.setAttribute("campId", campId);
		Map<String,Object> paramMap = initRequestParam(request);
		
		return viewPage("recMain");
	}
	
	@RequestMapping("listData")
	@ResponseBody
	public String listData(HttpServletRequest request){
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		int userId = user.getUser_id();
		Map<String,Object> paramMap = initRequestParam(request);
		paramMap.put("user_id", userId);
		String resultJson = campRecycleService.listMainData(paramMap);
		return resultJson;

	}
	
	@RequestMapping("rollback")
	@ResponseBody
	public boolean rollback(HttpServletRequest request){
		Map<String,Object> paramMap = initRequestParam(request);
		boolean result = campRecycleService.rollbackData(paramMap);
		return true;
	}
	
	@RequestMapping("deleteCamp")
	@ResponseBody
	public boolean deleteCamp(HttpServletRequest request){
		Map<String,Object> paramMap = initRequestParam(request);
		boolean result = campRecycleService.deleteCamp(paramMap);
		return true;
		
	}
	
	
}
