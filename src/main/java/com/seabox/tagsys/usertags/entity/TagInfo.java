package com.seabox.tagsys.usertags.entity;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Changhua, Wu
 *         Created on: 1/20/16,3:23 PM
 */
@JsonInclude(content= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"created_ts","updated_ts","parent"})
@JsonTypeName("tag")
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY)
public class TagInfo  implements Serializable {

    /*
       fields get from DB table
     */
    private int    tag_id;
    private String tag_nm;
    private String tag_desc	;
    private String tag_type_cd	;
    private int    tag_ctgy_id	;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date   enabled_dt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date   disabled_dt;
    private boolean    active_ind ;
    private boolean    unknown_ind;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date   created_ts;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date   updated_ts;


    private Long       covered_count;
    private Double        covered_rate;


    private TagCategoryLastLevel  parent;


    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_nm() {
        return tag_nm;
    }

    public void setTag_nm(String tag_nm) {
        this.tag_nm = tag_nm;
    }

    public String getTag_desc() {
        return tag_desc;
    }

    public void setTag_desc(String tag_desc) {
        this.tag_desc = tag_desc;
    }

    public String getTag_type_cd() {
        return tag_type_cd;
    }

    public void setTag_type_cd(String tag_type_cd) {
        this.tag_type_cd = tag_type_cd;
    }

    public int getTag_ctgy_id() {
        return tag_ctgy_id;
    }

    public void setTag_ctgy_id(int tag_ctgy_id) {
        this.tag_ctgy_id = tag_ctgy_id;
    }

    public Date getEnabled_dt() {
        return enabled_dt;
    }

    public void setEnabled_dt(Date enabled_dt) {
        this.enabled_dt = enabled_dt;
    }

    public Date getDisabled_dt() {
        return disabled_dt;
    }

    public void setDisabled_dt(Date disabled_dt) {
        this.disabled_dt = disabled_dt;
    }

    public boolean isActive_ind() {
        return active_ind;
    }

    public void setActive_ind(boolean active_ind) {
        this.active_ind = active_ind;
    }

    public boolean isUnknown_ind() {
        return unknown_ind;
    }

    public void setUnknown_ind(boolean unknown_ind) {
        this.unknown_ind = unknown_ind;
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


    public Long getCovered_count() {
        return covered_count;
    }

    public void setCovered_count(Long covered_count) {
        this.covered_count = covered_count;
    }

    public void setCovered_count(long covered_count) {
        this.covered_count = covered_count;
    }

    public Double getCovered_rate() {
        return covered_rate;
    }

    public void setCovered_rate(Double covered_rate) {
        this.covered_rate = covered_rate;
    }

    public void setCovered_rate(double covered_rate) {
        this.covered_rate = covered_rate;
    }


    public TagCategoryLastLevel getParent() {
        return parent;
    }

    public void setParent(TagCategoryLastLevel parent) {
        this.parent = parent;
    }
}
