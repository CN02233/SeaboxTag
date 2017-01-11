package com.seabox.tagsys.base.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseController {
	public String viewPage(String action) {
		RequestMapping requestMapping = this.getClass().getAnnotation(
				RequestMapping.class);

		String[] requestPath = requestMapping.value();

		if (requestPath != null && requestPath.length > 0) {
			StringBuffer buffer = new StringBuffer();
			//buffer.append("/WEB-INF/jsp/");
			buffer.append(requestPath[0]);
			buffer.append("/");
			buffer.append(action);
			return buffer.toString();
		} else {
			return null;
		}
	}

	public Map<String, Object> initRequestParam(HttpServletRequest request) {
		Enumeration<String> requestNames = request.getParameterNames();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		while (requestNames.hasMoreElements()) {
			String paramId = requestNames.nextElement();
			resultMap.put(paramId, request.getParameter(paramId));
		}

		StringBuffer json = new StringBuffer();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					request.getInputStream(), "UTF-8"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine != null && !"".equals(inputLine))
					json.append(inputLine);
			}

			if (json.toString() != null && !"".equals(json.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				//Map jsonMap = new HashMap();
				try{
					Map<?, ?> resultJsonObj = mapper.readValue(json.toString(),Map.class);
					resultMap.put("json_data", resultJsonObj);
				}catch(Exception e){
					List<?> resultJsonObj = mapper.readValue(json.toString(), List.class);
					resultMap.put("json_data", resultJsonObj);
				}
			}
			resultMap.put("json_data_str", json.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("reqData", resultMap);
		return resultMap;
	}

}
