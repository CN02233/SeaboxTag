package com.seabox.tagsys.usertags.action.job.engine.stage0;

import com.seabox.tagsys.usertags.action.job.engine.ProcessStep;
import com.seabox.tagsys.usertags.logicquery.TagCondition;
import com.seabox.tagsys.usertags.logicquery.impl.TagQueryResultCacheImpl;
import com.seabox.tagsys.usertags.service.impl.CacheServiceRedisImpl;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.BitSet;
import java.util.Collection;

/**
 * Created by wuchh on 3/10/16.
 */
@SingletonBean
public class StepAProcess_SaveGuidsToRedis implements ProcessStep<StepAInput, StepAOutput> {


    private static final Logger logger = LoggerFactory.getLogger(StepAProcess_SaveGuidsToRedis.class);


    @Autowired
    private JedisPool jedisPool;



    private final int  MAX_THREADS_FOR_SUB = 10;


    @Override
    public void beforeProcess() {

    }

    @Override
    public StepAOutput process(StepAInput stepAInput) {

        final String campId = stepAInput.getCampId();
        TagCondition condition = stepAInput.getCondition();
        Collection<String> excludeUserInCamps = stepAInput.getExcludeUserInCamps();

        logger.info("process() start  for campId: {}", campId);


        try (Jedis jedis = jedisPool.getResource()) { // auto close Jedis resource

            TagQueryResultCacheImpl result = CacheServiceRedisImpl.controllableQueryResultFromTagCondition(condition, jedis, excludeUserInCamps);

            final String campUsersKeyInRedis = UserTagUtils.getCampBitSetRedisKey(campId);//"camp_bits:" + campId;

            jedis.rename(result.getKey(), campUsersKeyInRedis);

            jedis.persist(campUsersKeyInRedis); // Don't expire this key, we need to keep camp-BitSet always

            byte[] redisBitSet = jedis.get(campUsersKeyInRedis.getBytes());

            final BitSet localBitSet = CacheServiceRedisImpl.fromByteArrayReverse(redisBitSet);

            long numsGUIDInKeyForCamp = jedis.bitcount( campUsersKeyInRedis );

            StepAOutput  output = new StepAOutput();
            output.setCampId(campId);
            output.setStepAInput( stepAInput );
            output.setRedisBitSetInBytes(redisBitSet);
            output.setRedisBitSetInJavaObj( localBitSet );
            output.setNumsGUIDInKeyForCamp( numsGUIDInKeyForCamp );

            return output;

        } catch (Throwable e) {

            logger.error("error on get Redis usersKey!", e);

            return null;

        }

    }

    @Override
    public void afterProcess() {

    }
}
