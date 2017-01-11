package com.seabox.sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



/**
 * 
 * Description 短信平台配置
 * @author shibh
 * @date 2016年1月25日
 */
public class SmsClient{
//	private SMSService sMSService;
//	/**
//	 * 产生一个固定格式的请求
//	 * 渠道号由短信平台统一分配
//	 * 流水号由各渠道自己定义，长度小于20位的数字
//	 * @return
//	 */
//	private SmsRequest getSmsRequest(Map<String,Object> map){
//		System.out.println("getSmsRequest:"+map);
//		SmsRequest request=new SmsRequest();
//		Map<String,Object> headerMap = new HashMap<String,Object>();
//		headerMap.put("channel_code", "001");
//		headerMap.put("serial_no", "100000001");
//		headerMap.put("tran_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis())));
//		request.setRequestHead(headerMap);
//		request.setRequestBody(map);
//		return 	request;
//	}
//	/**
//	 * 发送短信
//	 * @param
//	 */
//	public Map<String,Object> sendMessage(String model_code,String message,String mobile_no){
//		return sendMessage(model_code, message, mobile_no, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis())), "");
//	}
//	/**
//	 * 发送短信
//	 * @param
//	 */
//	public Map<String,Object> sendMessage(String model_code,String message,String mobile_no,String send_time,String send_type){
//		Map<String,Object> requestBody=new HashMap<String,Object>();
//		requestBody.put("mobile_no", mobile_no);
//		requestBody.put("message", message);
//		requestBody.put("model_code", model_code);
//		requestBody.put("send_time", send_time);
//		requestBody.put("send_type", send_type);
//		requestBody.put("extendaccessnum", "0");//扩展号不加会导致联通的手机号没法收到测试短信
//		SmsResponse response=sMSService.sendMessage(getSmsRequest(requestBody));
//		return responseToMap(response);
//	}
//	/**
//	 * 查询回执
//	 * @return
//	 */
//	public Map<String,Object> report(){
//		SmsResponse response=sMSService.report(getSmsRequest(null));
//		return responseToMap(response);
//	}
//	/**
//	 * 查询回复
//	 * @return
//	 */
//	public Map<String,Object> reply(){
//		SmsResponse response=sMSService.reply(getSmsRequest(null));
//		return responseToMap(response);
//	}
//	/**
//	 * 查询短信模板
//	 * @param model_code
//	 * @return
//	 */
//	public Map<String,Object> getModelContentByNo(String model_code){
//		Map<String,Object> requestBody=new HashMap<String,Object>();
//		requestBody.put("model_code", model_code);
//		SmsResponse response=sMSService.getModelContentByNo(getSmsRequest(requestBody));
//		return responseToMap(response);
//	}
//	private Map<String,Object> responseToMap(SmsResponse response){
//		Map<String,Object> map=new HashMap<String,Object>();
//		if(response.getResponseBody()!=null){
//			map.putAll(response.getResponseBody());
//		}
//		if(response.getResponseHead()!=null){
//			map.putAll(response.getResponseHead());
//		}
//		return map;
//	}
//	public SMSService getsMSService() {
//		return sMSService;
//	}
//	public void setsMSService(SMSService sMSService) {
//		this.sMSService = sMSService;
//	}
	
}
