package com.seabox.tagsys.usertags.service;

/**
 * @author Changhua, Wu
 *         Created on: 5/13/16,10:23 AM
 */
public interface TaskWithStatsCounter {


    void countStart(int taskId);
    void countStart(int taskId, int value);

    void countComplete(int taskId);
    void countComplete(int taskId, int value);

    void countError(int taskId);
    void countError(int taskId, int value);


    long getCountStart();

    long getCountComplete();

    long getCountError() ;

}
