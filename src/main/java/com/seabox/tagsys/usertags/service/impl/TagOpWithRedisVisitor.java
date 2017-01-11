package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.usertags.logicquery.TagCondition;
import com.seabox.tagsys.usertags.logicquery.TagConditionVisitor;
import com.seabox.tagsys.usertags.logicquery.impl.And;
import com.seabox.tagsys.usertags.logicquery.impl.Match;
import com.seabox.tagsys.usertags.logicquery.impl.Or;
import com.seabox.tagsys.usertags.logicquery.impl.TagQueryResultCacheImpl;
import com.seabox.tagsys.usertags.utils.UserTagUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

import static com.seabox.tagsys.usertags.utils.UserTagUtils.getCacheKeyByTagId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 2/15/16,4:59 PM
 */
public class TagOpWithRedisVisitor implements TagConditionVisitor<TagQueryResultCacheImpl> {

    private static final Logger logger = LoggerFactory.getLogger(TagOpWithRedisVisitor.class);

    private Jedis jedis;


    public TagOpWithRedisVisitor(Jedis jedis) {
        this.jedis = jedis;
    }


    @Override
    public TagQueryResultCacheImpl visitTagCondition(TagCondition op) {

        if(op instanceof Match)
        {
            Match opMatch = (Match)op;
            String key = getCacheKeyByTagId( opMatch.getTagId() );
            TagQueryResultCacheImpl result = createResultObject( key);
            return result;
        } else if(op instanceof And)
        {
            List<TagCondition> children = ((And)op).getConditions();
            TagQueryResultCacheImpl result = computeBitOperationWithStore(BitOP.AND, children);
            return result;
        } else if(op instanceof Or)
        {
            List<TagCondition> children = ((Or)op).getConditions();
            TagQueryResultCacheImpl result = computeBitOperationWithStore(BitOP.OR, children);
            return result;
        } else {
            logger.error("Error, TagOpWithRedisVisitor:  the TagCondition is not handled: {} ", op.getClass());
            return null;
        }

    }


    protected TagQueryResultCacheImpl computeBitOperationWithStore(BitOP bitOP, Collection<? extends TagCondition> childrenConditions) {

        List<String> srcKeys = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer(bitOP.toString());
        // visit my children first
        for(TagCondition child: childrenConditions ) {
            TagQueryResultCacheImpl r1 = this.visitTagCondition( child );
            srcKeys.add(r1.getKey());
        }
        stringBuffer.append(srcKeys);

        String resultKey = stringBuffer.toString();
        String[] srcKeyArray = new String[ srcKeys.size() ];
        srcKeys.toArray( srcKeyArray );

        cacheBitOperationWithStore(bitOP, resultKey, srcKeyArray);

        TagQueryResultCacheImpl result = createResultObject( resultKey );

        return result;
    }

    protected TagQueryResultCacheImpl createResultObject (String key) {
        TagQueryResultCacheImpl result = new TagQueryResultCacheImpl();
        result.setKey(key);
        return result;
    }

    protected void cacheBitOperationWithStore(BitOP bitOP, String resultKey, String[] srcKeyArray) {
        jedis.bitop(bitOP, resultKey, srcKeyArray);

        CacheServiceRedisImpl.safeExpireRedisKey(jedis, resultKey);

        logger.debug("perform Redis Operation, bitOP:{},  stored as new key: {}", bitOP, resultKey);
    }


}

