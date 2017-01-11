package com.seabox.tagsys.persongroup.utils;

import com.seabox.tagsys.usertags.service.impl.CacheServiceRedisImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * Redis中tag的相关操作,包括tag筛选人群的排序 一个筛选条件中各个标签圈定人群数的排序
 * Created by SongChaoqun on 2016/12/15.
 */
public class TagHelperUtil {

    private static final Logger logger = LoggerFactory.getLogger(TagHelperUtil.class);

    private static TagHelperUtil tagHelperUtil;

    private final JedisPool jedisPool;

    private final String tagTitle = "t:";

    public static TagHelperUtil getInstance(JedisPool jedisPool){
        if(tagHelperUtil!=null)
            return tagHelperUtil;
        else{
            tagHelperUtil = new TagHelperUtil(jedisPool);
            return tagHelperUtil;
        }
    }

    private TagHelperUtil(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    /**
     * 取出当前redis中存储的所有标签
     * @Param needToJavaObj true:需要将redis中取出的byte[]转成java对象BitSet (慎用 标签数量大的话会很慢)
     * @return
     */
    public List<TagHelperBean> getAllTagFromRedis(ByteToBitSet needToJavaObj){
        Jedis resource = jedisPool.getResource();
//        resource.clusterGetKeysInSlot()
        List<TagHelperBean> resultList = new ArrayList<>();
        logger.debug("start get tag keys ");
        Set<String> allTags = resource.keys(tagTitle+"*");
        logger.debug("get tag keys has done");

        for(String tagId:allTags){


            TagHelperBean tagHelperBean = new TagHelperBean();
            tagHelperBean.setTagId(tagId);
            Long userCount = resource.bitcount(tagId);
            tagHelperBean.setUserCount(userCount);
            if(needToJavaObj == ByteToBitSet.NEED_TURN){
                byte[] tagValueByte = resource.get(tagId.getBytes());
                BitSet tagValueBitSet = CacheServiceRedisImpl.fromByteArrayReverse(tagValueByte);
                tagHelperBean.setUserBit(tagValueBitSet);
//                int userCount = tagValueBitSet.cardinality();

            }
            resultList.add(tagHelperBean);

            resource.bitcount(tagId);

//            logger.debug(tagId + " ###tag value is "+resultList);

        }

        return resultList;
    }

    /**
     * 取出用户对应的所有标签
     * @param user
     * @return
     */
    public List<TagHelperBean> checkUserAllTag(Long user){
        logger.debug("start check tags of user :"+user);

        List<TagHelperBean> allTag = getAllTagFromRedis(ByteToBitSet.NOT_NEED);
        logger.debug("tag full list has got :"+user);

        List<TagHelperBean> tagList4User = new ArrayList<>();
        logger.debug("start check user tags....."+user);

        try(Jedis resource = jedisPool.getResource()){
            for(TagHelperBean tagObj :allTag){

                String tagIdFromRedis = tagObj.getTagId();
                Boolean hasCheck = resource.getbit(tagIdFromRedis, user);
                if(hasCheck){
                    tagList4User.add(tagObj);
                }
//
//            BitSet userBitSet = tagObj.getUserBit();
//
//            boolean isCheck = userBitSet.get(user);
//            if(isCheck){
//
//            }
            }
        }catch (Exception e){
            throw e;
        }


        logger.debug("check tags of user :"+user+" has done");

        return tagList4User;
    }


    /**
     * 根据标签命中的人群数进行排序
     * @Param order 排序是否升序,true升序 false降序
     */
    public static List<TagHelperBean> sortTagByUsers(List<TagHelperBean> tagList, final TagOrder order){

        Comparator comparable = new Comparator<TagHelperBean>() {

            @Override
            public int compare(TagHelperBean o1, TagHelperBean o2) {
                Long o1Count = o1.getUserCount();
                Long o2Count = o2.getUserCount();
                if(order == TagOrder.ORDER_ASC)
//                    return o1Count-o2Count;
                    return o1Count.compareTo(o2Count);
                return o2Count.compareTo(o1Count);
            }
        };

        Collections.sort(tagList,comparable);

        return tagList;
    }


    public enum TagOrder{
        ORDER_DESC,ORDER_ASC;
    }

    public enum ByteToBitSet{
        NEED_TURN,NOT_NEED;
    }

}

