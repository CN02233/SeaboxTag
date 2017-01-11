package com.seabox.tagsys.usertags.msg;

import java.io.Serializable;

/**
 * @author Changhua, Wu
 *         Created on: 5/18/16,12:07 PM
 */
public class IndvCallResult implements Serializable {

    private int campId;
    private long userId;
    private String mobile;
    private String callResult;

    public int getCampId() {
        return campId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }


    @Override
    public String toString() {
        return "IndvCallResult{" +
                "campId=" + campId +
                ", userId=" + userId +
                ", mobile='" + mobile + '\'' +
                ", callResult='" + callResult + '\'' +
                '}';
    }
}
