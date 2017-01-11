package com.seabox.tagsys.usertags.entity;

/**
 * Created by SongChaoqun on 2016/12/8.
 */
public class PersonData {

    private String userNm;

    private String userSex;

    private String userAge;

    private String hasMarried;

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getHasMarried() {
        return hasMarried;
    }

    public void setHasMarried(String hasMarried) {
        this.hasMarried = hasMarried;
    }

    public String toString(){
        return "user name is :"+userNm+"---user sex is :"+userSex+"---user age is:"+
                userAge+"---hasMarried value is "+hasMarried;
    }
}
