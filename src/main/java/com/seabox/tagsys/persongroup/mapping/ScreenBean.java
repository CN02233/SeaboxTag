package com.seabox.tagsys.persongroup.mapping;

/**
 * Created by pc on 2016/11/17.
 */
public class ScreenBean {

    private String tagId;//标签代码
    private String parTagId;//父标签ID
    private String tagNm;//标签名

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getParTagId() {
        return parTagId;
    }

    public void setParTagId(String parTagId) {
        this.parTagId = parTagId;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }
}
