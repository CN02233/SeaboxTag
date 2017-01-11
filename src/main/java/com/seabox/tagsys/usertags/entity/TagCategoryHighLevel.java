package com.seabox.tagsys.usertags.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * @author Changhua, Wu
 *         Created on: 1/25/16,9:55 AM
 */
@JsonInclude(content= JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"created_ts","updated_ts", "parent","tag_type_cd","up_tag_ctgy_id","enabled_dt","disabled_dt"})
@JsonTypeName("group")
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, visible=true)
public class TagCategoryHighLevel extends TagCategoryBase<TagCategoryHighLevel, TagCategoryBase >  implements Serializable {

    private Integer sub_ctgy_depth;

    public Integer getSub_ctgy_depth() {
        return sub_ctgy_depth;
    }

    public void setSub_ctgy_depth(Integer sub_ctgy_depth) {
        this.sub_ctgy_depth = sub_ctgy_depth;
    }
}
