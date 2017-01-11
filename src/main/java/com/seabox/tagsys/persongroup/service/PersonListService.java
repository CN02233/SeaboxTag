package com.seabox.tagsys.persongroup.service;


import com.seabox.tagsys.base.db.RelationDbTemplate;
import com.seabox.tagsys.persongroup.utils.TagGrpToCondition;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.entity.TUserInfo;
import com.seabox.tagsys.usertags.logicquery.UsersDistributeByTag;
import com.seabox.tagsys.usertags.logicquery.impl.TagConditionBase;
import com.seabox.tagsys.usertags.service.CacheService;
import com.seabox.tagsys.usertags.service.impl.CacheServiceRedisImpl;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 人群的生成以及其他功能
 *
 * Created by SongChaoqun on 2016/12/14.
 */

@Service( "personListService")
public class PersonListService {

    @Autowired
    private CacheService cachService;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private HBaseConnectionMgr connectionMgr;

    @Resource
    private RelationDbTemplate relationDbTemplate;

    private ThreadLocal<Integer> pageNumLocal = new ThreadLocal<>();
    private ThreadLocal<Integer> pageSizeLocal = new ThreadLocal<>();
    private ThreadLocal<Integer> totalCntLocal = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(PersonListService.class);

    private int EACH_COUNT = 10;

    /**
     *
     * @param screeningId
     * @return
     */
    public List<TUserInfo> createPersonListFromHbase(String screeningId, int pageNum, int pageSize){
        pageNumLocal.set(pageNum);
        pageSizeLocal.set(pageSize);
        List<Map<String, Object>> tagFromDb = getTagList(screeningId);
        TagConditionBase condition = TagGrpToCondition.dbMapToCondition(tagFromDb);

        BitSet userBit = getUserBitFromRedis(condition);

        List<TUserInfo> userList = getUserDataFromHbase(userBit);

        totalCntLocal.set(getPageTotalNum(userBit));

        return userList;

    }

    /**
     * 将位图从选定的标签组合位图从redis中计算后取出
     * @return
     */
    public BitSet getUserBitFromRedis(TagConditionBase condition){

        UsersDistributeByTag result = cachService.getUsersDistributeByTag(condition, null);
        String keyOfUsers = result.getKeyOfUsers();
        byte[] userBitVal = jedisPool.getResource().get(keyOfUsers.getBytes());
        BitSet userBit = CacheServiceRedisImpl.fromByteArrayReverse(userBitVal);

        return userBit;
    }


    /**
     * 根据从redis中取到的位图信息,从hbase中捞取对应的用户信息
     */
    public List<TUserInfo> getUserDataFromHbase(BitSet userBit) {
        List<Integer> userList = getUserListThisTime(userBit);

        List<TUserInfo> userInfoList = new ArrayList<>();

        try(Table userTable = connectionMgr.getConnection().getTable(TUserInfo.tableName())){
            for(Integer user:userList){
                TUserInfo userInfo = TUserInfo.findById(userTable, user.toString());
                userInfo.setUserGuid(user.toString());
                logger.debug(userInfo.toString());
                userInfoList.add(userInfo);
            }
        }catch (IOException e){

        }

        return userInfoList;
//        Table userTable = connectionMgr.getConnection().getTable(TUserInfo.tableName());


    }

    public List<Integer> getUserListThisTime(BitSet userBit){
        logger.debug(userBit.toString());

        int currPage = pageNumLocal.get();
        int pageSize = pageSizeLocal.get();
        if(pageSize==0)
            pageSize = EACH_COUNT;
        int startPagePlace = ((currPage-1)*pageSize)+1;//如:第2页,应该是第11~20条数据 startPagePlace==11

        int startIndexPlace = 0;
        int checkTime = 0;
        int forTime = 0;

        List<Integer> resultList = new ArrayList<>();
        while (true){
            startIndexPlace = userBit.nextSetBit(startIndexPlace);
            ++forTime;
            if(forTime==startPagePlace){
                resultList.add(startIndexPlace);
                ++checkTime;
            }else{
                if(resultList.size()>0){
                    resultList.add(startIndexPlace);
                    ++checkTime;
                }
            }
            startIndexPlace++;
            if(checkTime==pageSize)
                break;
        }

        return resultList;

    }

    private int getPageTotalNum(BitSet userBit){
        int trueCount = userBit.cardinality();
        int pageNum = trueCount/EACH_COUNT;
        if(trueCount%EACH_COUNT>0)
            ++pageNum;
        return pageNum;
    }

    private List<Map<String,Object>> getTagList(String screeningId){
        Map param = new HashMap();
        param.put("screeningId",screeningId);
        List<Map<String,Object>> tagResults = relationDbTemplate.selectList("prnScreening.getScreeningTags",param);
        return tagResults;
    }

    public int getPageTotalNum() {
        return totalCntLocal.get();
    }
}
