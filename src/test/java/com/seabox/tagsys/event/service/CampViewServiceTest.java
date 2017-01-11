package com.seabox.tagsys.event.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.seabox.tagsys.camp.service.CampViewService;
import org.junit.Ignore;
import org.junit.Test;

import com.seabox.test.base.BaseTestClass;

@Ignore
public class CampViewServiceTest extends BaseTestClass {

	@Resource
	private CampViewService enventViewService;

	@Test
	public void getEvtViewDataTest() {
		enventViewService.getDetailData(null);
	}
	
	@Test
	public void getLabelDataTest(){
		Map paramMap= new HashMap();
		paramMap.put("campId", "10");
		String resultJson = enventViewService.getLabelData(paramMap);
		System.out.println(resultJson);
	}
	
	@Test
	public void listTagDataTest(){
		Map pramMap = new HashMap();
		pramMap.put("userId", 1);
		String resultJson = enventViewService.listTagData(pramMap);
		System.out.println(resultJson);
	}
	
	@Test
	public void listCampAndTags(){
		Map pramMap = new HashMap();
		pramMap.put("campId", "120160126111645679");
		String resultJson = enventViewService.listCampAndTags(pramMap);
		System.out.println(resultJson);
	}
	
	@Test
	public void getCampTmpDataTest(){
		Map pramMap = new HashMap();
		pramMap.put("campId", "220160128164513200");
		String resultJson = enventViewService.getCampTmpData(pramMap);
		System.out.println(resultJson);
	}
	
	@Test
	public void sumIndvNumTest(){
		//[{"tag_id":"4","tag_ctgy_id":"4"},{"tag_id":"6","tag_ctgy_id":"4"},{"tag_id":"1","tag_ctgy_id":"3"},{"tag_id":"9","tag_ctgy_id":"5"}]
		
		List<Map<String,String>> paramList = new ArrayList<>();
		
//		Map paramMap1 = new HashMap();
//		paramMap1.put("tag_id", "4");
//		paramMap1.put("tag_ctgy_id", "4");
//		Map paramMap2 = new HashMap();
//		paramMap2.put("tag_id", "6");
//		paramMap2.put("tag_ctgy_id", "4");
//		Map paramMap3 = new HashMap();
//		paramMap3.put("tag_id", "1");
//		paramMap3.put("tag_ctgy_id", "3");
//		Map paramMap4 = new HashMap();
//		paramMap4.put("tag_id", "9");
//		paramMap4.put("tag_ctgy_id", "5");
//		paramList.add(paramMap1);
//		paramList.add(paramMap2);
//		paramList.add(paramMap3);
//		paramList.add(paramMap4);
		Map jsonMap = new HashMap();
//		jsonMap.put("json_data", paramList);
		
		Map paramMap0 = new HashMap();
		paramMap0.put("tag_id", "100054");
		paramMap0.put("tag_ctgy_id", "4");
		paramList.add(paramMap0);
		jsonMap.put("json_data", paramList);
		
		enventViewService.sumIndvNum(jsonMap);
	}
	
	@Test
	public void getSendDataTest(){
		Map paramMap = new HashMap();
		paramMap.put("campId", 11);
		System.out.println(enventViewService.getSendData(paramMap));
	}
	
	@Test
	public void listTagValiDataTest(){
		Map paramMap = new HashMap();
		paramMap.put("campId", 29);
		enventViewService.listTagValiData(paramMap);
//		System.out.println(enventViewService.listTagValiData(paramMap));
	}
}
