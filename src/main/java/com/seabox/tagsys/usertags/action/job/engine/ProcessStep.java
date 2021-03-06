package com.seabox.tagsys.usertags.action.job.engine;

/**
 * Created by wuchh on 3/10/16.
 */
public interface ProcessStep<INPUT, OUTPUT>{

    void beforeProcess();

    OUTPUT  process(INPUT input);

    void afterProcess();

}
