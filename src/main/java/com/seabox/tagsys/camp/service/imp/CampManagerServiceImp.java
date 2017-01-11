package com.seabox.tagsys.camp.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.usertags.logicquery.impl.Match;
import com.seabox.tagsys.usertags.logicquery.impl.TagConditionBase;
import com.seabox.sms.SmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seabox.tagsys.camp.service.CampManagerService;
import com.seabox.tagsys.usertags.service.CacheService;

@Service("enventManagerService")
public class CampManagerServiceImp extends BaseService implements CampManagerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CampManagerServiceImp.class);
	
//	@Autowired //本地无法调用dubbo服务提供者,需要将程序发布到阿里云上才可以。
	private SmsClient smsClient;
	
//	@Autowired
	private CacheService cacheService;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean addSave(Map<String, Object> paramMap) {
		
		Map<String,Object> json_data = (Map)paramMap.get("json_data");
		
		logger.info("start add save camp,camp id is "+json_data.get("camp_id"));
		
		Date startDate = new Date();
		String stats = (String) json_data.get("camp_status_cd");
		if(stats!=null&&"02".equals(stats)){//当前操作为提交操作,判断当前用户是否为管理员,如果是管理员则直接将状态置为分布状态
			Integer userId = (Integer) paramMap.get("user_id");
			//判断当前用户是否为组管理员
			Integer cntNum = relationDbTemplate.selectOne("campView.isGrpAdmin", paramMap);
			if(cntNum>0){//当前用户为组管理员
				logger.info("user is admin,submit camp : "+json_data.get("camp_id"));
				json_data.put("camp_status_cd","03");//将新建活动状态置为初始
			}
		}else{
			logger.info("camp is save operation : "+json_data.get("camp_id"));
			json_data.put("camp_status_cd","01");//将新建活动状态置为初始
		}
		
		json_data.put("created_ts", startDate);
		json_data.put("updated_ts", startDate);
		json_data.put("user_id", paramMap.get("user_id"));
		relationDbTemplate.insert("campManage.saveCamp", json_data);//保存活动信息
		
		List<List<Map<String,Object>>> campTagDataList = (List<List<Map<String,Object>>>) json_data.get("tag_grp");
		int tag_group_seq = 1;
		
		TagConditionBase resultCondition = null;
		
		logger.info("camp tag array is : "+objectToJson(campTagDataList));
		
		for(List<Map<String,Object>> campTagData:campTagDataList){
			Map<String,TagConditionBase> tagCtgyMap = new HashMap<>();
//			Map<String,TagConditionBase> tagCtgyMap = new HashMap<>();
			for(Map<String,Object> tagData:campTagData){//组
				tagData.put("tag_group_seq", tag_group_seq);
				tagData.put("camp_id", json_data.get("camp_id"));
				tagData.put("created_ts", startDate);
				tagData.put("updated_ts", startDate);
				relationDbTemplate.insert("campManage.saveCampTag",tagData);//保存活动&标签对应关系
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
		logger.info("check call cache interface is not or yes : "+json_data.get("camp_id"));
		if(stats!=null&&"02".equals(stats)){//当前操作为提交操作,判断当前用户是否为管理员,如果是管理员则直接将状态置为分布状态
			//活动提交,调用巫长华接口,生成发送名单
			String campIdStr = (String) json_data.get("camp_id");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			logger.info("开始调用cache接口:"+campIdStr+"--------------"+format.format(new Date()));
			int campIdInt = new Integer(campIdStr).intValue();
			cacheService.commitAndSaveCacheForCampaign(campIdInt, resultCondition,null);
			logger.info("调用cache接口完成:"+campIdStr+"--------------"+format.format(new Date()));
		}
		
		logger.info("check call cache interface was done : "+json_data.get("camp_id"));
		
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
			logger.info("start save camp infomation : "+json_data.get("camp_id"));
			relationDbTemplate.insert("campManage.saveCampTep",campTepMap);//保存活动&短信模板内容
			logger.info("camp infomation save was done : "+json_data.get("camp_id"));
			
			List<Map<String,Object>> paramList = (List<Map<String, Object>>) smsDataMap.get("msg_params");
			int i = 1;
			for(Map<String,Object> param : paramList){
				String paramName = (String) param.get("paramName");
				String paramValue = (String) param.get("paramValue");
				
				if(paramValue!=null&&paramValue.length()>0){
					Map<String,Object> campParamMap = new HashMap<String,Object>();
					campParamMap.put("camp_id", json_data.get("camp_id"));
					campParamMap.put("templt_id", smsTmpId);
					campParamMap.put("templt_para_nm", paramName);
					campParamMap.put("templt_para_value", paramValue);
					campParamMap.put("created_ts", startDate);
					campParamMap.put("updated_ts", startDate);
					campParamMap.put("templt_para_seq", i);
					logger.info("start save camp templace params infomation , camp id is: "+json_data.get("camp_id")+" | param info is :"+objectToJson(campParamMap));
					relationDbTemplate.insert("campManage.saveCampTepPars",campParamMap);//保存模板&参数内容
					logger.info("params info has inserted,param info is "+objectToJson(campParamMap));
					i++;
				}
				
			}
		}
		
		return true;
	}

	@Override
	public boolean submitCamp(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * 获取短信预览内容
	 */
	public String viewTestMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Map<String, Object> jsnMap = (Map<String,Object>)paramMap.get("json_data");
		String sms_tmp_id = (String) jsnMap.get("sms_tmp_id");
		Map smsContMap = new HashMap();
		smsContMap.put("sms_tmp_id", sms_tmp_id);
		
		logger.info("开始获取短信模板内容:"+sms_tmp_id);
		
		Map tempContent = getSmsCont(smsContMap);
		
		logger.info("短信模板内容获取完成:"+sms_tmp_id+"|"+objectToJson(tempContent));
		
		if(tempContent==null){
			return "";
		}
		String modelContent = (String)tempContent.get("model_content");
		
//		String modelContent = "恒大旗下互联网金融平台重磅上线！恒大地产业主专享:注册即送1000元现金礼包！新手专享预期年化收益高达10%！http://t.cn/RGBA5g1 立即注册领取礼包！退订回复TD";
		
		if(modelContent.indexOf("{")<1&&modelContent.indexOf("}")<1){
			return modelContent;
		}
		
		List<Map<String,String>> msgParamList = (List<Map<String,String>>)jsnMap.get("msg_params");
		
		int strintStart = 0;
		logger.info("拼装测试短信内容:"+sms_tmp_id);
		for(Map<String,String> msgParamMap : msgParamList){
			StringBuffer resultBuffer = new StringBuffer();
			String paramValue = msgParamMap.get("paramValue");
			
			if("".equals(paramValue))
				continue;
			
			if(paramValue.indexOf(" ")>0){
				
				char[] paramChars = paramValue.toCharArray();
				
				StringBuffer buffer = new StringBuffer();
				for(int i=0;i<paramChars.length;i++){
					if(paramChars[i]==' '){
						buffer.append("&nbsp;");
					}else
						buffer.append(paramChars[i]);
				}
				
				paramValue = buffer.toString();
			}
			
			logger.info("参数值为:"+paramValue);
			
			if(paramValue.equalsIgnoreCase("#username#")){
				paramValue = (String) paramMap.get("userNm");
			} 
			if(paramValue.equalsIgnoreCase("#point_current#")){
				paramValue = "0";
			}
			int parmStart = modelContent.indexOf("{");
			int parmEnd = modelContent.indexOf("}");
			String firstPart = modelContent.substring(strintStart, parmStart);
			String secondPart = modelContent.substring(parmEnd+1);
			resultBuffer.append(firstPart);
			resultBuffer.append(paramValue);
			resultBuffer.append(secondPart);
			modelContent = resultBuffer.toString();
			
		}
		logger.info("拼装测试短信内容完成:"+sms_tmp_id);
		return modelContent;
	}


	/**
	 * 发送测试短信
	 */
	@Override
	public List<Map<String,Object>> sendTestMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
//		String msgContent = viewTestMsg(paramMap);
		Map<String, Object> jsnMap = (Map<String,Object>)paramMap.get("json_data");
		List<Map<String,String>> msgParamList = (List<Map<String,String>>)jsnMap.get("msg_params");
		
		StringBuffer paramBuffer = new StringBuffer();
		for(Map<String,String> msgParamMap : msgParamList){
			String paramValue = msgParamMap.get("paramValue");
			
			if("#username#".equals(paramValue)){
				paramValue = (String) paramMap.get("userNm");
			} 
			if("#point_current#".equals(paramValue)){
				paramValue = "0";
			}
			if(paramBuffer.length()<1){
				paramBuffer.append(paramValue);
			}else{
				paramBuffer.append("~");
				paramBuffer.append(paramValue);
			}
			
		}
		String msgContent = paramBuffer.toString();
		Map<String,Object> jsnData = (Map<String, Object>) paramMap.get("json_data");
		String test_nums = (String) jsnData.get("test_nums");
		String[] numsArray = test_nums.split(",");
		
		List<Map<String,Object>> sendResultList = new ArrayList<>();
		for(int i=0;i<numsArray.length;i++){
//			if(smsClient==null){
//				return true;
//			}else{
				String model_code = (String) jsnData.get("sms_tmp_id");
				logger.info("开始发送测试短信,测试手机号:"+numsArray[i]);
//				Map<String, Object> sendResult = smsClient.sendMessage(model_code, msgContent, numsArray[i]);
				Map<String, Object> sendResult = null;
				logger.info("测试手机号:"+numsArray[i]+"的测试短信发送完成,发送结果为:"+objectToJson(sendResult));
				Map<String,Object> sendResltMap = new HashMap<String,Object>();
				sendResltMap.put("phoneNo", numsArray[i]);
				sendResltMap.put("sendMsg", sendResult.get("description"));
				sendResultList.add(sendResltMap);
//				sendResltMap.put(numsArray[i], (String)sendResult.get("description"));
//			}
		}
		return sendResultList;
	}
	
	@Override
	public int getCampId() {
		int campId = relationDbTemplate.selectOne("campManage.getCampId");
		return campId;
	}

	@Override
	public Map getSmsCont(Map<String, Object> paramMap) {
		String model_code = (String) paramMap.get("sms_tmp_id");
//		if(smsClient==null){
//			//data.result+","+data.desc+","+data.model_content
//			Map resultMap = new HashMap();
//			resultMap.put("result", "YES");
//			String resultMsg = "本地JAVA:尊敬的{xxxxxxxxxxxx},您的订单已收到，有效期为{xxxxxxxx},请及时使用";
//			if(!"001000001".equals(model_code)){
//				resultMsg = "本地JAVA:这是一条测试短信,短信参数第一个:{xxxxxxxxxxxx},短信参数第二个:{xxxxxxxx},短信参数第三个:{xxxxxxxx},测试短信内容完毕,字数字数字数";
//			}
////			String resultMsg = "尊敬的:张三,您的订单已收到，有效期为100天,请及时使用";
//			
//			String[] resultMsgs = resultMsg.split("\\{");
//			List paramList = new ArrayList();
//			for(int i=0;i<resultMsgs.length;i++){
//				String splitMsg = resultMsgs[i];
//				if(splitMsg.indexOf("}")>0){
//					paramList.add(splitMsg.subSequence(0, splitMsg.indexOf("}")));
//				}
//			}
//			resultMap.put("model_content", resultMsg);
//			if(paramList!=null&&paramList.size()>0)
//				resultMap.put("paramList", paramList);
//			
//			return resultMap;
//		}else{
			Map resultMap = new HashMap();
//			Map<String, Object> smsContentMap = smsClient.getModelContentByNo(model_code);
			Map<String, Object> smsContentMap = null;
			String resultMsg = (String) smsContentMap.get("model_content");
//			String resultMsg = "恒大旗下互联网金融平台重磅上线！恒大地产业主专享:注册即送1000元现金礼包！新手专享预期年化收益高达10%！http://t.cn/RGBA5g1 立即注册领取礼包！退订回复TD";
			if(resultMsg==null||"".equals(resultMsg)){
				return null;
			}
			String[] resultMsgs = resultMsg.split("\\{");
			List paramList = new ArrayList();
			for(int i=0;i<resultMsgs.length;i++){
				String splitMsg = resultMsgs[i];
				if(splitMsg.indexOf("}")>0){
					paramList.add(splitMsg.subSequence(0, splitMsg.indexOf("}")));
				}
			}
			resultMap.put("model_content", resultMsg);
			if(paramList!=null&&paramList.size()>0)
				resultMap.put("paramList", paramList);
			
			return resultMap;
//		}
		
	}

}
