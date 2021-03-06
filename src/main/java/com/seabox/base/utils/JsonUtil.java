/**
 * 
 */
package com.seabox.base.utils;

import java.util.Map;

import com.seabox.tagsys.sys.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description
 * 
 * @author shibh
 * @create date 2015年12月25日
 * @version 0.0.1
 */
public class JsonUtil {
	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	@SuppressWarnings("unchecked")
	public static Map<String, Object> string2Map(String json) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(json, Map.class);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("0003", "json解析错误");
		}
	}

	public static String map2Json(@SuppressWarnings("rawtypes") Map map) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.debug("json转换失败" + e);
			e.printStackTrace();
			throw new BaseException("0003", "json解析错误");
		}
	}
}
