package com.seabox.tagsys.persongroup.service;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.persongroup.utils.TagGrpToCondition;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.usertags.logicquery.UsersDistributeByTag;
import com.seabox.tagsys.usertags.logicquery.impl.TagConditionBase;
import com.seabox.tagsys.usertags.service.CacheService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SongChaoqun on 2016/11/16.
 */
@Service("personScreenService")
public class PersonScreenServiceImp extends BaseService{

    private ThreadLocal<List<Map<String,Object>>> tagCtgysListLocal = new ThreadLocal<>();

    private Map<String,String> realOrderColum = new HashMap<String,String>();

    @Autowired
    private CacheService cacheService;

    private  static final Logger logger = LoggerFactory.getLogger(PersonScreenServiceImp.class);

    public Map<String,List<Map<String,Object>>> listTagData(Map<String, Object> paramMap) {
        List<Map<String,Object>> tagCtgysList = relationDbTemplate.selectList("prnScreening.listTagCtgy", paramMap);
        List<Map<String,Object>> tagsList = relationDbTemplate.selectList("prnScreening.listTags", paramMap);

        List<Map<String,Object>> allTagsList = new ArrayList<>();
        allTagsList.addAll(tagCtgysList);
        allTagsList.addAll(tagsList);

        Map<String,List<Map<String,Object>>> tagTreeTmp = new HashMap<>();

        for(Map<String,Object> tagCtgyMap:allTagsList){
            String parTagId = (String)  tagCtgyMap.get("parTagId");
            if(tagTreeTmp.containsKey(parTagId)){
                List<Map<String, Object>> listTmp = tagTreeTmp.get(parTagId);
                listTmp.add(tagCtgyMap);
                tagTreeTmp.put(parTagId,listTmp);
            }else{
                List<Map<String, Object>> listTmp = new ArrayList<>();
                listTmp.add(tagCtgyMap);
                tagTreeTmp.put(parTagId,listTmp);
            }
        }


        return tagTreeTmp;
    }


    private List<Map<String,Object>> getMarketList(){

        List<Map<String,Object>> tagsList = relationDbTemplate.selectList("screeningView.listTags", null);//获取标签数据
        List<Map<String,Object>> tagCtgysList = relationDbTemplate.selectList("screeningView.listTagCtgys", null);//获取标签类别数据

        tagCtgysListLocal.set(tagCtgysList);

        List<Map<String,Object>> marketList = new ArrayList<>();
        Map<String,Map<String, Object>> tagCtgy = new LinkedHashMap<>();
        for(Map<String,Object> ctgyMap:tagCtgysList){
            String tagId = (String) ctgyMap.get("tagId");
            String tagNm = (String) ctgyMap.get("tagNm");
            String parTagId = (String) ctgyMap.get("parTagId");
            if(!tagCtgy.containsKey(tagId)){
                ctgyMap.put("sonCnt", 0);
                tagCtgy.put(tagId, ctgyMap);
            }
            else{
                Map ctgyTmpMap = (Map) tagCtgy.get(tagId);
                ctgyTmpMap.putAll(ctgyMap);
            }

            if(parTagId!=null&&!"".equals(parTagId)){
                if(!tagCtgy.containsKey(parTagId)){
                    if("prt_".equals(parTagId))
                        continue;
                    Map ctgyTmpMap = new HashMap();
                    ctgyTmpMap.put("sonCnt", 1);
                    tagCtgy.put(parTagId, ctgyTmpMap);
                }else{
                    Map ctgyTmpMap = (Map) tagCtgy.get(parTagId);
                    int sonCnt = 0;
                    if(ctgyTmpMap.containsKey("sonCnt")){
                        sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
                    }
                    ctgyTmpMap.put("sonCnt", ++sonCnt);
                }
            }

        }
        List<Map<String,Object>> resTagsList = new ArrayList<>();
        for(Map<String,Object> tagMap:tagsList){
            Integer tagId = (Integer) tagMap.get("tagId");
            String tagNm = (String) tagMap.get("tagNm");
            String parTagId = (String) tagMap.get("parTagId");
            if(tagCtgy.containsKey(parTagId)){
                Map ctgyTmpMap = (Map) tagCtgy.get(parTagId);
//				int sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
//				ctgyTmpMap.put("sonCnt", ++sonCnt);
                tagMap.put("tagCtgyNm", ctgyTmpMap.get("tagNm"));//父标签类别名称

                int sonCnt = 0;
                if(ctgyTmpMap.containsKey("sonCnt")){
                    sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
                }
                ctgyTmpMap.put("sonCnt", ++sonCnt);
                resTagsList.add(tagMap);
            }

        }
        marketList.addAll(tagCtgy.values());
        marketList.addAll(resTagsList);


        return marketList;
    }

    private List<Map<String,Object>> getFavList(Map<String,Object> paramMap){
        List<Map<String,Object>> favTagResultList = new ArrayList<>();

        List<Map<String,Object>> favTagList = relationDbTemplate.selectList("screeningView.listFavTags", paramMap);

        if(favTagList!=null&&favTagList.size()>0){}
        else return favTagList;

        Map<String,Map<String, Object>> tagCtgy = new LinkedHashMap<>();

        List<Map<String, Object>> tagCtgysList = tagCtgysListLocal.get();

        for(Map<String,Object> ctgyMap:tagCtgysList){
            String tagId = (String) ctgyMap.get("tagId");
            String tagNm = (String) ctgyMap.get("tagNm");
            String parTagId = (String) ctgyMap.get("parTagId");
            if(!tagCtgy.containsKey(tagId)){
                ctgyMap.put("sonCnt", 0);
                tagCtgy.put(tagId, ctgyMap);
            }
            else{
                Map ctgyTmpMap = (Map) tagCtgy.get(tagId);
                ctgyTmpMap.putAll(ctgyMap);
            }

            if(parTagId!=null&&!"".equals(parTagId)){
                if(!tagCtgy.containsKey(parTagId)){
                    if("prt_".equals(parTagId))
                        continue;
                    Map ctgyTmpMap = new HashMap();
                    ctgyTmpMap.put("sonCnt", 1);
                    tagCtgy.put(parTagId, ctgyTmpMap);
                }else{
                    Map ctgyTmpMap = (Map) tagCtgy.get(parTagId);
                    int sonCnt = 0;
                    if(ctgyTmpMap.containsKey("sonCnt")){
                        sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
                    }
                    ctgyTmpMap.put("sonCnt", ++sonCnt);
                }
            }
        }

        List<Map<String,Object>> tagCtgyFavList = new ArrayList<>();
        List<String> hasFindCtgyList = new ArrayList<>();
        List<Map<String,Object>> resTagsList = new ArrayList<>();
        for(Map<String,Object> tagMap:favTagList){
            Integer tagId = (Integer) tagMap.get("tagId");
            String tagNm = (String) tagMap.get("tagNm");
            String parTagId = (String) tagMap.get("parTagId");
            if(tagCtgy.containsKey(parTagId)){
                Map<String, Object> ctgyTmpMap = (Map<String, Object>) tagCtgy.get(parTagId);
                tagMap.put("tagCtgyNm", ctgyTmpMap.get("tagNm"));//父标签类别名称
                int sonCnt = 0;
                if(ctgyTmpMap.containsKey("sonCnt")){
                    sonCnt = (Integer)ctgyTmpMap.get("sonCnt");
                }
                ctgyTmpMap.put("sonCnt", ++sonCnt);

                Map<String, Object> parentTmp = ctgyTmpMap;
                if(hasFindCtgyList.contains(parTagId))
                    continue;
                else
                    hasFindCtgyList.add(parTagId);
                //根据父标签类别向上遍历找到整个树
                String parentTagIdTmp = (String) parentTmp.get("parTagId");

                while(true){
                    tagCtgyFavList.add(parentTmp);
                    parentTagIdTmp = (String) parentTmp.get("parTagId");
                    if(hasFindCtgyList.contains(parentTagIdTmp))
                        break;
                    hasFindCtgyList.add(parentTagIdTmp);
                    if(tagCtgy.containsKey(parentTagIdTmp)){
                        parentTmp = tagCtgy.get(parentTagIdTmp);
                    }else
                        break;
                }
                resTagsList.add(tagMap);

            }



        }
//		favTagResultList.addAll(tagCtgy.values());
        favTagResultList.addAll(tagCtgyFavList);
        favTagResultList.addAll(resTagsList);


        return favTagResultList;

    }

    public String listMainData(Map<String, Object> paramMap) {

        PageBean<Object> resultList = relationDbTemplate.selectListForPage("prnScreening.listAllScreening", "prnScreening.cntAllScreening", paramMap);

        return objectToJson(resultList);
    }


    @Transactional(rollbackFor=Exception.class)
    public Map<String,List<Map<String,Object>>> saveScreening(Map<String,Object> param) {

        int screeningId = getScreeningId();

        String screeningNm = (String) param.get("screeningNm");


        Map<String,Object> screeningMap = new HashMap<>();
        screeningMap.putAll(param);
        screeningMap.put("camp_id",screeningId);
        screeningMap.put("camp_nm",screeningNm);

        relationDbTemplate.insert("prnScreening.saveScreening",screeningMap);

        param.put("screeningId",screeningId);

        insertScreeningTags(param);
        return null;
    }

//    @Transactional(rollbackFor=Exception.class)
    public void insertScreeningTags(Map<String,Object> param){
        Map<String,Map<String,List<String>>> tagData = (Map<String, Map<String, List<String>>>) param.get("tagData");
        int tagGrpSeq = 1;
        for(String tagGrpNm : tagData.keySet()){
            Map<String, List<String>> tagCtgys = tagData.get(tagGrpNm);
            for(String tagCtgy :  tagCtgys.keySet()){
                List<String> tagList = tagCtgys.get(tagCtgy);
                for(String tag : tagList){
                    Map<String,Object> saveMap = new HashMap<>();
//                    (#{camp_id},#{tag_group_seq},#{tag_id},#{tag_ctgy_id},#{created_ts},#{updated_ts})
                    saveMap.put("tag_ctgy_id",tagCtgy);
                    saveMap.put("tag_id",tag);
                    saveMap.put("tag_group_seq", tagGrpSeq);
                    saveMap.put("camp_id", param.get("screeningId"));
                    relationDbTemplate.insert("prnScreening.saveScreeningTags",saveMap);
                }
            }
            tagGrpSeq++;
        }

    }

    /**
     * 获取筛选条件数据,包括筛选条件以及条件中的标签组
     * @param paramMap
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String,Object> getScreeningAndTags(Map<String, Object> paramMap) {
        Map<String,Object> screeningObj = relationDbTemplate.selectOne("prnScreening.getScreeningObj", paramMap);
        List<Map<String,Object>> tagList = relationDbTemplate.selectList("prnScreening.getScreeningTags",paramMap);

        Map<Integer,List<Map<String,Object>>> tagGrpMap = new HashMap<Integer,List<Map<String,Object>>>();
        for(Map<String,Object> tagGrpData:tagList){
            Integer tagGrpSq = (Integer)tagGrpData.get("grpSeq");
            List<Map<String, Object>> tagsList = null;
            if(tagGrpMap.containsKey(tagGrpSq)){
                tagsList = tagGrpMap.get(tagGrpSq);
            }else{
                tagsList = new ArrayList<>();
                tagGrpMap.put(tagGrpSq, tagsList);
            }
            tagsList.add(tagGrpData);
        }

        List<Map<String,Object>> tagGrpList = new ArrayList<>();
        for(Integer key : tagGrpMap.keySet()){
            Map<String,Object> tagGrpResTmpMap = new HashMap<>();
            tagGrpResTmpMap.put("grpSeq", key);
            tagGrpResTmpMap.put("tagList", tagGrpMap.get(key));
            tagGrpList.add(tagGrpResTmpMap);
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("screeningObj",screeningObj);
        resultMap.put("tagsObj",tagGrpList);
        return resultMap;
    }

    public int getScreeningId() {
        int screeningId = relationDbTemplate.selectOne("prnScreening.getScreeningId");
        return screeningId;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> updateScreening(Map<String, Object> paramMap) {

        relationDbTemplate.update("prnScreening.updateScreening",paramMap);

        relationDbTemplate.update("prnScreening.rmOldTagGrps",paramMap);

        insertScreeningTags(paramMap);

        return null;
    }

    /**
     * 删除活动,删除操作会将活动数据放到回收站表中
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delScreeningData(Map<String, Object> paramMap) {
        // TODO Auto-generated method stub
        relationDbTemplate.insert("prnScreening.moveToDelTb", paramMap);
        relationDbTemplate.delete("prnScreening.delScreeningData",paramMap);

        return true;
    }

    @Transactional(readOnly = true)
    public long sumPersonCount(Map<String, Object> pramMap) {
        TagConditionBase resultCondition = TagGrpToCondition.jsonMapToCondition(pramMap);
        UsersDistributeByTag result = cacheService.getUsersDistributeByTag(resultCondition, null);
        long userCont = result.getTotalNumOfUsers();
        logger.debug("\n"+objectToJson(pramMap)+"----->"+userCont);
        return userCont;
    }
}
