package com.seabox.tagsys.usertags.entity;

/**
 * @author Changhua, Wu
 *         Created on: 1/23/16,5:31 PM
 */


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  Virtual Group of Tags, which don't include any  real Tags as sub-items
 *  Level 1, 2 items in Tag_Categories table,
 *  　for example: 基本信息　／　人口属性
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value=TagCategoryHighLevel.class, name="group"),
        @JsonSubTypes.Type(value=TagCategoryLastLevel.class, name="category") })
public abstract  class  TagCategoryBase <PARENT, CHILD> implements Serializable {
    /*
           fields get from DB table
         */
    protected int          tag_ctgy_id;
    protected String       tag_ctgy_nm;
    protected String       tag_desc;

    protected String       tag_type_cd;
    protected Integer      up_tag_ctgy_id;
    protected Boolean      have_tag_ind;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected Date         enabled_dt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected Date         disabled_dt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    protected Date         created_ts;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    protected Date         updated_ts;


    /*
       additional link for Child->Parent
     */
    protected PARENT parent;
    protected List<CHILD> children;


    public int getTag_ctgy_id() {
        return tag_ctgy_id;
    }

    public void setTag_ctgy_id(int tag_ctgy_id) {
        this.tag_ctgy_id = tag_ctgy_id;
    }

    public String getTag_ctgy_nm() {
        return tag_ctgy_nm;
    }

    public void setTag_ctgy_nm(String tag_ctgy_nm) {
        this.tag_ctgy_nm = tag_ctgy_nm;
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

    public Integer getUp_tag_ctgy_id() {
        return up_tag_ctgy_id;
    }

    public void setUp_tag_ctgy_id(Integer up_tag_ctgy_id) {
        this.up_tag_ctgy_id = (null ==up_tag_ctgy_id) ? null : up_tag_ctgy_id;
    }

    public Boolean getHave_tag_ind() {
        return have_tag_ind;
    }

    public void setHave_tag_ind(Boolean have_tag_ind) {
        this.have_tag_ind = (null ==have_tag_ind) ? null : have_tag_ind;
    }


    public Date getEnabled_dt() {
        return enabled_dt;
    }

    public void setEnabled_dt(Date enabled_dt) {
        this.enabled_dt = enabled_dt;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getDisabled_dt() {
        return disabled_dt;
    }

    public void setDisabled_dt(Date disabled_dt) {
        this.disabled_dt = disabled_dt;
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


    public PARENT getParent() {
        return parent;
    }

    public void setParent(PARENT parent) {
        this.parent = parent;
    }

    public List<CHILD> getChildren() {
        return children;
    }

    public void setChildren(List<CHILD> children) {
        this.children = children;
    }

    public List<CHILD>  createEmptyListOfChildren() {
        return new ArrayList<CHILD>();
    }
}
