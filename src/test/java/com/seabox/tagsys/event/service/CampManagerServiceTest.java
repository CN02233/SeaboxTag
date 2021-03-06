package com.seabox.tagsys.event.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.seabox.tagsys.camp.service.CampManagerService;
import com.seabox.test.base.BaseTestClass;

@Ignore
public class CampManagerServiceTest extends BaseTestClass {
	
	@Autowired
	private CampManagerService testService ;
	
	@Test
	public void addSaveTest(){
		
		Map paramMap = new HashMap();
		paramMap.put("camp_id", "220160128164239258");
		paramMap.put("camp_nm", "测试20160128");
		paramMap.put("camp_desc", "测试20160128");
		paramMap.put("start_dt", "2016-01-21");
		paramMap.put("end_dt", "2016-01-28");
		paramMap.put("camp_chnl_cd", "1");
		
		List tagGrp1 = new ArrayList();
		Map tagMap0 = new HashMap();
		Map tagMap1 = new HashMap();
		tagMap0.put("tag_id", 4);
		tagMap0.put("tag_ctgy_id", 4);
		tagMap1.put("tag_id", 5);
		tagMap1.put("tag_ctgy_id", 4);
		tagGrp1.add(tagMap0);
		tagGrp1.add(tagMap1);
		
		List tagGrp2 = new ArrayList();
		Map tagMap2 = new HashMap();
		Map tagMap3 = new HashMap();
		tagMap2.put("tag_id", 7);
		tagMap2.put("tag_ctgy_id", 4);
		tagMap3.put("tag_id", 8);
		tagMap3.put("tag_ctgy_id", 4);
		tagGrp2.add(tagMap2);
		tagGrp2.add(tagMap3);
		
		List tag_grp = new ArrayList();
		tag_grp.add(tagGrp1);
		tag_grp.add(tagGrp2);
		
		paramMap.put("tag_grp", tag_grp);
		
		Map sms_tmp = new HashMap();
		sms_tmp.put("sms_tmp_id", "20160128TEST");
		
		List paramList = new ArrayList();
		Map paramMapTmp = new HashMap();
		paramMapTmp.put("paramName", "P_1");
		paramMapTmp.put("paramValue", "V_1");
		paramList.add(paramMapTmp);
		sms_tmp.put("msg_params", "20160128TEST");
		sms_tmp.put("msg_params", paramList);
		
		paramMap.put("sms_tmp", sms_tmp);
		
		Map sendMap = new HashMap();
		sendMap.put("json_data", paramMap);
		
		testService.addSave(sendMap);
	}
}
