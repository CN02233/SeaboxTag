package com.seabox.tagsys.usertags.service.impl;

import com.seabox.tagsys.usertags.logicquery.TagCondition;
import com.seabox.tagsys.usertags.logicquery.TagConditionVisitor;
import com.seabox.tagsys.usertags.logicquery.impl.And;
import com.seabox.tagsys.usertags.logicquery.impl.Match;
import com.seabox.tagsys.usertags.logicquery.impl.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 2/15/16,5:07 PM
 */
public class GrepMatchConditionVisitor implements TagConditionVisitor<GrepMatchConditionResult> {

    private static final Logger logger = LoggerFactory.getLogger(GrepMatchConditionVisitor.class);

    GrepMatchConditionResult   allMatchTagIds = new GrepMatchConditionResult();

    @Override
    public GrepMatchConditionResult  visitTagCondition(TagCondition op) {

        if(op instanceof Match)
        {
            Match opMatch = (Match)op;
            allMatchTagIds.add(opMatch.getTagId());
        } else if(op instanceof And)
        {
            List<TagCondition> children = ((And)op).getConditions();
            for(TagCondition child: children) {
                visitTagCondition( child );
            }
        } else if(op instanceof Or)
        {
            List<TagCondition> children = ((Or)op).getConditions();
            for(TagCondition child: children) {
                visitTagCondition( child );
            }
        } else {
            logger.error("Error, GrepMatchConditionResult:  the TagCondition is not handled: {} ", op.getClass());
        }
        return allMatchTagIds;
    }
}

