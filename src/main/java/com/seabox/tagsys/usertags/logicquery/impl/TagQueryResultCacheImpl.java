package com.seabox.tagsys.usertags.logicquery.impl;

import com.seabox.tagsys.usertags.logicquery.CacheKeyStore;
import com.seabox.tagsys.usertags.logicquery.TagQueryResult;

/**
 * @author Changhua, Wu
 *         Created on: 1/19/16,3:17 PM
 */

public class TagQueryResultCacheImpl implements TagQueryResult, CacheKeyStore {

    private String key;

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
