package com.seabox.tagsys.camp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seabox.tagsys.sys.entity.SysLogModel;
import com.seabox.tagsys.sys.service.SysLogService;
import com.seabox.tagsys.sys.util.LogTypeCd;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.camp.service.CampManagerService;
import com.seabox.tagsys.sys.entity.User;

/**
 * 活动管理模块：
 * 活动管理：活动创建、查看等controller层
 * @author SongChaoqun
 * @since 2016-01-26
 */
@Controller
@RequestMapping("camp/campMag")
public class CampManageController extends BaseController{
	
	@Autowired
	private CampManagerService campManagerService;
	
	@Autowired
	private SysLogService sysLogService;
	
	@RequestMapping("crtCamp")
	public String showEvtMagMain(HttpServletRequest request){
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String nowDateString = format.format(new Date());
//		String campId = new StringBuffer().append(user.getUser_id()).append(nowDateString).toString();
		
		int campId =  campManagerService.getCampId();
		
		request.setAttribute("campId", campId);
		Map<String,Object> paramMap = initRequestParam(request);
		
		return viewPage("campCreate");
	}
	
	@RequestMapping("addSave")
	@ResponseBody
	public boolean addSave(HttpServletRequest request,HttpServletResponse response){
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		Map<String,Object> paramMap = initRequestParam(request);
		paramMap.put("user_id", user.getUser_id());
		boolean hasSuc = campManagerService.addSave(paramMap);
		
		SysLogModel sysLogModel = new SysLogModel();
		sysLogModel.setLog_type_cd(LogTypeCd.EVENT_CREATION);//日志类型;注1
		sysLogModel.setUser_id(user.getUser_id());//用户ID
		sysLogModel.setUser_ip(user.getSign_in_ip());//用户IP
		sysLogService.addSysLog(sysLogModel);
		
		return hasSuc;
	}
	
//	@RequestMapping("viewTestMsg")
//	@ResponseBody
//	public String viewTestMsg(HttpServletRequest request,HttpServletResponse response){
//		Map<String,Object> paramMap = initRequestParam(request);
//		String msgCont = campManagerService.viewTestMsg(paramMap);
//		return msgCont;
//	}
	
	/**
	 * 获取测试短信内容
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("viewTestMsg")
	@ResponseBody
	public Map<String,Object> viewTestMsg(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		String userNm = user.getUser_real_nm();
		paramMap.put("userNm", userNm);
		String resultString = campManagerService.viewTestMsg(paramMap);
		Map<String,Object> restMap = new HashMap<>();
		restMap.put("msgStr", resultString);
		return restMap;
	}
	
	@RequestMapping("sendTestMsg")
	@ResponseBody
	public List<Map<String, Object>> sendTestMsg(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		String userNm = user.getUser_real_nm();
		paramMap.put("userNm", userNm);
		List<Map<String, Object>> result = campManagerService.sendTestMsg(paramMap);
		return result;
	}
	
	@RequestMapping("getSmsCont")
	@ResponseBody
	public Map getSmsCont(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		Map smsContentMap = campManagerService.getSmsCont(paramMap);
		if(smsContentMap!=null)
			return smsContentMap;
		else{
			Map resultMap = new HashMap();
			resultMap.put("errorMsg", "未找到短信模板");
			return resultMap;
		}
			
	}
}
