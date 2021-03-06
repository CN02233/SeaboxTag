package com.seabox.tagsys.camp.service.imp;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.seabox.tagsys.camp.service.CampViewService;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.usertags.hbase.entity.CampSmsActionStatics;
import com.seabox.tagsys.usertags.logicquery.impl.Match;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.sys.entity.SysLogModel;
import com.seabox.tagsys.sys.entity.User;
import com.seabox.tagsys.sys.service.SysLogService;
import com.seabox.tagsys.sys.util.LogTypeCd;
import com.seabox.tagsys.usertags.logicquery.UsersDistributeByTag;
import com.seabox.tagsys.usertags.logicquery.impl.TagConditionBase;
import com.seabox.tagsys.usertags.service.CacheService;


@Service("enventViewService")
public class CampViewServiceImp extends BaseService implements CampViewService {
	
	private static final Logger logger = LoggerFactory.getLogger(CampViewServiceImp.class);
	
	private Map<String,String> realOrderColum = new HashMap<String,String>();
	
	private ThreadLocal<List<Map<String,Object>>> tagCtgysListLocal = new ThreadLocal<List<Map<String,Object>>>();
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private SysLogService sysLogService;
	
	
	/**
	 * 报文格式：
	 *  userId:用户ID,
	 *  isMa:是否管理员:true false
	 *  list:[
	 * 	campId:活动ID,
		campDesc:活动描述,
		ppDesc:人群描述,
		startDate:活动开始日期,
		endDate:活动结束日期,
		hitCount:活动目标客户数量,
		campType:活动方式代码.....,
		campTypeNm:活动方式:SMS EMD......,
		status:活动状态代码,
		statusNm:活动状态,
		createUser:创建用户,
		createDate:创建时间,
		updateDate:最后更新时间]
	 */
	@Override
	public String getEvtViewData(Map<String,Object> paramMap) {
		// TODO Auto-generated method stub
		
//		String order_id = (String) paramMap.get("order_id");
//		String orderId = realOrderColum(order_id);
//		paramMap.put("orderId", orderId);
//
//		Integer userId = (Integer) paramMap.get("user_id");
//		//判断当前用户是否为组管理员
//		Integer cntNum = relationDbTemplate.selectOne("campView.isGrpAdmin", paramMap);
//
//		Map resultMap = new HashMap();
//		resultMap.put("userId", userId);
//
//		String startDate = (String) paramMap.get("start_date");
//		String end_date = (String) paramMap.get("end_date");
//		if("".equals(startDate)&&"".equals(end_date)){
////			Date nowDate = new Date();
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.MONTH, -1);
//			Date oneMonthAgo = calendar.getTime();
//			String oneMonthAgoStr = format.format(oneMonthAgo);
////			String nowDateStr = format.format(nowDate);
//			paramMap.put("start_date", oneMonthAgoStr);
////			paramMap.put("end_date", nowDateStr);
//		}
//
//		PageBean<Map<String,Object>> pageBean = null;
//		List<Map<String,Object>> tagList = null;
//		if(cntNum>0){//当前用户为组管理员
//			pageBean = relationDbTemplate.selectListForPage("campView.listEvtViewAd","campView.cntEvtAdList",paramMap);
//			resultMap.put("isMa", true);
//			tagList = relationDbTemplate.selectList("campView.grpTagNmsAd",paramMap);
//		}else{
//			pageBean = relationDbTemplate.selectListForPage("campView.listEvtView","campView.cntEvtList",paramMap);
//			resultMap.put("isMa", false);
//			tagList = relationDbTemplate.selectList("campView.grpTagNms",paramMap);
//		}
//
//		//获取活动标签数据
//
//		Map<Integer,String> tagMap = new HashMap<Integer,String>();
//		for(Map<String,Object> tag:tagList){
//			Integer campId = (Integer) tag.get("campId");
//			String tagNms = (String) tag.get("tagNms");
//			tagMap.put(campId, tagNms);
//		}
//
//		List<Map<String, Object>> campList = pageBean.getList();
//		for(Map<String,Object> campData:campList){
//			Integer campId = (Integer) campData.get("campId");
//			String tagNms = tagMap.get(campId);
//			if(tagNms!=null&&!"".equals(tagNms))
//				campData.put("ppDesc", tagNms);
//		}
//
//		resultMap.put("pageSize", pageBean.getPageSize());
//		resultMap.put("totalNum", pageBean.getTotalNum());
//		resultMap.put("currPage", pageBean.getCurrPage());
//		resultMap.put("list", campList);
//
////		pageBean.getPageSize();
////		pageBean.getTotalNum();
////		pageBean.getCurrPage();
////		pageBean.getList();
////		List<Map<String, Object>> resultList = pageBean.getList();
//
////		List<Object> resultList = relationDbTemplate.selectList("campView.listEvtView", paramMap);
//
////		String json = objectToJson(pageBean);
//		return objectToJson(resultMap);
		return null;
	}	
	
	
	private String realOrderColum(String orderId){
//		if(realOrderColum.size()<1){
//			realOrderColum.put("campId", "camp_id");
//			realOrderColum.put("campNm", "camp_nm");
//			realOrderColum.put("campDesc", "camp_desc");
//			realOrderColum.put("startDate", "start_dt");
//			realOrderColum.put("endDate", "end_dt");
//			realOrderColum.put("hitCount", "indv_num");
//			realOrderColum.put("campType", "camp_chnl_cd");
//			realOrderColum.put("status", "camp_status_cd");
//			realOrderColum.put("createUser", "user_id");
//			realOrderColum.put("createDate", "created_ts");
//			realOrderColum.put("updateDate", "updated_ts");
//			realOrderColum.put("userId", "user_id");
//		}
//		return realOrderColum.get(orderId);
		return null;
	}

	@Override
	public boolean copyEvtData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		//取新活动ID
		int camp_new_id = relationDbTemplate.selectOne("campManage.getCampId");
		paramMap.put("camp_new_id", camp_new_id);
		paramMap.put("camp_status_cd", "01");
		paramMap.put("created_ts", new Date());
		paramMap.put("updated_ts", new Date());
		
		int copyResult = relationDbTemplate.insert("campView.copyData", paramMap);
		relationDbTemplate.insert("campView.copyTagData", paramMap);
		relationDbTemplate.insert("campView.copyTmpData", paramMap);
		relationDbTemplate.insert("campView.copyTmpParamData", paramMap);
		
		return true;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean editSave(Map<String, Object> paramMap) {
		Map<String,Object> json_data = (Map)paramMap.get("json_data");
		Date startDate = new Date();
		json_data.put("updated_ts", startDate);
		
		String stats = (String) json_data.get("camp_status_cd");
		if(stats!=null&&"02".equals(stats)){//当前操作为提交操作,判断当前用户是否为管理员,如果是管理员则直接将状态置为分布状态
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			SysLogModel sysLogModel = new SysLogModel();
			sysLogModel.setLog_type_cd(LogTypeCd.EVENT_SUBMISSION);//活动提交
			sysLogModel.setUser_id(user.getUser_id());//用户ID
			sysLogModel.setUser_ip(user.getSign_in_ip());//用户IP
			sysLogService.addSysLog(sysLogModel);
			
			paramMap.put("user_id",user.getUser_id());
			//判断当前用户是否为组管理员
			Integer cntNum = relationDbTemplate.selectOne("campView.isGrpAdmin", paramMap);
			if(cntNum>0){//当前用户为组管理员
				json_data.put("camp_status_cd","03");//将新建活动状态置为初始
			}
		}else{
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			SysLogModel sysLogModel = new SysLogModel();
			sysLogModel.setLog_type_cd(LogTypeCd.EVENT_MODIFICATION);//活动修改
			sysLogModel.setUser_id(user.getUser_id());//用户ID
			sysLogModel.setUser_ip(user.getSign_in_ip());//用户IP
			sysLogService.addSysLog(sysLogModel);
		}
		logger.info("开始更新活动信息:"+objectToJson(json_data));
		relationDbTemplate.insert("campView.updateCamp", json_data);
		logger.info("活动信息更新完成！");
		logger.info("开始删除旧标签:"+objectToJson(json_data));
		relationDbTemplate.delete("campView.removeCampTag", json_data);
		logger.info("旧标签完成！");
		
		List<List<Map<String,Object>>> campTagDataList = (List<List<Map<String,Object>>>) json_data.get("tag_grp");
		int tag_group_seq = 1;
		for(List<Map<String,Object>> campTagData:campTagDataList){
			for(Map<String,Object> tagData:campTagData){
				tagData.put("tag_group_seq", tag_group_seq);
				tagData.put("camp_id", json_data.get("camp_id"));
				tagData.put("created_ts", startDate);
				tagData.put("updated_ts", startDate);
				logger.info("开始插入新标签信息:"+objectToJson(tagData));
				relationDbTemplate.insert("campManage.saveCampTag",tagData);
				logger.info("新标签完成！");
			}
			
			tag_group_seq++;
		}
		
		TagConditionBase resultCondition = null;
		
		for(List<Map<String,Object>> campTagData:campTagDataList){
			Map<String,TagConditionBase> tagCtgyMap = new HashMap<>();
//			Map<String,TagConditionBase> tagCtgyMap = new HashMap<>();
			for(Map<String,Object> tagData:campTagData){//组
				tagData.put("tag_group_seq", tag_group_seq);
				tagData.put("camp_id", json_data.get("camp_id"));
				tagData.put("created_ts", startDate);
				tagData.put("updated_ts", startDate);
//				relationDbTemplate.insert("campManage.saveCampTag",tagData);//保存活动&标签对应关系
				String tag_ctgy_id = (String) tagData.get("tag_ctgy_id");
				String tag_id = (String) tagData.get("tag_id");
				
				Match match = new Match();
				match.setTagId(new Integer(tag_id));
				
				if(tagCtgyMap.containsKey(tag_ctgy_id)){
					tagCtgyMap.put(tag_ctgy_id, tagCtgyMap.get(tag_ctgy_id).or(match));
				}else{
					
					tagCtgyMap.put(tag_ctgy_id, match);
				}
			}
			TagConditionBase tagCtgyResult = null;
			for(String tagCtgyId : tagCtgyMap.keySet()){
				TagConditionBase tagCtgyCondition = tagCtgyMap.get(tagCtgyId);
				if(tagCtgyResult!=null)
					tagCtgyResult = tagCtgyResult.and(tagCtgyCondition);
				else
					tagCtgyResult = tagCtgyCondition;
			}
			
			if(resultCondition!=null)
				resultCondition = resultCondition.or(tagCtgyResult);
			else
				resultCondition = tagCtgyResult;
			
			tag_group_seq++;
		}
		
		String json = objectToJson(resultCondition);
		
		if(stats!=null&&"02".equals(stats)){//当前操作为提交操作,判断当前用户是否为管理员,如果是管理员则直接将状态置为分布状态
			//活动提交,调用巫长华接口,生成发送名单
			String campIdStr = (String) json_data.get("camp_id");
			int campIdInt = new Integer(campIdStr).intValue();
			logger.info("当前活动为提交操作,开始调用cache接口"+campIdStr);
			cacheService.commitAndSaveCacheForCampaign(campIdInt, resultCondition,null);
			logger.info("活动提交完成:"+campIdStr);
		}
		
		Map<String,Object> smsDataMap = (Map<String,Object>)json_data.get("sms_tmp");
		if(smsDataMap!=null&&smsDataMap.size()>0){
			String smsTmpId = (String) smsDataMap.get("sms_tmp_id");
			String msg_conent = (String) smsDataMap.get("msg_conent");
			Map<String,Object> campTepMap = new HashMap<>();
			campTepMap.put("camp_id", json_data.get("camp_id"));
			campTepMap.put("templt_id", smsTmpId);
			campTepMap.put("templt_cont", msg_conent);
			campTepMap.put("created_ts", startDate);
			campTepMap.put("updated_ts", startDate);
			
			relationDbTemplate.update("campManage.saveOrUpdCampTemp",campTepMap);//保存活动&短信模板内容
			
			List<Map<String,Object>> paramList = (List<Map<String, Object>>) smsDataMap.get("msg_params");
			
			if(paramList!=null&&paramList.size()>0){
				relationDbTemplate.delete("campManage.removeCampTepPars", campTepMap);
			}
			int i=1;
			for(Map<String,Object> param : paramList){
				String paramName = (String) param.get("paramName");
				String paramValue = (String) param.get("paramValue");
				
				if("".equals(paramValue))
					continue;
				
				Map<String,Object> campParamMap = new HashMap<String,Object>();
				campParamMap.put("camp_id", json_data.get("camp_id"));
				campParamMap.put("templt_id", smsTmpId);
				campParamMap.put("templt_para_seq", i);
				
				campParamMap.put("templt_para_nm", paramName);
				campParamMap.put("templt_para_value", paramValue);
				campParamMap.put("created_ts", startDate);
				campParamMap.put("updated_ts", startDate);
				relationDbTemplate.insert("campManage.saveCampTepPars",campParamMap);//保存模板&参数内容
				i++;
			}
		}
//		for(Map<String,Object> campTagData:campTagDataList){
//			campTagData.put("tag_group_seq", tag_group_seq);
//			campTagData.put("camp_id", json_data.get("camp_id"));
//			campTagData.put("created_ts", startDate);
//			campTagData.put("updated_ts", startDate);
//			relationDbTemplate.insert("campView.saveCampTag", campTagData);
//			tag_group_seq++;
//		}
		
		return false;
	}


	@Override
	/**
	 * 删除活动,删除操作会将活动数据放到回收站表中
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean delEvtData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		SysLogModel sysLogModel = new SysLogModel();
		sysLogModel.setLog_type_cd(LogTypeCd.EVENT_DELETE);//活动提交
		sysLogModel.setUser_id(user.getUser_id());//用户ID
		sysLogModel.setUser_ip(user.getSign_in_ip());//用户IP
		sysLogService.addSysLog(sysLogModel);
		relationDbTemplate.insert("campView.moveToDelTb", paramMap);
		relationDbTemplate.delete("campView.delEvtData",paramMap);
		
		return true;
	}

	@Override
	public boolean stopEvtData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("camp_status_cd", "05");//停止活动,将活动置为完成状态
		relationDbTemplate.insert("campView.stopEvtData", paramMap);
		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String getDetailData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Map<String,Object> detailData = (Map<String,Object>)relationDbTemplate.selectOne("campView.getDetailData", paramMap);
		return objectToJson(detailData);
	}
	
	@Override
	/**
	 * 获取活动对应标签数据
	 * tagHitCnt:目标人群数量,
	 * tagGrpLst:
	 * [tagGrpSeq:标签组ID,
	 * tagGrpDetail:[
	 * 	tagCtgyNm:标签类别名称,
	 *  tagCtgyId:标签类别ID
	 *  tagDetail:{
	 * 		tagId:标签ID,
	 * 		tagNm:标签名称,
	 * 	}
	 * ]]
	 * 
	 * */
	public String getLabelData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> tagList = relationDbTemplate.selectList("campView.getLabelData", paramMap);
		
		List<Map<String,Object>> resultList = new ArrayList<>();
		
		//标签组ID:<标签类别ID,标签组<标签信息KEY:标签信息VALUE>>
		Map<Integer,Map<String,List<Map<String,Object>>>> tagGrpMapTmp = new HashMap<>();
		
		Map<String,String> tagCtgyMapTmp = new HashMap<String,String>();//为防止标签类别中文名相同,将标签放到MAP中,防止后面用到的时候乱了。
		
//		List<Map<String,Object>> sumNumList = new ArrayList<>();
		
		for(Map<String,Object> tagMapDb : tagList){
			Integer tagGrpSq = (Integer) tagMapDb.get("tagGrpSq");//标签组序号
			String tagCtgyId = (String) tagMapDb.get("tagCtgyId");//标签类别ID
			Integer tagId = (Integer) tagMapDb.get("tagId");//标签ID
			String tagNm = (String) tagMapDb.get("tagNm");//标签名称
			String tagTyp = (String) tagMapDb.get("tagTyp");//标签类别名称
			Map sumNumMapTmp = new HashMap();
			sumNumMapTmp.put("tag_id",tagId.toString());
			sumNumMapTmp.put("tag_ctgy_id",tagCtgyId);
//			sumNumList.add(sumNumMapTmp);
			if(!tagCtgyMapTmp.containsKey(tagCtgyId))
				tagCtgyMapTmp.put(tagCtgyId, tagTyp);
			
			Map<String,Object> tagMapTmp = new HashMap<String,Object>();
			tagMapTmp.put("tagId", tagId);
			tagMapTmp.put("tagNm", tagNm);
			
			if(tagGrpMapTmp.containsKey(tagGrpSq)){//标签组下所有标签类别
				Map<String, List<Map<String, Object>>> tagGrpDetailTmpMap = tagGrpMapTmp.get(tagGrpSq);
				if(tagGrpDetailTmpMap.containsKey(tagCtgyId)){//标签类别下所有标签
					List<Map<String, Object>> tagDetail = tagGrpDetailTmpMap.get(tagCtgyId);
					tagDetail.add(tagMapTmp);
				}else{
					List<Map<String, Object>> tagDetail = new ArrayList<>();
					tagDetail.add(tagMapTmp);
					tagGrpDetailTmpMap.put(tagCtgyId, tagDetail);
				}
			}else{
				Map<String, List<Map<String, Object>>> tagGrpDetailTmpMap = new HashMap<>();
				List<Map<String, Object>> tagDetail = new ArrayList<>();
				tagDetail.add(tagMapTmp);
				tagGrpDetailTmpMap.put(tagCtgyId, tagDetail);
				tagGrpMapTmp.put(tagGrpSq, tagGrpDetailTmpMap);
			}
		}
		
		Set<Integer> tagGrpIds = tagGrpMapTmp.keySet();
		for(Integer tagGrpId:tagGrpIds){
			Map<String,Object> resultMapTmp = new HashMap<>();
			List<Map<String,Object>> tagGrpDetailList = new ArrayList<>();
			resultMapTmp.put("tagGrpSeq", tagGrpId);
			
			Map<String, List<Map<String, Object>>> tagGrpDetailTmpMap = tagGrpMapTmp.get(tagGrpId);
			
			Set<String> tagTypeIds = tagGrpDetailTmpMap.keySet();
			for(String tagTypId:tagTypeIds){
				HashMap<String,Object> tagTypMap = new HashMap<>();
				tagTypMap.put("tagCtgyId", tagTypId);
				tagTypMap.put("tagCtgyNm", tagCtgyMapTmp.get(tagTypId));
				List<Map<String, Object>> tagListTmp = tagGrpDetailTmpMap.get(tagTypId);
				tagTypMap.put("tagDetail", tagListTmp);
				tagGrpDetailList.add(tagTypMap);
			}
			resultMapTmp.put("tagGrpDetail", tagGrpDetailList);
			resultList.add(resultMapTmp);
		}
		//Map<String,List<Map<String,Object>>>
		List<List<Map<String, Object>>> sumList = new ArrayList<>();
		for(Map<String, List<Map<String, Object>>> tagSeqMaps/*<标签组ID,标签LIST>*/ : tagGrpMapTmp.values()){
			List<Map<String, Object>> tagSeqList = new ArrayList<Map<String, Object>>();
			for(List<Map<String, Object>> tagGrpList:tagSeqMaps.values()){
				tagSeqList.addAll(tagGrpList);
			}
			sumList.add(tagSeqList);
		}
		
//		long hitNum = sumDataFromRedis(sumList,false);//计算标签命中人群数量
		
		Map resultMap = new HashMap();
		resultMap.put("tagGrpLst", resultList);
//		resultMap.put("tagHitCnt", hitNum);
				
		return objectToJson(resultMap);
	}

	/**
	 * 获取短信发送情况数据
	 * campId:活动ID
	 * indvNum:目标客户数量
	 * sendNum:推送成功客户数量
	 * sendPcnt:推送成功占比
	 * repNum:回执客户数量 -成功送达数量
	 * repPcnt:回执客户占比 -成功送达占比
	 * replyNum:回复客户数量 -响应短信数量
	 * replyPcnt:回复客户占比 -响应短信占比
	 */
	@Override
	public String getSendData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		String campIdStr = (String) paramMap.get("campId");
		CampSmsActionStatics sendResult = cacheService.getCampSmsActionStatics(new Integer(campIdStr).intValue());
		
//		Map<String,Object> resultMap = relationDbTemplate.selectOne("campView.getSendData",paramMap);
		Map<String,Object> resultMap = null;
//		if(resultMap!=null&&resultMap.size()>0){
		
		NumberFormat numberFormat = NumberFormat.getInstance();  
        // 设置精确到小数点后2位 
        numberFormat.setMaximumFractionDigits(2);  
  
		if(sendResult!=null){	
			resultMap = new HashMap<>();
			long indvNum = sendResult.getTotalUserCount();//目标客户数量
			long sendNum = sendResult.getSendCount();//推送成功客户数量
			long repNum = sendResult.getRecvCount();//推送成功客户数量
			long replyNum = sendResult.getAckCount();//回复的客户数量
			
			resultMap.put("indvNum", indvNum);
			resultMap.put("sendNum", sendNum);
			resultMap.put("sendPcnt",numberFormat.format((float) sendNum / (float) indvNum * 100));
			resultMap.put("repNum", repNum);
			resultMap.put("repPcnt", numberFormat.format((float) repNum / (float) indvNum * 100));
			resultMap.put("replyNum", replyNum);
			resultMap.put("replyPcnt", numberFormat.format((float) replyNum / (float) indvNum * 100));
		}else{
			resultMap = new HashMap<>();
			resultMap.put("indvNum", 0);
			resultMap.put("sendNum", 0);
			resultMap.put("sendPcnt", 0);
			resultMap.put("repNum", 0);
			resultMap.put("repPcnt", 0);
			resultMap.put("replyNum", 0);
			resultMap.put("replyPcnt", 0);
		}
		
		return objectToJson(resultMap);
	}


	/**
	 * 获取标签数据,包括标签收藏夹数据和标签市场数据
	 * 报文格式:
	 * favTagList:{
	 * 	tagId:标签ID,
	 * 	tagNm:标签名称,
	 * 	parTagId:上级标签类别ID,
	 * 	sonCnt:下级标签/标签类别个数
	 * }
	 * marketTagList:{
	 * 	tagId:标签ID,
	 * 	tagNm:标签名称,
	 * 	parTagId:上级标签类别ID,
	 * 	sonCnt:下级标签/标签类别个数
	 * }
	 * @param paramMap
	 * @return
	 */
	@Override
	public String listTagData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		//获取标签市场数据 
//		List<Map<String, Object>> marketList = getMarketList();
//
//		//获取标签收藏夹数据
//		List<Map<String, Object>> favTagList = getFavList(paramMap);
//
//
//		Map tagResultMap = new HashMap();
//		tagResultMap.put("marketTagList", marketList);
//		tagResultMap.put("favTagList", favTagList);
//		String jsonValue = objectToJson(tagResultMap);
//		return jsonValue;
		return null;
	}
	
	private List<Map<String,Object>> getMarketList(){
//		List<Map<String,Object>> tagsList = relationDbTemplate.selectList("campView.listTags", null);//获取标签数据
//		List<Map<String,Object>> tagCtgysList = relationDbTemplate.selectList("campView.listTagCtgys", null);//获取标签类别数据
//
//		tagCtgysListLocal.set(tagCtgysList);
//
//		List<Map<String,Object>> marketList = new ArrayList<>();
//		Map<String,Map<String, Object>> tagCtgy = new LinkedHashMap<>();
//		for(Map<String,Object> ctgyMap:tagCtgysList){
//			String tagId = (String) ctgyMap.get("tagId");
//			String tagNm = (String) ctgyMap.get("tagNm");
//			String parTagId = (String) ctgyMap.get("parTagId");
//			if(!tagCtgy.containsKey(tagId)){
//				ctgyMap.put("sonCnt", 0);
//				tagCtgy.put(tagId, ctgyMap);
//			}
//			else{
//				Map ctgyTmpMap = (Map) tagCtgy.get(tagId);
//				ctgyTmpMap.putAll(ctgyMap);
//			}
//
//			if(parTagId!=null&&!"".equals(parTagId)){
//				if(!tagCtgy.containsKey(parTagId)){
//					if("prt_".equals(parTagId))
//						continue;
//					Map ctgyTmpMap = new HashMap();
//					ctgyTmpMap.put("sonCnt", 1);
//					tagCtgy.put(parTagId, ctgyTmpMap);
//				}else{
//					Map ctgyTmpMap = (Map) tagCtgy.get(parTagId);
//					int sonCnt = 0;
//					if(ctgyTmpMap.containsKey("sonCnt")){
//						sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
//					}
//					ctgyTmpMap.put("sonCnt", ++sonCnt);
//				}
//			}
//
//		}
//		List<Map<String,Object>> resTagsList = new ArrayList<>();
//		for(Map<String,Object> tagMap:tagsList){
//			Integer tagId = (Integer) tagMap.get("tagId");
//			String tagNm = (String) tagMap.get("tagNm");
//			String parTagId = (String) tagMap.get("parTagId");
//			if(tagCtgy.containsKey(parTagId)){
//				Map ctgyTmpMap = (Map) tagCtgy.get(parTagId);
////				int sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
////				ctgyTmpMap.put("sonCnt", ++sonCnt);
//				tagMap.put("tagCtgyNm", ctgyTmpMap.get("tagNm"));//父标签类别名称
//
//				int sonCnt = 0;
//				if(ctgyTmpMap.containsKey("sonCnt")){
//					sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
//				}
//				ctgyTmpMap.put("sonCnt", ++sonCnt);
//				resTagsList.add(tagMap);
//			}
//
//		}
//		marketList.addAll(tagCtgy.values());
//		marketList.addAll(resTagsList);
		
		
//		return marketList;
		return null;
	}
	
	private List<Map<String,Object>> getFavList(Map<String,Object> paramMap){
//		List<Map<String,Object>> favTagResultList = new ArrayList<>();
//
//		List<Map<String,Object>> favTagList = relationDbTemplate.selectList("campView.listFavTags", paramMap);
//
//		if(favTagList!=null&&favTagList.size()>0){}
//		else return favTagList;
//
//		Map<String,Map<String, Object>> tagCtgy = new LinkedHashMap<>();
//
//		List<Map<String, Object>> tagCtgysList = tagCtgysListLocal.get();
//
//		for(Map<String,Object> ctgyMap:tagCtgysList){
//			String tagId = (String) ctgyMap.get("tagId");
//			String tagNm = (String) ctgyMap.get("tagNm");
//			String parTagId = (String) ctgyMap.get("parTagId");
//			if(!tagCtgy.containsKey(tagId)){
//				ctgyMap.put("sonCnt", 0);
//				tagCtgy.put(tagId, ctgyMap);
//			}
//			else{
//				Map ctgyTmpMap = (Map) tagCtgy.get(tagId);
//				ctgyTmpMap.putAll(ctgyMap);
//			}
//
//			if(parTagId!=null&&!"".equals(parTagId)){
//				if(!tagCtgy.containsKey(parTagId)){
//					if("prt_".equals(parTagId))
//						continue;
//					Map ctgyTmpMap = new HashMap();
//					ctgyTmpMap.put("sonCnt", 1);
//					tagCtgy.put(parTagId, ctgyTmpMap);
//				}else{
//					Map ctgyTmpMap = (Map) tagCtgy.get(parTagId);
//					int sonCnt = 0;
//					if(ctgyTmpMap.containsKey("sonCnt")){
//						sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
//					}
//					ctgyTmpMap.put("sonCnt", ++sonCnt);
//				}
//			}
//		}
//
//		List<Map<String,Object>> tagCtgyFavList = new ArrayList<>();
//		List<String> hasFindCtgyList = new ArrayList<>();
//		List<Map<String,Object>> resTagsList = new ArrayList<>();
//		for(Map<String,Object> tagMap:favTagList){
//			Integer tagId = (Integer) tagMap.get("tagId");
//			String tagNm = (String) tagMap.get("tagNm");
//			String parTagId = (String) tagMap.get("parTagId");
//			if(tagCtgy.containsKey(parTagId)){
//				Map<String, Object> ctgyTmpMap = (Map<String, Object>) tagCtgy.get(parTagId);
//				tagMap.put("tagCtgyNm", ctgyTmpMap.get("tagNm"));//父标签类别名称
//				int sonCnt = 0;
//				if(ctgyTmpMap.containsKey("sonCnt")){
//					sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
//				}
//				ctgyTmpMap.put("sonCnt", ++sonCnt);
//
//				Map<String, Object> parentTmp = ctgyTmpMap;
//				if(hasFindCtgyList.contains(parTagId))
//					continue;
//				else
//					hasFindCtgyList.add(parTagId);
//				//根据父标签类别向上遍历找到整个树
//				String parentTagIdTmp = (String) parentTmp.get("parTagId");
//
//				while(true){
//					tagCtgyFavList.add(parentTmp);
//					parentTagIdTmp = (String) parentTmp.get("parTagId");
//					if(hasFindCtgyList.contains(parentTagIdTmp))
//						break;
//					hasFindCtgyList.add(parentTagIdTmp);
//					if(tagCtgy.containsKey(parentTagIdTmp)){
//						parentTmp = tagCtgy.get(parentTagIdTmp);
//					}else
//						break;
//				}
//				resTagsList.add(tagMap);
//
//			}
//
//
//
//		}
////		favTagResultList.addAll(tagCtgy.values());
//		favTagResultList.addAll(tagCtgyFavList);
//		favTagResultList.addAll(resTagsList);
//
//
//		return favTagResultList;
		return null;
		
	}


	@Override
	public long sumIndvNum(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
//		List<Map<String,Object>> jsonData = (List<Map<String,Object>>) paramMap.get("json_data");
		
		List<List<Map<String,Object>>> jsonData = (List<List<Map<String,Object>>>) paramMap.get("json_data");
		
		return sumDataFromRedis(jsonData,true);
	}
	
	/**
	 * 逻辑:标签组中同类别标签为or关系 不同类别标签为and关系。标签组之间为or关系
	 * 如:组1:20~30岁 30~40岁 男，组2:50~60岁
	 * 关系为:(20~30岁 or 30~40岁 and 男) or (50~60岁)
	 * @param tagsList
	 * @param fromPage
	 * @return
	 */
	private long sumDataFromRedis(List<List<Map<String,Object>>> tagsList,boolean fromPage){
		Map<String,TagConditionBase> tagGrpMap = new HashMap<>();
//		Map<String,Match> tagGrpTmpMap = new HashMap<>();
		
		TagConditionBase resultCondition = null;
		for(List<Map<String,Object>> tagSeq:tagsList){//组
			TagConditionBase conditionTmp = null;
			Map<String,TagConditionBase> tagCtgyMap = new HashMap<>();
			for(Map<String,Object> tagData:tagSeq){//标签
				Match tagCondition = new Match();
				if(fromPage){//是否页面传递参数,页面的需要转成Integer,数据库查询到的直接就是integer
					String tag_id = (String) tagData.get("tagId");
			        tagCondition.setTagId(new Integer(tag_id));
				}else{
					tagCondition.setTagId((Integer) tagData.get("tagId"));
				}
				String tagCtgyId = (String)tagData.get("tagCtgyId");
				if(tagCtgyMap.containsKey(tagCtgyId)){//取出同类别的标签集合,or操作
					TagConditionBase hasOr = tagCtgyMap.get(tagCtgyId);
					tagCtgyMap.put(tagCtgyId, hasOr.or(tagCondition));
				}else{
					tagCtgyMap.put(tagCtgyId,tagCondition);
				}
//		        if(conditionTmp==null)
//		        	conditionTmp = tagCondition;
//		        else
//		        	conditionTmp = conditionTmp.and(tagCondition);
			}
			
			//将标签组中不同类别的标签and操作
			for(TagConditionBase orCondition : tagCtgyMap.values()){
				if(conditionTmp==null)
					conditionTmp = orCondition;
				else
					conditionTmp = conditionTmp.and(orCondition);
			}
			
			//将标签组与其他标签组and操作
			if(resultCondition==null)
				resultCondition = conditionTmp;
			else
				resultCondition = resultCondition.or(conditionTmp);
		}
		
		long resultNum = 0;
		if(resultCondition!=null)
			resultNum = cacheService.getNumOfUsersByCondition(resultCondition,null);
		
		return resultNum;
	}


	@Override
	/**
	 * 获取活动信息以及该活动的标签信息
	 * campInfo:{
	 * 	campId:活动ID
	 * 	campNm:活动名称
	 * 	startDt:活动开始日期
	 * 	endDt:活动结束日期
	 * 	campChnlId:活动渠道代码
	 *  indvNum:活动人数
	 * }
	 * tagGrpList:[{
	 * 	tagGrpSq：标签组序号
	 * 	tagList:[
	 *  	{tagId:标签ID
	 * 		 tagNm:标签名称
	 * 		 tagCtgyId:标签所属类别ID
	 * 		 tagTyp:标签名称
	 *  	},
	 *  	.............
	 *  ]
	 * 
	 * }]
	 */
	public String listCampAndTags(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Map<String,Object> resultJsonMap = new HashMap<>();
		
		//活动信息
		Map<String,Object> detailData = (Map<String,Object>)relationDbTemplate.selectOne("campView.getDetailData", paramMap);
		Map<String,Object> campInfoMap = new HashMap<String,Object>();
		campInfoMap.put("campId", detailData.get("campId"));
		campInfoMap.put("campNm", detailData.get("campNm"));
		campInfoMap.put("startDt", detailData.get("startDt"));
		campInfoMap.put("endDt", detailData.get("endDt"));
		campInfoMap.put("campChnlId", detailData.get("campChnlId"));
		campInfoMap.put("campIndsId", detailData.get("campIndsId"));
		campInfoMap.put("indvNum", detailData.get("hitCount"));
		
		
		resultJsonMap.put("campInfo", campInfoMap);
		
		//活动关联标签信息
		List<Map<String,Object>> tagsDatas = relationDbTemplate.selectList("campView.getLabelData", paramMap);
		Map<Integer,List<Map<String,Object>>> tagGrpMap = new HashMap<Integer,List<Map<String,Object>>>();
		for(Map<String,Object> tagGrpData:tagsDatas){
			Integer tagGrpSq = (Integer)tagGrpData.get("tagGrpSq");
			Integer tagId = (Integer)tagGrpData.get("tagId");
			String tagNm = (String)tagGrpData.get("tagNm");
			String tagCtgyId = (String)tagGrpData.get("tagCtgyId");
			String tagTyp = (String)tagGrpData.get("tagTyp");
			List<Map<String, Object>> tagsList = null;
			if(tagGrpMap.containsKey(tagGrpSq)){
				tagsList = tagGrpMap.get(tagGrpSq);
			}else{
				tagsList = new ArrayList<Map<String, Object>>();
				tagGrpMap.put(tagGrpSq, tagsList);
			}
			Map mapTmp = new HashMap();
			mapTmp.put("tagId",tagId);
			mapTmp.put("tagNm",tagNm);
			mapTmp.put("tagCtgyId",tagCtgyId);
			mapTmp.put("tagTyp",tagTyp);
			tagsList.add(mapTmp);
		}
		List<Map<String,Object>> tagGrpList = new ArrayList<>();
		for(Integer key : tagGrpMap.keySet()){
			Map<String,Object> tagGrpResTmpMap = new HashMap<>();
			tagGrpResTmpMap.put("tagGrpSq", key);
			tagGrpResTmpMap.put("tagList", tagGrpMap.get(key));
			tagGrpList.add(tagGrpResTmpMap);
		}
		resultJsonMap.put("tagGrpList", tagGrpList);
		
		return objectToJson(resultJsonMap);
	}


	/**
	 * 报文格式
	 * sms_tmp_id:短信模板ID 
	 * msg_params:[{ paramName:参数名称,paramValue:参数值}]
	 */
	@Override
	public String getCampTmpData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> tmpDatas = relationDbTemplate.selectList("campView.getCampTmpData", paramMap);
		
//		if(tmpDatas!=null&&tmpDatas.size()>0){}
//		else return null;
		
		Map resultMap = new HashMap();
		
		String tmpId = null;
		String model_content = null;
		List msgParasList = new ArrayList();
		for(Map<String,Object> data:tmpDatas){
			if(tmpId==null)
				tmpId = (String) data.get("tmpId");
			
			if(model_content==null)
				model_content = (String) data.get("model_content");
			
			String tmpParaNm = (String) data.get("tmpParaNm");
			String tmpParaVal = (String) data.get("tmpParaVal");
			Map tmpMap = new HashMap();
			tmpMap.put("paramName",tmpParaNm);
			tmpMap.put("paramValue",tmpParaVal);
			msgParasList.add(tmpMap);
		}
		
		resultMap.put("sms_tmp_id", tmpId);
		resultMap.put("model_content", model_content);
		resultMap.put("msg_params", msgParasList);
		
		return objectToJson(resultMap);
	}


	/**
	 * 获取短信模板内容
	 * @param tmpId
	 * @return
	 */
	private String getTmpCont(String tmpId){
		return "测试短信模板内容";
	}


	/**
	 * 通过&拒绝活动
	 */
	@Override
	public boolean workflowCamp(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("updated_ts", new Date());
		relationDbTemplate.update("campView.workflowCamp", paramMap);
		return false;
	}


	/**
	 * 获取活动渠道&活动产业代码数据
	 * chnlDataList:[
	 * 	{chnlCd:活动渠道代码
	 * 	 chnlNm:活动渠道名称}
	 * ]
	 * indsDataList:[
	 * 	{indsCd:活动产业代码
	 * 	 indsNm:活动产业名称}
	 * ]
	 */
	@Override
	public String getIndsAndChnlData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> chnlDataList = relationDbTemplate.selectList("campView.getChnlDataList",paramMap);
		
		List<Map<String,Object>> indsDataList = relationDbTemplate.selectList("campView.getIndsDataList",paramMap);
		
		Map resultMap = new HashMap();
		resultMap.put("chnlDataList", chnlDataList);
		resultMap.put("indsDataList", indsDataList);
		
		return objectToJson(resultMap);
	}

	
	/**
	 * JSON报文格式:
	 * allCnt:发送总数量
	 * tagCtgyList:标签数据列表
	 * [tagCtgyId:标签类别ID,
	 *  tagCtgyNm:标签类别名称,
	 * tagList:{
	 * 	tagId:标签ID,
	 *  tagNm:标签名称,
	 *  tagCodiCnt:标签人群数量
	 * }]
	 */
	@Override
	public String listTagValiData(Map<String, Object> paramMap) {		
		//获取活动下所有标签
		List<Map<String,Object>> tagRelaList = relationDbTemplate.selectList("campView.getLabelData", paramMap);
		
		Map<Integer,TagConditionBase> tagCtgyBaseMap = new HashMap<>();
		
		//<组ID,<标签类别ID,标签关系集>>
		Map<Integer,Map<String,TagConditionBase>> tagCtgyContiMap = new HashMap<>();
		
		for(Map<String,Object> tagRelaData:tagRelaList){
			Integer tagId = (Integer)tagRelaData.get("tagId");
			String tagCtgyId = (String)tagRelaData.get("tagCtgyId");
			Integer tagGrpSq = (Integer)tagRelaData.get("tagGrpSq");
			
			Match tagCondition = new Match();
	        tagCondition.setTagId(tagId);
	        
	        if(tagCtgyContiMap.containsKey(tagGrpSq)){
	        	Map<String, TagConditionBase> tagCtgyMap = tagCtgyContiMap.get(tagGrpSq);
	        	if(tagCtgyMap.containsKey(tagCtgyId)){
	        		tagCtgyMap.put(tagCtgyId, tagCtgyMap.get(tagCtgyId).or(tagCondition));
	        	}else{
		        	tagCtgyMap.put(tagCtgyId, tagCondition);
	        	}
	        }else{
	        	Map<String,TagConditionBase> tagCtgyMap = new HashMap<>();
	        	tagCtgyMap.put(tagCtgyId, tagCondition);
	        	tagCtgyContiMap.put(tagGrpSq, tagCtgyMap);
	        }
	        
//			if(tagCtgyBaseMap.containsKey(tagGrpSq)){//同一组标签
//				//查看
//				
//				TagConditionBase conditionBase = tagCtgyBaseMap.get(tagGrpSq);
//				tagCtgyBaseMap.put(tagGrpSq, conditionBase.and(tagCondition));
//			}else{
//				tagCtgyBaseMap.put(tagGrpSq, tagCondition);
//			}
		}
		
		TagConditionBase resultCondition = null;
		
		for(Integer seqs : tagCtgyContiMap.keySet()){//循环标签组集合
			Map<String, TagConditionBase> tagCtgyMap = tagCtgyContiMap.get(seqs);//取出标签组下标签类别集合
			TagConditionBase seqCollection = null;
			for(String tagCtgyId : tagCtgyMap.keySet()){//不同类别标签and操作
				TagConditionBase condition = tagCtgyMap.get(tagCtgyId);
				if(seqCollection==null)
					seqCollection = condition;
				else
					seqCollection = seqCollection.and(condition);
			}
			if(resultCondition==null)
				resultCondition = seqCollection;
			else
				resultCondition = resultCondition.or(seqCollection);
		}
		
//		for(Integer tagGrpSq : tagCtgyBaseMap.keySet()){
//			TagConditionBase conditionBase = tagCtgyBaseMap.get(tagGrpSq);
//			if(resultCondition==null)
//				resultCondition = conditionBase;
//			else
//				resultCondition = resultCondition.or(conditionBase);
//		}
		
		
		UsersDistributeByTag distributeByTag = cacheService.getUsersDistributeByTag(resultCondition,null);
		Map<Integer, Long> tagCountMap = distributeByTag.getTagToUserCountMap();
		
//		long sumResult = cacheService.getNumOfUsersByCondition(resultCondition);
		long sumResult = 0;
		Map<String,Map<String,Object>> tagValiMap = new HashMap<>();
//		Map<String,List<Map<String,Object>>> tagValiMap = new HashMap<>();
		Map<String,String> tagCtgyNmMap = new HashMap<>();
		
		for(Map<String,Object> tagRelaData:tagRelaList){
			Integer tagId = (Integer)tagRelaData.get("tagId");
			String tagNm = (String)tagRelaData.get("tagNm");
			String tagCtgyId = (String)tagRelaData.get("tagCtgyId");
			String tagTyp = (String)tagRelaData.get("tagTyp");
			Map<String, Object> tagCtgyMaptmp = null;
			if(tagValiMap.containsKey(tagCtgyId)){
				tagCtgyMaptmp = tagValiMap.get(tagCtgyId);
				List<Map<String,Object>> tagList = (List<Map<String, Object>>) tagCtgyMaptmp.get("tagList");
				Map tagMap = new HashMap();
				tagMap.put("tagId", tagId);
				tagMap.put("tagNm", tagNm);
				tagMap.put("tagCodiCnt", tagCountMap.get(tagId));
				tagList.add(tagMap);
			}else{
				tagCtgyMaptmp = new HashMap<>();
				tagCtgyMaptmp.put("tagCtgyId", tagCtgyId);
				tagCtgyMaptmp.put("tagCtgyNm", tagTyp);
				
				Map tagMap = new HashMap();
				tagMap.put("tagId", tagId);
				tagMap.put("tagNm", tagNm);
				tagMap.put("tagCodiCnt", tagCountMap.get(tagId));
				tagCtgyMaptmp.put("tagCodiCnt", tagCountMap.get(tagId));
				List<Map<String,Object>> tagList = new ArrayList<Map<String,Object>>();
				tagList.add(tagMap);
				tagCtgyMaptmp.put("tagList", tagList);
				tagValiMap.put(tagCtgyId, tagCtgyMaptmp);
			}
		}
				
		Collection<Map<String, Object>> valus = tagValiMap.values();
		Map resultMap = new HashMap();
		resultMap.put("allCnt", sumResult);
		resultMap.put("tagCtgyList", valus);
		
		
		
//		System.out.println(objectToJson(resultMap));
		
		return objectToJson(resultMap);
	}
}
