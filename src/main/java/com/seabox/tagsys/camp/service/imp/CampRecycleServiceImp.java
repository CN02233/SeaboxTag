package com.seabox.tagsys.camp.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seabox.tagsys.camp.service.CampRecycleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.sys.entity.PageBean;

@Service("campRecycleService")
public class CampRecycleServiceImp  extends BaseService implements CampRecycleService {

	private Map<String,String> realOrderColum = new HashMap<String,String>();
	
	@Override
	public String listMainData(Map<String, Object> paramMap) {
		String order_id = (String) paramMap.get("order_id");
		String orderId = realOrderColum(order_id);
		paramMap.put("orderId", orderId);
		
		Integer userId = (Integer) paramMap.get("user_id");
		
		Map resultMap = new HashMap();
		resultMap.put("userId", userId);
		
		//判断当前用户是否为组管理员
		Integer cntNum = relationDbTemplate.selectOne("campView.isGrpAdmin", paramMap);
		PageBean<Map<String,Object>> pageBean = null;
		List<Map<String,Object>> tagList = null;
		if(cntNum>0){//当前用户为组管理员
			pageBean = relationDbTemplate.selectListForPage("campRecycle.listEvtViewAd","campRecycle.cntEvtAdList",paramMap);
//			resultMap.put("isMa", true);
			tagList = relationDbTemplate.selectList("campRecycle.grpTagNmsAd",paramMap);
		}else{
			pageBean = relationDbTemplate.selectListForPage("campRecycle.listCampView","campRecycle.cntCampList",paramMap);
//			resultMap.put("isMa", false);
			tagList = relationDbTemplate.selectList("campRecycle.grpTagNms",paramMap);
		}
		
		Map<Integer,String> tagMap = new HashMap<Integer,String>();
		for(Map<String,Object> tag:tagList){
			Integer campId = (Integer) tag.get("campId");
			String tagNms = (String) tag.get("tagNms");
			tagMap.put(campId, tagNms);
		}
		
		List<Map<String, Object>> campList = pageBean.getList();
		for(Map<String,Object> campData:campList){
			Integer campId = (Integer) campData.get("campId");
			String tagNms = tagMap.get(campId);
			if(tagNms!=null&&!"".equals(tagNms))
				campData.put("ppDesc", tagNms);
		}
		
		resultMap.put("pageSize", pageBean.getPageSize());
		resultMap.put("totalNum", pageBean.getTotalNum());
		resultMap.put("currPage", pageBean.getCurrPage());
		resultMap.put("list", campList);
		
//		PageBean<Map<String,Object>> pageBean = relationDbTemplate.selectListForPage("campRecycle.listCampView","campRecycle.cntCampList",paramMap);
		
		return objectToJson(resultMap);
	}
	
	
	private String realOrderColum(String orderId){
		if(realOrderColum.size()<1){
//			realOrderColum.put("campId", "camp_id");
//			realOrderColum.put("campNm", "camp_nm");
//			realOrderColum.put("campDesc", "camp_desc");
//			realOrderColum.put("startDate", "stat_dt");
//			realOrderColum.put("endDate", "end_dt");
//			realOrderColum.put("hitCount", "indv_num");
//			realOrderColum.put("campType", "camp_chnl_cd");
//			realOrderColum.put("status", "camp_status_cd");
//			realOrderColum.put("createUser", "user_id");
//			realOrderColum.put("createDate", "created_ts");
//			realOrderColum.put("updateDate", "updated_ts");
//			realOrderColum.put("userId", "user_id");
			
			realOrderColum.put("campId", "camp_id");
			realOrderColum.put("campNm", "camp_nm");
			realOrderColum.put("campDesc", "camp_desc");
			realOrderColum.put("startDate", "start_dt");
			realOrderColum.put("endDate", "end_dt");
			realOrderColum.put("hitCount", "indv_num");
			realOrderColum.put("campType", "camp_chnl_cd");
			realOrderColum.put("status", "camp_status_cd");
			realOrderColum.put("createUser", "user_id");
			realOrderColum.put("createDate", "created_ts");
			realOrderColum.put("updateDate", "updated_ts");
			realOrderColum.put("userId", "user_id");
			
		}
		return realOrderColum.get(orderId);
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean rollbackData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		relationDbTemplate.insert("campRecycle.rollbackCamp", paramMap);//将回收站数据恢复到正式表中
		
		relationDbTemplate.delete("campRecycle.removeCampRec", paramMap);//删除回收站中数据
		
//		List<String> campList = (List<String>) paramMap.get("json_data");
//		for(String campId : campList){
//			Map campMap = new HashMap();
//			campMap.put("camp_id", campId);
//			relationDbTemplate.insert("campRecycle.rollbackCamp", paramMap);//将回收站数据恢复到正式表中
//			
//			relationDbTemplate.delete("campRecycle.removeCampRec", campMap);//删除回收站中数据
//		}
		
		return false;
	}


	@Override
	public boolean deleteCamp(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		relationDbTemplate.update("campRecycle.updateDelStat", paramMap);//将回收站中数据置为彻底删除状态(软删除)
		
		return false;
	}
}
