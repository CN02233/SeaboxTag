package com.seabox.tagsys.persongroup.service;

import com.seabox.tagsys.usertags.service.impl.CacheServiceRedisImpl;
import com.seabox.test.base.BaseTestClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.*;

import java.util.BitSet;
import java.util.Set;

/**
 * Created by pc on 2016/12/14.
 */
public class RedisInfoTest extends BaseTestClass {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void sumPersonCountFromTag(){
        Jedis resource = jedisPool.getResource();
        Set<String> allKeys = resource.keys("t:*");

        BitSet localBitSetTmp = null;
        for(String key:allKeys){
            byte[] usersBitSet = resource.get(key.getBytes());
            BitSet localBitSet = CacheServiceRedisImpl.fromByteArrayReverse( usersBitSet );


            int numsLocalBitSet = localBitSet.cardinality();
            System.out.println(key+":"+resource.bitcount(key)+"-->"+numsLocalBitSet);

        }

    }

    @Test
    public void testAndReslt(){
        Jedis resource = jedisPool.getResource();
        Pipeline pipeline = resource.pipelined();
        pipeline.bitop(BitOP.AND, "test-and-result".getBytes(), "t:100850".getBytes(), "t:100889".getBytes());
        pipeline.sync();
    }

    @Test
    public void getValFromRedis(){
        Jedis resource = jedisPool.getResource();
        byte[] valResu = resource.get("test-and-result".getBytes());

        BitSet localBitSet = CacheServiceRedisImpl.fromByteArrayReverse( valResu );
        System.out.println(localBitSet.cardinality());

    }
}
