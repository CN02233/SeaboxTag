package com.seabox.tagsys.camp.service;

import java.util.Map;

public interface CampRecycleService {

	String listMainData(Map<String, Object> paramMap);

	boolean rollbackData(Map<String, Object> paramMap);

	boolean deleteCamp(Map<String, Object> paramMap);

}
