package com.seabox.tagsys.event.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.seabox.tagsys.camp.service.CampRecycleService;
import org.junit.Ignore;
import org.junit.Test;

import com.seabox.test.base.BaseTestClass;

@Ignore
public class CampRecycleServiceImpTest extends BaseTestClass {
	
	@Resource
	CampRecycleService test;
	
	@Test
	public void rollbackDataTest(){
		Map paramMap = new HashMap();
		
		List campIds = new ArrayList();
		campIds.add("320160116151649000000");
		campIds.add("320160116151653000000");
		campIds.add("320160116151656000000");
		paramMap.put("json_data", campIds);
		test.rollbackData(paramMap);
	}
	
}
