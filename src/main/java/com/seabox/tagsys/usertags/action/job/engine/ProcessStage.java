package com.seabox.tagsys.usertags.action.job.engine;

import com.seabox.tagsys.usertags.action.job.engine.stage0.StepAInput;

/**
 * Created by wuchh on 3/13/16.
 */
public interface ProcessStage<INPUT> {

    void processStage(INPUT input);

}
