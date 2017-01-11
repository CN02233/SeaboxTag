package com.seabox.tagsys.usertags.entity;

/**
 * @author Changhua, Wu
 *         Created on: 5/5/16,5:39 PM
 */
public class ImportFileUserTemp {


    private String mobile;

    private Long count;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImportFileUserTemp{" +
                "mobile='" + mobile + '\'' +
                ", count=" + count +
                '}';
    }
}
