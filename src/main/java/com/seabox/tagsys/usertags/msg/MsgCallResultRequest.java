package com.seabox.tagsys.usertags.msg;

import java.io.Serializable;
import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 5/18/16,3:32 PM
 */
public class MsgCallResultRequest implements Serializable{

    private List<IndvCallResult>  callResultList;


    public List<IndvCallResult> getCallResultList() {
        return callResultList;
    }

    public void setCallResultList(List<IndvCallResult> callResultList) {
        this.callResultList = callResultList;
    }

    @Override
    public String toString() {
        return "MsgCallResultRequest{" +
                "callResultList=" + callResultList +
                '}';
    }
}
