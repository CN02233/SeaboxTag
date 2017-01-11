package com.seabox.tagsys.usertags.logicquery.impl;

import com.seabox.tagsys.usertags.logicquery.TagConditionVisitor;
import com.seabox.tagsys.usertags.logicquery.TagQueryResult;
import com.seabox.tagsys.usertags.logicquery.TagCondition;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Changhua, Wu
 *         Created on: 1/19/16,3:19 PM
 */
@XmlRootElement(name="and")
public class And extends TagConditionBase {

    @JsonProperty
    @XmlElementRef(type = TagConditionBase.class)
    protected List<TagCondition> conditions = new ArrayList<>();


    @Override
    public <R extends TagQueryResult> R accept(TagConditionVisitor<R> visitor) {
        R   result = visitor.visitTagCondition(this);
        return result;
    }


    public List<TagCondition> getConditions() {
        return conditions;
    }

}