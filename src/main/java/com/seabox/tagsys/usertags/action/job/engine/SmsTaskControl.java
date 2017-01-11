package com.seabox.tagsys.usertags.action.job.engine;

/**
 * Created by wuchh on 3/11/16.
 */
public interface SmsTaskControl {

    boolean isWorking();

    void waitForResume(Object taskObj) throws InterruptedException;

}
