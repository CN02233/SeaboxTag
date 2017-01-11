package com.seabox.tagsys.usertags.action.job.engine.stage0;

import com.seabox.tagsys.usertags.action.job.engine.BaseCampObj;
import com.seabox.tagsys.usertags.logicquery.TagCondition;

import java.util.Collection;

/**
 * Created by wuchh on 3/10/16.
 */
public class StepAInput extends BaseCampObj {


    TagCondition condition;
    Collection<String> excludeUserInCamps;


    public TagCondition getCondition() {
        return condition;
    }

    public void setCondition(TagCondition condition) {
        this.condition = condition;
    }

    public Collection<String> getExcludeUserInCamps() {
        return excludeUserInCamps;
    }

    public void setExcludeUserInCamps(Collection<String> excludeUserInCamps) {
        this.excludeUserInCamps = excludeUserInCamps;
    }
}
