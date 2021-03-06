package com.seabox.tagsys.usertags.logicquery;

/**
 * @author Changhua, Wu
 *         Created on: 1/19/16,3:17 PM
 */

public interface TagConditionVisitor <R extends TagQueryResult>{

    R visitTagCondition(TagCondition op);

}