package com.seabox.tagsys.usertags.logicquery.impl;

import com.seabox.tagsys.usertags.logicquery.TagCondition;

import javax.xml.bind.annotation.XmlType;


/**
 * @author Changhua, Wu
 *         Created on: 1/19/16,3:18 PM
 */
@XmlType(name = "TagCondition")
public abstract class TagConditionBase implements TagCondition {

    public And and(TagCondition b) {
        And condition;
        if(this instanceof And) {
            condition = (And)this;
            condition.conditions.add( b );
        } else if (b instanceof And) {
            condition = (And)b;
            condition.conditions.add( this );
        } else {
            condition = new And();
            condition.conditions.add(this);
            condition.conditions.add( b );
        }
        return condition;
    }

    public Or or(TagCondition b) {
        Or condition;
        if(this instanceof Or) {
            condition = (Or)this;
            condition.conditions.add( b );
        } else if (b instanceof Or) {
            condition = (Or)b;
            condition.conditions.add( this );
        } else {
            condition = new Or();
            condition.conditions.add(this);
            condition.conditions.add( b );
        }
        return condition;
    }


}