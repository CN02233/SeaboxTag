package com.seabox.tagsys.usertags.entity;

import java.util.Date;

/**
 * @author Changhua, Wu
 *         Created on: 5/5/16,11:29 AM
 */
public class ImportUser {

    private long uid;
    private String mobile;
    private Date created_ts;
    private Date updated_ts;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    @Override
    public String toString() {
        return "ImportUser{" +
                "uid=" + uid +
                ", mobile='" + mobile + '\'' +
                ", created_ts=" + created_ts +
                ", updated_ts=" + updated_ts +
                '}';
    }
}
