package com.seabox.tagsys.camp.service;

import java.util.Map;

/**
 * 
 * 活动管理service层
 * @author SongChaoqun
 * @since 2016-01-12
 *
 */
public interface CampViewService {
	String getEvtViewData(Map<String,Object> paramMap);
	
	/**
	 * 获取活动详情数据
	 * campId:活动详情ID
	 * campNm:活动详情名称
	 * startDt:活动开始日期
	 * endDt:活动结束日期
	 * hitCount:人群数量,
	 * campChnlId:活动渠道ID
	 * campChnlNm:活动渠道名称
	 * */
	String getDetailData(Map<String,Object> paramMap);
	
	/**
	 * 获取活动对应标签数据
	 * tagGrpSeq:标签组ID,
	 * tagGrpDetail:[
	 * 	tagCtgyNm:标签类别名称,
	 *  tagCtgyId:标签类别ID
	 *  tagGrpDetail:{
	 * 		tagId:标签ID,
	 * 		tagNm:标签名称,
	 * 		tagTyp:标签类别名称
	 * 	}
	 * ]
	 * 
	 * */
	String getLabelData(Map<String,Object> paramMap);
	
	/**
	 * 获取投放情况数据
	 * JSON格式:
	 * sendSucsCnt:发送成功数量,
	 * pushSucsRate:推送成功占比,
	 * deliverySucsCnt:成功送达数量,
	 * deliverySucsRate:成功送达占比,
	 * msgSucsCnt:成功短信数量,
	 * repMsgCnt:响应短信效果
	 * */
	String getSendData(Map<String,Object> paramMap);
	
	boolean copyEvtData(Map<String,Object> paramMap);
	
	boolean editSave(Map<String,Object> paramMap);
	
//	boolean addSave(Map<String,Object> paramMap);
	
	boolean delEvtData(Map<String,Object> paramMap);
	
	boolean stopEvtData(Map<String,Object> paramMap);
	
	/**
	 * 获取标签数据,包括标签收藏夹数据和标签市场数据
	 * 报文格式:
	 * favTagList:{
	 * 	tagId:标签ID,
	 * 	tagNm:标签名称,
	 * 	parTagId:上级标签类别ID,
	 * 	parTagNm:上级标签类别名称,
	 * 	sonCnt:下级标签/标签类别个数
	 * }
	 * marketTagList:{
	 * 	tagId:标签ID,
	 * 	tagNm:标签名称,
	 * 	parTagId:上级标签类别ID,
	 * 	parTagNm:上级标签类别名称,
	 * 	sonCnt:下级标签/标签类别个数
	 * }
	 * @param paramMap
	 * @return
	 */
	String listTagData(Map<String,Object> paramMap);
	
	/**
	 * 实时计算标签命中人群数
	 * @param paramMap
	 * @return
	 */
	long sumIndvNum(Map<String,Object> paramMap);
	
	String listCampAndTags(Map<String,Object> paramMap);
	
	/**
	 * 获取短信模板内容
	 * 报文格式:
	 * sms_tmp_id:短信模板ID
	 * msg_params:[{ paramName:参数名称,paramValue:参数值}], 
	 * @param paramMap
	 * @return
	 */
	String getCampTmpData(Map<String,Object> paramMap);

	/**
	 * 通过&拒绝活动
	 * @param paramMap
	 * @return
	 */
	boolean workflowCamp(Map<String, Object> paramMap);

	String getIndsAndChnlData(Map<String, Object> paramMap);

	/**
	 * 获取标签透视图数据
	 * @return
	 */
	String listTagValiData(Map<String, Object> paramMap);
}
