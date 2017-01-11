package com.seabox.tagsys.camp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seabox.tagsys.camp.service.CampViewService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seabox.tagsys.base.controller.BaseController;
import com.seabox.tagsys.sys.entity.SysLogModel;
import com.seabox.tagsys.sys.entity.User;
import com.seabox.tagsys.sys.service.SysLogService;
import com.seabox.tagsys.sys.util.LogTypeCd;

/**
 * 活动管理模块：
 * 活动总览：活动列表、查看等controller层
 * @author SongChaoqun
 * @since 2016-01-12
 */
@Controller
@RequestMapping("camp/campView")
public class CampViewController extends BaseController{
	
	@Autowired
	private CampViewService enventViewService;
	
	@Autowired
	private SysLogService sysLogService;

	
	@RequestMapping("main")
	public String showEvtViewMain(){
		return viewPage("evtViewList");
	}
	
	@RequestMapping("detailPage")
	public String showDetailPage(HttpServletRequest request){
		Map<String,Object> paramMap = initRequestParam(request);
		return viewPage("evtDetail");
	}
	
	@RequestMapping("evtEdit")
	public String editPage(HttpServletRequest request){
		
		Map<String,Object> paramMap = initRequestParam(request);
		
		
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String nowDateString = format.format(new Date());
//		String campId = new StringBuffer().append(user.getUser_id()).append(nowDateString).toString();
//		request.setAttribute("campId", campId);
//		Map<String,Object> paramMap = initRequestParam(request);
		
		
		
		return viewPage("campEdit");
	}
	
	@RequestMapping("showCampAndTags")
	@ResponseBody
	public String showCampAndTags(HttpServletRequest request){
		
		Map<String,Object> paramMap = initRequestParam(request);
		String campData = enventViewService.listCampAndTags(paramMap);//活动信息
		
		return campData;
	}
	
	@RequestMapping("listMainData")
	@ResponseBody
	public String getEvtViewData(HttpServletRequest request,HttpServletResponse response){
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		int userId = user.getUser_id();
		Map<String,Object> paramMap = initRequestParam(request);
		paramMap.put("user_id", userId);
		String resultJson = enventViewService.getEvtViewData(paramMap);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("success", "true");  
		resultMap.put("resultJson", resultJson);  
		return resultJson;
	}
	
	@RequestMapping("getDetailData")
	@ResponseBody
	public String getDetailData(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> paramMap = initRequestParam(request);
		String resultJson = enventViewService.getDetailData(paramMap);
		return resultJson;
	}
	
	@RequestMapping("sumSendData")
	@ResponseBody
	public String sumSendData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		String resultJson = enventViewService.getSendData(paramMap);
		return resultJson;
	}
	
	@RequestMapping("getTagDetailData")
	@ResponseBody
	public String  getTagDetailData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		String resultJson = enventViewService.getLabelData(paramMap);
		
		return resultJson;
	}
	
	@RequestMapping("copyEvtData")
	@ResponseBody
	public String copyEvtData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		User user = (User)request.getSession().getAttribute("user");
		
		int userId = user.getUser_id();
		paramMap.put("user_id", userId);
		
		boolean hasSuc = enventViewService.copyEvtData(paramMap);
		return "success";
	}
	
	@RequestMapping("moveToDelTb")
	@ResponseBody
	public String moveToDelTb(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> paramMap = initRequestParam(request);
		boolean hasSuc = enventViewService.delEvtData(paramMap);
		
		return "success";
	}
	
	@RequestMapping("editSave")
	@ResponseBody
	public boolean editSave(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		boolean hasSuc = enventViewService.editSave(paramMap);
		return hasSuc;
	}
	
	@RequestMapping("listTagData")
	@ResponseBody
	public String listTagData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		int userId = user.getUser_id();
		paramMap.put("userId", userId);
		String resultJson = enventViewService.listTagData(paramMap);
		return resultJson;
	}
	
	@RequestMapping("sumIndvNum")
	@ResponseBody
	public long sumIndvNum(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> paramMap = initRequestParam(request);
		long hitCnt = enventViewService.sumIndvNum(paramMap);
		
		return hitCnt;
	}
	
	@RequestMapping("getCampTmpData")
	@ResponseBody
	public String getCampTmpData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		String tmplateData = enventViewService.getCampTmpData(paramMap);
		return tmplateData;
	}
	
	@RequestMapping("workflowCamp")
	@ResponseBody
	public boolean workflowCamp(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		boolean workflowResult = enventViewService.workflowCamp(paramMap);
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		SysLogModel sysLogModel = new SysLogModel();
		sysLogModel.setLog_type_cd(LogTypeCd.EVENT_APPROVAL);//活动审批
		sysLogModel.setUser_id(user.getUser_id());//用户ID
		sysLogModel.setUser_ip(user.getSign_in_ip());//用户IP
		sysLogService.addSysLog(sysLogModel);
		return true;
	}
	
	@RequestMapping("indsAdChnl")
	@ResponseBody
	public String getIndsAndChnlData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		String jsonData = enventViewService.getIndsAndChnlData(paramMap);
		return jsonData;
	}
	
	@RequestMapping("stopEvt")
	@ResponseBody
	public String stopEvt(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		User user = (User)request.getSession().getAttribute("user");
		
		int userId = user.getUser_id();
		paramMap.put("user_id", userId);
		
		boolean hasSuc = enventViewService.stopEvtData(paramMap);
		return "success";
	}
	
	//listTagValiData
	@RequestMapping("listTagValiData")
	@ResponseBody
	public String listTagValiData(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> paramMap = initRequestParam(request);
		String tagValiData = enventViewService.listTagValiData(paramMap);
		return tagValiData;
	}
}
