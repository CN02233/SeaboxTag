package com.seabox.tagsys.sys.http;


import java.util.Map;

import com.seabox.tagsys.sys.Constants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seabox.base.utils.JsonUtil;
import com.seabox.tagsys.sys.BaseException;

/**
 * Description  http请求工具类
 * @author shibh
 * @create date 2015年12月24日
 * @version 0.0.1
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class); 
	/**
	 * Description 发起get方法
	 * @param  url {@link #com.seabox.eview.sys.Constants}
	 * @return Map<String,Object>
	 * @throws
	 */
	public static Map<String,Object> getMethod(String url,Map<String,Object> reqMap)throws Exception{
		CloseableHttpClient httpclient=HttpClientManager.getHttpClient();
		CloseableHttpResponse response=null;
		try {
			HttpGet httpget = HttpClientManager.getHttpGet(Constants.HDI_AUTH_HOST+url, reqMap);
            System.out.println("executing request " + httpget.getURI());
            response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());
            String res=EntityUtils.toString(response.getEntity());
            return JsonUtil.string2Map(res);
        }catch(Exception e){
        	e.printStackTrace();
        	logger.error(""+e);
        	throw new BaseException("",e);
        }finally {
        	response.close();
            httpclient.close();
        }
		
	}
	/**
	 * 
	 * Description 发起post请求
	 * @param  url {@link #com.seabox.eview.sys.Constants} 各类请求的url统一定义
	 * @return Map<String,Object>
	 * @throws
	 */
	public static Map<String,Object> postMethod(String url,Map<String,Object> reqMap)throws Exception{
		CloseableHttpClient httpclient=HttpClientManager.getHttpClient();
		CloseableHttpResponse response=null;
		try{
            HttpPost httpPost = HttpClientManager.getHttpPost(Constants.HDI_AUTH_HOST+url);
            StringEntity stringEntity = new StringEntity(JsonUtil.map2Json(reqMap));
            httpPost.setEntity(stringEntity);
            System.out.println("executing request " + httpPost.getURI());
        	response = httpclient.execute(httpPost);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            String res=EntityUtils.toString(response.getEntity());
            System.out.println(res);
            System.out.println("----------------------------------------");
            return JsonUtil.string2Map(res);
        } finally {
        	response.close();
            httpclient.close();
        }
	}
	/**
	 * Description 发起delete请求
	 * @param  url {@link #com.seabox.eview.sys.Constants}
	 * @param reqMap 请求参数
	 * @return Map<String,Object>
	 * @throws
	 */
	public static Map<String,Object> deleteMethod(String url,Map<String,Object> reqMap)throws Exception{
		CloseableHttpClient httpclient=HttpClientManager.getHttpClient();
		CloseableHttpResponse response=null;
		try {
            HttpDeleteWithBody httpDelete = HttpClientManager.getHttpDelete(Constants.HDI_AUTH_HOST+url);
            StringEntity stringEntity=new StringEntity(JsonUtil.map2Json(reqMap));
            httpDelete.setEntity(stringEntity);
            System.out.println("executing request " + httpDelete.getURI());
        	response = httpclient.execute(httpDelete);
        	System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            String res=EntityUtils.toString(response.getEntity());
            System.out.println(res);
            System.out.println("----------------------------------------");
            return JsonUtil.string2Map(res);
        }catch(Exception e){
        	e.printStackTrace();
        	throw new BaseException("",e);
        } finally {
        	response.close();
            httpclient.close();
        }
	}
	

}
