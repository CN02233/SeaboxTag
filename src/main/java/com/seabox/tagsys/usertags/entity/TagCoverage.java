package com.seabox.tagsys.usertags.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Changhua, Wu
 *         Created on: 1/27/16,1:48 PM
 */
@JsonInclude(content= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"created_ts","updated_ts"})
public class TagCoverage  implements Serializable {

    private int      tag_id;
    private int      tag_ctgy_id;
    private int      indv_sum;
    private double   indv_pcnt;  // Percentage of Coverage
    private Date     created_ts;
    private Date     updated_ts;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public int getTag_ctgy_id() {
        return tag_ctgy_id;
    }

    public void setTag_ctgy_id(int tag_ctgy_id) {
        this.tag_ctgy_id = tag_ctgy_id;
    }

    public int getIndv_sum() {
        return indv_sum;
    }

    public void setIndv_sum(int indv_sum) {
        this.indv_sum = indv_sum;
    }

    public double getIndv_pcnt() {
        return indv_pcnt;
    }

    public void setIndv_pcnt(double indv_pcnt) {
        this.indv_pcnt = indv_pcnt;
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

}
