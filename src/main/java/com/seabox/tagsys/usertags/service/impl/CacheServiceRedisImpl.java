package com.seabox.tagsys.usertags.service.impl;


import com.seabox.tagsys.usertags.action.job.engine.stage0.Stage0Engine_CommitCamp;
import com.seabox.tagsys.usertags.action.job.HealthStatsInstance;
import com.seabox.tagsys.usertags.action.job.HealthStatsManager;
import com.seabox.tagsys.usertags.action.job.engine.stage0.StepAInput;
import com.seabox.tagsys.usertags.hbase.entity.CampSmsActionStatics;
import com.seabox.tagsys.usertags.hbase.CampaignActionStoreHBaseImpl;
import com.seabox.tagsys.usertags.hbase.HBaseConnectionMgr;
import com.seabox.tagsys.usertags.hbase.entity.CampSmsActionStaticsImpl;
import com.seabox.tagsys.usertags.logicquery.UsersDistributeByTag;
import com.seabox.tagsys.usertags.logicquery.impl.*;
import com.seabox.tagsys.usertags.service.CacheService;
import com.seabox.tagsys.usertags.logicquery.TagCondition;
import com.seabox.tagsys.usertags.hbase.CampaignActionStore;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;


import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Changhua, Wu
 *         Created on: 1/25/16,3:02 PM
 */
@Service
public class CacheServiceRedisImpl implements CacheService, HealthStatsInstance {


    private static final Logger logger = LoggerFactory.getLogger(CacheServiceRedisImpl.class);

    @Autowired
    private JedisPool jedisPool;

//    @Autowired
    private HBaseConnectionMgr connectionMgr;

    @Autowired
    private HealthStatsManager healthStatsManager;


    @Autowired
    Stage0Engine_CommitCamp stage0EngineCommitCamp;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    private AtomicLong count_getNumOfUsersByCondition = new AtomicLong(0);
    private AtomicLong count_getUsersDistributeByTag = new AtomicLong(0);
    private AtomicLong count_commitAndSaveCacheForCampaign = new AtomicLong(0);
    private AtomicLong count_getCampSmsActionStatics = new AtomicLong(0);

    public static boolean  ENABLE_GLOBAL_FILTER  = false;

    @PostConstruct
    public void init() {

        healthStatsManager.register(this);

        //clean up temp keys used for Tag Condition calculation
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> andKeys = jedis.keys("AND*");
            Set<String> orKeys = jedis.keys("OR*");
            Pipeline pipeline = jedis.pipelined();

            pipeline.multi();
            for(String key: andKeys) {
                pipeline.del( key);
            }

            for(String key: orKeys) {
                pipeline.del( key );
            }
            pipeline.exec();

            pipeline.sync();

        }
    }


    public static void  safeExpireRedisKey(Jedis jedis, final String key) {

        if(null==key) {
            return;
        }

        if(key.startsWith("t:")
          || key.equals(UserTagUtils.REDIS_KEY_INDEX_TO_USER_GUID_MAP)
          || key.equals(UserTagUtils.REDIS_KEY_USER_GUID_TO_INDEX_MAP)
          || key.equals(UserTagUtils.GLOBAL_EXCLUDE_USERS_KEY)
          || key.startsWith("camp_bits:")
        ) {
            jedis.persist( key );
        } else {
            jedis.expire(key, UserTagUtils.DEFAULT_TEMP_CALC_REDIS_KEY_EXPIRE_SECONDS ); // expire on 3 mins
        }

    }

    /**
     * @param bytes
     * @return
     */
    public static BitSet fromByteArrayReverse(final byte[] bytes) {
        final BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            //将bytes的8位 每个位都与'1',将byte 2进制中是1的位的位置记录下来。
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                bits.set(i);
            }
        }
        return bits;
    }


    public String[] getAvailableCampsForExclude() {

        Set<String> campBitKeys = new HashSet<>(); //empty set

        try(Jedis jedis = jedisPool.getResource() ) {
            campBitKeys = jedis.keys("camp_bits:*");
        }

        String[] campIds = new String[ campBitKeys.size() ];
        int i=0;
        for(String campBitKey : campBitKeys) {
            campIds [i++] = campBitKey.replaceFirst("camp_bits:", "");
        }
        return campIds;
    }



    public static TagQueryResultCacheImpl controllableQueryResultFromTagCondition(TagCondition condition, Jedis jedis, @Nullable Collection<String> excludeUserInCamps) {
        TagOpWithRedisVisitor visitor = new TagOpWithRedisVisitor(jedis);
        TagQueryResultCacheImpl result = condition.accept( visitor);


        if(null!=excludeUserInCamps && !excludeUserInCamps.isEmpty()) {
            String[] campsKey = new String[ excludeUserInCamps.size() ];
            Iterator<String> ite = excludeUserInCamps.iterator();
            int i = 0;
            while (ite.hasNext()) {
                campsKey[ i++ ] = UserTagUtils.getCampBitSetRedisKey( ite.next() );
            }

            Pipeline pipeline = jedis.pipelined();
            final String f3ExcludesKey = "f3-or:" +  campsKey.toString();
            pipeline.bitop(BitOP.OR, f3ExcludesKey, campsKey);

            final String f1Key = "f1-xor:" + result.getKey();
            pipeline.bitop(BitOP.XOR, f1Key,    f3ExcludesKey, result.getKey());

            final String f2Key = "f2-and:" + f1Key;
            pipeline.bitop(BitOP.AND, f2Key,    f1Key, result.getKey());

            pipeline.del(f3ExcludesKey);
            pipeline.del(f1Key);

            pipeline.sync();

            result.setKey( f2Key ); // let's use the filtered result (exclude users exists in CampLists)

            CacheServiceRedisImpl.safeExpireRedisKey(jedis, f2Key );
        }


        if( ENABLE_GLOBAL_FILTER ) {
            if( jedis.exists( UserTagUtils.GLOBAL_EXCLUDE_USERS_KEY ) ) {
                //GLOBAL_EXCLUDE_USERS_KEY = "g:excludeUsers"
                Pipeline pipeline = jedis.pipelined();
                final String f1Key = "f1-xor:" + result.getKey();
                pipeline.bitop(BitOP.XOR, f1Key,    UserTagUtils.GLOBAL_EXCLUDE_USERS_KEY, result.getKey());

                final String f2Key = "f2-and:" + f1Key;
                pipeline.bitop(BitOP.AND, f2Key,    f1Key, result.getKey());

                pipeline.del(f1Key);

                pipeline.sync();

                result.setKey( f2Key ); // let's use the filtered result (exclude users already send SMS)

                CacheServiceRedisImpl.safeExpireRedisKey(jedis, f2Key);
            }

        }

        return result;
    }


    @Override
    public long getNumOfUsersByCondition(TagCondition condition, @Nullable Collection<String> excludeUserInCamps) {

        count_getNumOfUsersByCondition.getAndIncrement();

        try (Jedis jedis = jedisPool.getResource()) {

            TagQueryResultCacheImpl result = controllableQueryResultFromTagCondition(condition, jedis, excludeUserInCamps);
            String retKey = result.getKey();
            Long nums = jedis.bitcount( retKey );

            return nums==null ? 0L : nums;

        } catch (Throwable e) {
            logger.error("getNumOfUsersByCondition() exception:", e);
        }

        return 0L;

    }



    @Override
    public UsersDistributeByTag getUsersDistributeByTag(TagCondition condition, @Nullable Collection<String> excludeUserInCamps) {

        count_getUsersDistributeByTag.getAndIncrement();

        try (Jedis jedis = jedisPool.getResource()){

            TagQueryResultCacheImpl result = controllableQueryResultFromTagCondition(condition, jedis, excludeUserInCamps);
            String retKey = result.getKey();
            Long nums = jedis.bitcount( retKey );

            UsersDistributeByTagResultImpl distResult = new UsersDistributeByTagResultImpl();
            distResult.setTotalNumOfUsers(nums);
            distResult.setKeyOfUsers(retKey);//add by SongChaoqun

            GrepMatchConditionVisitor grepVisitor = new GrepMatchConditionVisitor();
            GrepMatchConditionResult grepResult = condition.accept(grepVisitor);

            for(Integer tagId : grepResult) {
                String tagKey = UserTagUtils.getCacheKeyByTagId(tagId);
                String retKeyByTag = "calc_DIST(" + tagKey + ", " + retKey + ")";

                Pipeline pipeline = jedis.pipelined();
                pipeline.multi();
                pipeline.bitop(BitOP.AND, retKeyByTag, retKey, UserTagUtils.getCacheKeyByTagId(tagId) );
                Response<Long> numOfUserForTag = pipeline.bitcount( retKeyByTag );
                pipeline.del( retKeyByTag );
                pipeline.exec();
                pipeline.sync();

                distResult.addIntoTagToUserCountMap(tagId, numOfUserForTag.get() );
            }

            return distResult;

        } catch (Throwable e) {
            logger.error("getUsersDistributeByTag() exception:", e);
        }

        return new UsersDistributeByTagResultImpl();

    }



    public final static int MAX_THREADS_FOR_SUB = 10;



    @Override
    public void commitAndSaveCacheForCampaign(int campId, TagCondition condition, @Nullable Collection<String> excludeUserInCamps) {

        try {
            count_commitAndSaveCacheForCampaign.getAndIncrement();

            StepAInput input = new StepAInput();
            input.setCampId(String.valueOf(campId));
            input.setCondition(condition);
            input.setExcludeUserInCamps(excludeUserInCamps);

            stage0EngineCommitCamp.processStage(input);
        }
        catch (Throwable e) {
            logger.error("error occurred on commitAndSaveCacheForCampaign(), exception:", e);
        }
    }


    @Override
    public CampSmsActionStatics getCampSmsActionStatics(int campId) {

        count_getCampSmsActionStatics.getAndIncrement();

        CampSmsActionStatics statics;

        try {
            Connection hBaseCon = connectionMgr.getConnection();
            CampaignActionStore campaignActionStore = CampaignActionStoreHBaseImpl.createInstance( String.valueOf(campId) , hBaseCon);

            statics = campaignActionStore.getCampSmsActionStatics();

        } catch (IOException e) {

            statics = new CampSmsActionStaticsImpl(); // create Zero Answer

            logger.error("getCampSmsActionStatics() Failed for campId: " + campId + ", give Zero Answer due to HBase access have issue", e);
        }


        return statics;
    }


    @Override
    public String getStatsTitle() {
        return CacheServiceRedisImpl.class.getSimpleName();
    }

    @Override
    public Map<String, String> getStatsMap() {
        Map<String, String>  statsMap = new HashMap<>();
        statsMap.put("commitAndSave", count_commitAndSaveCacheForCampaign.toString());
        statsMap.put("getStatics", count_getCampSmsActionStatics.toString());
        statsMap.put("getNums",  count_getNumOfUsersByCondition.toString());
        statsMap.put("getDistribute", count_getUsersDistributeByTag.toString());

        return statsMap;
    }
}
