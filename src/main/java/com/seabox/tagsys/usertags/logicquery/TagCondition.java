package com.seabox.tagsys.usertags.logicquery;



import com.seabox.tagsys.usertags.logicquery.impl.And;
import com.seabox.tagsys.usertags.logicquery.impl.Match;
import com.seabox.tagsys.usertags.logicquery.impl.Or;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * @author Changhua, Wu
 *         Created on: 1/19/16,3:15 PM
 */

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value=And.class, name="And"),
    @JsonSubTypes.Type(value=Or.class, name="Or"),
    @JsonSubTypes.Type(value=Match.class, name="Match")})
public interface TagCondition extends Serializable{

    <R extends TagQueryResult> R accept(TagConditionVisitor<R> visitor);


}