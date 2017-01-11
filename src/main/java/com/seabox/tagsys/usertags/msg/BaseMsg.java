package com.seabox.tagsys.usertags.msg;

import java.io.Serializable;

/**
 * @author Changhua, Wu
 *         Created on: 5/18/16,11:03 AM
 */
public class BaseMsg implements Serializable {

    private int resultCode;
    private String msg;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void buildSucessMsg() {
        setResultCode(0);
        setMsg("success");
    }

}
