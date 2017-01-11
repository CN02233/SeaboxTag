package com.seabox.tagsys.persongroup.entity;

import java.util.List;

/**
 * Created by SongChaoqun on 2016/12/14.
 */
public class TagGrpEntity {

    private String grpSeq;

    private List<TagCtgyEntity> tagCtgys;

    public String getGrpSeq() {
        return grpSeq;
    }

    public void setGrpSeq(String grpSeq) {
        this.grpSeq = grpSeq;
    }

    public List<TagCtgyEntity> getTagCtgys() {
        return tagCtgys;
    }

    public void setTagCtgys(List<TagCtgyEntity> tagCtgys) {
        this.tagCtgys = tagCtgys;
    }
}
