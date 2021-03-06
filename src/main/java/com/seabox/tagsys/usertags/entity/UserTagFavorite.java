package com.seabox.tagsys.usertags.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Changhua, Wu
 *         Created on: 1/23/16.
 */
@JsonInclude(content= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"created_ts","updated_ts"})
public class UserTagFavorite implements Serializable {

    /*
       fields get from DB table
     */
    int user_id;
    int tag_ctgy_id;
    Date created_ts;
    Date updated_ts;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTag_ctgy_id() {
        return tag_ctgy_id;
    }

    public void setTag_ctgy_id(int tag_ctgy_id) {
        this.tag_ctgy_id = tag_ctgy_id;
    }

    public Date getCreated_ts() {
        return created_ts;
    }

    public void setCreated_ts(Date created_ts) {
        this.created_ts = created_ts;
    }

    public Date getUpdated_ts() {
        return updated_ts;
    }

    public void setUpdated_ts(Date updated_ts) {
        this.updated_ts = updated_ts;
    }
}
