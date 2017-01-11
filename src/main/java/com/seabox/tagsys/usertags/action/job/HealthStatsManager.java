package com.seabox.tagsys.usertags.action.job;

/**
 * @author Changhua, Wu
 *         Created on: 2/26/16,2:06 PM
 */
public interface HealthStatsManager {

    void register(HealthStatsInstance healthStatsInstance);

    void unRegisterAfterNextDump(HealthStatsInstance healthStatsInstance);

}
