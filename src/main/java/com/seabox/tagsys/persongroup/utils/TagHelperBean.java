package com.seabox.tagsys.persongroup.utils;

import java.util.BitSet;

/**
 * Created by pc on 2016/12/15.
 */
public class TagHelperBean {

    private String tagId;
    private String tagNm;
    private Long userCount;
    private BitSet userBit;
    private byte[] userByteArray;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public BitSet getUserBit() {
        return userBit;
    }

    public void setUserBit(BitSet userBit) {
        this.userBit = userBit;
    }

    public byte[] getUserByteArray() {
        return userByteArray;
    }

    public void setUserByteArray(byte[] userByteArray) {
        this.userByteArray = userByteArray;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }
}
