package com.seabox.tagsys.usertags.logicquery.impl;

import com.seabox.tagsys.usertags.logicquery.TagQueryResult;
import com.seabox.tagsys.usertags.logicquery.UsersDistributeByTag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Changhua, Wu
 *         Created on: 2/4/16,11:52 AM
 */
public class UsersDistributeByTagResultImpl implements UsersDistributeByTag, TagQueryResult {

    String keyOfUsers;

    long totalNumOfUsers;

    Map<Integer, Long>   numOfUsersByTag = new HashMap<>();

    @Override
    public String getKeyOfUsers() {
        return this.keyOfUsers;
    }

    @Override
    public long getTotalNumOfUsers() {
        return totalNumOfUsers;
    }

    @Override
    public Map<Integer, Long> getTagToUserCountMap() {
        return Collections.unmodifiableMap( numOfUsersByTag );
    }

    public void setTotalNumOfUsers(long totalNumOfUsers) {
        this.totalNumOfUsers = totalNumOfUsers;
    }

    public void addIntoTagToUserCountMap(int tagId, long numOfUsers) {
        numOfUsersByTag.put(tagId, numOfUsers);
    }

    public void setKeyOfUsers(String keyOfUsers) {
        this.keyOfUsers = keyOfUsers;
    }
}
