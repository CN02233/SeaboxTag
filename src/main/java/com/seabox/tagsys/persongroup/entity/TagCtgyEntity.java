package com.seabox.tagsys.persongroup.entity;

import java.util.List;

/**
 * Created by SongChaoqun on 2016/12/14.
 */
public class TagCtgyEntity {

    private String tagCtgyId;

    private List<String> tagIds;

    public String getTagCtgyId() {
        return tagCtgyId;
    }

    public void setTagCtgyId(String tagCtgyId) {
        this.tagCtgyId = tagCtgyId;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

}