package com.seabox.tagsys.usertags.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * @author Changhua, Wu
 *         Created on: 1/22/16.
 */
@JsonInclude(content= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"created_ts","updated_ts", "parent"})
@JsonTypeName("category")
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY)
public class TagCategoryLastLevel extends TagCategoryBase<TagCategoryHighLevel, TagInfo>  implements Serializable {


    /*
          fields get from Redis Cache
        */
    private Double       covered_rate;

    private Boolean      favorite_by_user;

    public Double getCovered_rate() {
        return covered_rate;
    }

    public void setCovered_rate(double covered_rate) {
        this.covered_rate = covered_rate;
    }

    public void setCovered_rate(Double covered_rate) {
        this.covered_rate = covered_rate;
    }

    public Boolean getFavorite_by_user() {
        return favorite_by_user;
    }

    public void setFavorite_by_user(Boolean favorite_by_user) {
        this.favorite_by_user = favorite_by_user;
    }


    public String toString() {
        return "id:" + tag_ctgy_id + " name:" + tag_ctgy_nm + " cover:" + covered_rate;
    }
}
