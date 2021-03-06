package com.seabox.tagsys.usertags.logicquery.impl;

import com.seabox.tagsys.usertags.logicquery.TagConditionVisitor;
import com.seabox.tagsys.usertags.logicquery.TagQueryResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;


/**
 * @author Changhua, Wu
 *         Created on: 1/19/16,3:18 PM
 */
@XmlRootElement(name="match")
public class Match extends TagConditionBase {

    @JsonProperty
    private String tag;

    @JsonProperty
    private int tagId;

    public Match(int tagId, String tag) {
        this.tagId = tagId;
        this.tag = tag;
    }

    //default constructor is required for Jackson deserialize
    public Match() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public <R extends TagQueryResult> R accept(TagConditionVisitor<R> visitor) {
        R   result = visitor.visitTagCondition(this);
        return result;
    }

    @Override
    public String toString() {
        return  "Match tag:" + tag + " , tagId:" + tagId;
    }

}
