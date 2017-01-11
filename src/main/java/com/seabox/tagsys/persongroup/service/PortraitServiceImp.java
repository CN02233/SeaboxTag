package com.seabox.tagsys.persongroup.service;

import com.seabox.tagsys.base.service.BaseService;
import com.seabox.tagsys.persongroup.utils.TagHelperBean;
import com.seabox.tagsys.persongroup.utils.TagHelperUtil;
import com.seabox.tagsys.sys.entity.PageBean;
import com.seabox.tagsys.usertags.hbase.entity.TUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * Created by pc on 2016/12/7.
 */
@Service("portraitService")
public class PortraitServiceImp extends BaseService{

    @Autowired
    private PersonListService personListService;

    @Autowired
    private JedisPool jedisPool;

    private static final Logger logger = LoggerFactory.getLogger(PortraitServiceImp.class);

    public PageBean<Object> listUser4Screening(Map<String,Object> paramMap){
        logger.info("/n get person grop data -->"+paramMap.get("currPage")+"--->"
                +paramMap.get("pageSize")+"--->"+paramMap.get("screeningId"));

        String screeningId = (String) paramMap.get("screeningId");
        int currPage = new Integer(paramMap.get("currPage").toString());
        int pageSize = new Integer(paramMap.get("pageSize").toString());

        List<TUserInfo> resultUsrInf = personListService.createPersonListFromHbase(screeningId, currPage,pageSize);

        List<Map<String,String>> dataList = new ArrayList<>();

        for(TUserInfo resultUsr:resultUsrInf){
//            String userNm = resultUsr.getUserName();
//            String userAge = resultUsr.getUserAge();
            String userSex = resultUsr.getUserSex();
            String hasMarried = resultUsr.getHasMarried();
            Map<String,String> dataMap = new HashMap<>();
            dataMap.put("userNm",resultUsr.getUserName());
            dataMap.put("userAge",resultUsr.getUserAge());
            dataMap.put("userGuid",resultUsr.getUserGuid());

            if(userSex.equals(PersonEnum.SEX_MR.toString())){
                userSex = PersonEnum.SEX_MR.showVal;
            }else if(userSex.equals(PersonEnum.SEX_MS.toString())){
                userSex = PersonEnum.SEX_MS.showVal;
            }else
                userSex = PersonEnum.NULL.toString();
            dataMap.put("userSex",userSex);

            if(hasMarried.equals(PersonEnum.YES.toString())){
                hasMarried = PersonEnum.YES.showVal;
            }else if(hasMarried.equals(PersonEnum.NO.toString())){
                hasMarried = PersonEnum.NO.showVal;
            }else
                hasMarried = PersonEnum.NULL.toString();
            dataMap.put("hasMarried",hasMarried);
            dataList.add(dataMap);
        }

        PageBean pageBean = new PageBean();
        pageBean.setList(dataList);
        pageBean.setCurrPage(currPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalNum(personListService.getPageTotalNum());

        return pageBean;
    }

    public List<TagHelperBean> getAllTags(TagHelperUtil.TagOrder order){
        TagHelperUtil tagHelperUtil = TagHelperUtil.getInstance(jedisPool);
        List<TagHelperBean> allTags = tagHelperUtil.getAllTagFromRedis(TagHelperUtil.ByteToBitSet.NOT_NEED);
        tagHelperUtil.sortTagByUsers(allTags,order);


        return allTags;
    }


    /**
     * 获取当前用户下的所有标签中排名前N个的标签数据:N值由TOP_COUNT指定
     * @param
     */
    public List<Map<String, Object>> getUserAllTag(String userGuid){
        TagHelperUtil tagHelperUtil = TagHelperUtil.getInstance(jedisPool);

        int eachCount = 10;

        logger.debug("start check user all tag from redis.....");
        List<TagHelperBean> tagList = tagHelperUtil.checkUserAllTag(new Long(userGuid));
        logger.debug("check user all tag from redis has done.....");

        int forTime = 1;
        if(tagList.size()>eachCount){
            forTime = tagList.size()/ eachCount;
            if(tagList.size()%eachCount>0)
                forTime++;
        }
        List<Map<String,Object>> resultList = new ArrayList<>();
        for(int i=0;i<forTime;i++){
            int startNum = i*eachCount;
            int endNum = startNum+eachCount;
            if(forTime==(i+1)){
                endNum = startNum+(tagList.size()%eachCount);
            }

            List<Map<String, Object>> tagInfoList = getTagInfoList(tagList, startNum, endNum);
            resultList.addAll(tagInfoList);
        }
        return resultList;

    }

    private List<Map<String,Object>> getTagInfoList(List<TagHelperBean> allTagList,int startNum,int endNum){
        List<String> searchList = new ArrayList<>();
        Map<String,Object> tagAndCountTmp = new HashMap<>();
        for(int s=startNum;s<endNum;s++){
            TagHelperBean tagHelperBean = allTagList.get(s);
            String realTagId = tagHelperBean.getTagId().replace("t:", "");
            searchList.add(realTagId);
            tagAndCountTmp.put(realTagId,tagHelperBean.getUserCount());
        }

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("tagList",searchList);
        List<Map<String,Object>> tagResult = relationDbTemplate.selectList("portrait.getTagInfoByTaList", paramMap);

        Map<String,List<Map<String,Object>>> resultMap = new HashMap<>();

        for(Map<String,Object> tagMap:tagResult){
            String tagCtgyId = (String) tagMap.get("tagCtgyId");
            tagMap.put("userCount",tagAndCountTmp.get(tagMap.get("tagId").toString()));
            if(resultMap.containsKey(tagCtgyId)){
                List<Map<String, Object>> tagList = resultMap.get(tagCtgyId);
                tagList.add(tagMap);
            }else{
                List<Map<String, Object>> tagList = new ArrayList<>();
                resultMap.put(tagCtgyId,tagList);
                tagList.add(tagMap);

            }
        }

        Collections.sort(tagResult,comparable);

        return tagResult;
    }

    /**
     * 获取系统中排名前N的标签
     */
    public List<Map<String, Object>> getTopNTags(List<TagHelperBean> allTagList, int topN) {
        return getTagInfoList(allTagList, 0, topN);
    }

    /**
     * 获取当前筛选条件中排名最高的前10个标签
     */
    public Map<String, List<Map<String, Object>>> getTagsForScreeningOrder(String screeningId, List<TagHelperBean> allTagList) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("screeningId",screeningId);
        List<Map<String,Object>> tagResult = relationDbTemplate.selectList("prnScreening.getScreeningTags", paramMap);

        Map<String,Long> tagHelperBeanTmp = new HashMap<>();

        for(TagHelperBean tagBean:allTagList){
            tagBean.getTagId();
            tagBean.getUserCount();
            tagHelperBeanTmp.put(tagBean.getTagId().replace("t:",""), tagBean.getUserCount());
        }

        List<Map<String,Object>> orderList = new ArrayList<>();
        for(Map<String,Object> tag4Screening:tagResult){
            Integer tagId = (Integer) tag4Screening.get("tagId");
            if(tagHelperBeanTmp.containsKey(tagId.toString())){
                Map<String,Object> orderScreenMap = new HashMap<>();
                Long userCount = tagHelperBeanTmp.get(tagId.toString());
                orderScreenMap.put("userCount",userCount);
                orderScreenMap.putAll(tag4Screening);
                tagHelperBeanTmp.remove(tagId.toString());
                orderList.add(orderScreenMap);
            }
        }
        Collections.sort(orderList,comparable);

        Map<String,List<Map<String,Object>>> orderResultMap = new HashMap<>();

        orderResultMap.put("orderList",orderList);
        orderResultMap.put("tagList",tagResult);


        return orderResultMap;
    }

    Comparator comparable = new Comparator<Map<String,Object>>() {
        @Override
        public int compare(Map<String,Object> o1, Map<String,Object> o2) {
            Long userCount1 = (Long) o1.get("userCount");
            Long userCount2 = (Long) o2.get("userCount");
            return userCount2.compareTo(userCount1);
        }
    };

    enum PersonEnum{

        SEX_MR("1","男") ,SEX_MS("0","女"),YES("Y","是"),NO("N","否"),NULL("未知","");

        private String val;
        private String showVal;

        PersonEnum(String val,String showVal){
            this.showVal = showVal;
            this.val = val;
        }

        public String toString(){
            return val;
        }
    }

}
