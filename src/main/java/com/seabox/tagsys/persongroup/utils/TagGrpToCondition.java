package com.seabox.tagsys.persongroup.utils;

import com.seabox.tagsys.persongroup.entity.TagCtgyEntity;
import com.seabox.tagsys.persongroup.entity.TagGrpEntity;
import com.seabox.tagsys.usertags.logicquery.impl.Match;
import com.seabox.tagsys.usertags.logicquery.impl.TagConditionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 将标签组组成用于计算的Condition实体
 * Created by SongChaoqun on 2016/12/14.
 */
public final class TagGrpToCondition {

    private  static final Logger logger = LoggerFactory.getLogger(TagGrpToCondition.class);

    public static TagConditionBase getCondition(List tagGrpEntityList){

        logger.debug("start work :tag json data to tag condition-->"+tagGrpEntityList);

        TagConditionBase matchResult = null;
        for(Object tagObj:tagGrpEntityList){
            TagConditionBase tagCondition = null;

            if(tagObj instanceof TagGrpEntity){
//                List<TagCtgyEntity> tt = ((TagGrpEntity) tagObj).getTagCtgys();
//                tagCondition = getCondition(tt);
                tagCondition = getCondition(((TagGrpEntity) tagObj).getTagCtgys());

            }else if(tagObj instanceof TagCtgyEntity){
//                List<String> tt = ((TagCtgyEntity) tagObj).getTagIds();
//                tagCondition = getCondition(tt);
                tagCondition = getCondition(((TagCtgyEntity) tagObj).getTagIds());
            }else if(tagObj instanceof String){
                String tag = (String) tagObj;
                Match math = new Match();
                math.setTag(tag);
                math.setTagId(new Integer(tag));
                tagCondition = math;
            }

            if(matchResult==null)
                matchResult = tagCondition;
            else{
                if(tagObj instanceof TagCtgyEntity)
                    matchResult = matchResult.and(tagCondition);
                else
                    matchResult = matchResult.or(tagCondition);
            }
        }
        return matchResult;
    }

    /**
     * 用于将以下格式的json文本转成对象
     * {"grp_0":{"10203000":["100004","100005"],"10203001":["100001"],"11203000":["100012","100013"]}}
     * @param paramMap
     * @return
     */
    public static TagConditionBase jsonMapToCondition(Map<String,Object> paramMap){
        List<TagGrpEntity> tagGrpEntityList = new ArrayList<>();

        Set<String> grpSeqSet = paramMap.keySet();
        for(String grpSeq : grpSeqSet){
            Map<String,List<String>> tagCtgys = (Map<String, List<String>>) paramMap.get(grpSeq);
            Set<String> tagCtgySet = tagCtgys.keySet();

            TagGrpEntity tagGrpEntity = new TagGrpEntity();
            tagGrpEntity.setGrpSeq(grpSeq);

            List<TagCtgyEntity> tagCtgyEntityList = new ArrayList<>();
            tagGrpEntity.setTagCtgys(tagCtgyEntityList);
            tagGrpEntityList.add(tagGrpEntity);

            for(String tagCtgy : tagCtgySet){
                TagCtgyEntity tagCtgyEntity = new TagCtgyEntity();
                tagCtgyEntity.setTagCtgyId(tagCtgy);
                tagCtgyEntity.setTagIds( tagCtgys.get(tagCtgy));
                tagCtgyEntityList.add(tagCtgyEntity);
            }
        }

        return getCondition(tagGrpEntityList);

    }

    /**
     * 用于将从数据库中查询到的筛选条件对应的标签列表转成Condition对象
     * @return
     */
    public static TagConditionBase dbMapToCondition(List<Map<String,Object>> dbDataList){

        Map<String,Object> grpMap = new HashMap<>();

        for(Map<String,Object> dbDataMap : dbDataList){
            String grpSeq = dbDataMap.get("grpSeq").toString();
            String tagId = dbDataMap.get("tagId").toString();
            String tagCtgyId = dbDataMap.get("tagCtgyId").toString();
            if(grpMap.containsKey(grpSeq)){
                Map<String, List<String>> ctgysMap = (Map<String, List<String>>) grpMap.get(grpSeq);
                if(ctgysMap.containsKey(tagCtgyId)){
                    List<String> tagList = ctgysMap.get(tagCtgyId);
                    tagList.add(tagId);
                }else{
                    List<String> tagList = new ArrayList<>();
                    tagList.add(tagId);
                    ctgysMap.put(tagCtgyId,tagList);
                }
            }else{
                Map<String, List<String>> ctgysMap = new HashMap<>();
                List<String> tagList = new ArrayList<>();
                tagList.add(tagId);
                ctgysMap.put(tagCtgyId,tagList);
                grpMap.put(grpSeq,ctgysMap);
            }
        }

        TagConditionBase condition = jsonMapToCondition(grpMap);

        return condition;
    }

//    public static void getCondition1(List<TagGrpEntity> tagGrpEntityList){
//        TagConditionBase matchResult = null;
//        for(TagGrpEntity tagGrp:tagGrpEntityList){
//
//            List<TagCtgyEntity> tagCtgys = tagGrp.getTagCtgys();
//            TagConditionBase tagCondition = getCtgyCondtionF4SameGrp(tagCtgys);
//
//            if(matchResult==null)
//                matchResult = tagCondition;
//            else{
//                matchResult.and(tagCondition);
//            }
//        }
//
//    }
//
//    /**
//     * 将同一标签组下的标签类别进行and操作
//     * @param tagCtgyList
//     */
//    private static TagConditionBase getCtgyCondtionF4SameGrp(List<TagCtgyEntity> tagCtgyList){
//        TagConditionBase matchResult = null;
//        for(TagCtgyEntity tagCtgy:tagCtgyList){
//            List<String> tagList = tagCtgy.getTagIds();
//            TagConditionBase tagCondition = getCondtion4TagsSameCtgy(tagList);
//            if(matchResult==null)
//                matchResult = tagCondition;
//            else{
//                matchResult.and(tagCondition);
//            }
//        }
//
//        return matchResult;
//    }
//
//    /**
//     * 将列表中的标签进行or操作，得到用于计算的Condition
//     * @param tagList
//     */
//    private static TagConditionBase getCondtion4TagsSameCtgy(List<String> tagList){
//        TagConditionBase matchResult = null;
//        for(String tag:tagList){
//            Match match = new Match();
//            if(matchResult==null){
//                match.setTag(tag);
//                matchResult = match;
//                continue;
//            }
//            match.setTag(tag);
//            matchResult.or(match);
//        }
//        return matchResult;
//    }

}
