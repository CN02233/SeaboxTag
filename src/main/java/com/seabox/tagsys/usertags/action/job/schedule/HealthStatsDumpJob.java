package com.seabox.tagsys.usertags.action.job.schedule;

import com.seabox.tagsys.usertags.action.job.HealthStatsInstance;
import com.seabox.tagsys.usertags.action.job.HealthStatsManager;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Changhua, Wu
 *         Created on: 2/26/16,1:56 PM
 */
@SingletonBean
public class HealthStatsDumpJob implements HealthStatsManager {


    private static final Logger logger = LoggerFactory.getLogger(HealthStatsDumpJob.class);

    public Set<HealthStatsInstance> healthStatsInstances = new CopyOnWriteArraySet<>();

    public Set<HealthStatsInstance> pendingRemovedStats = new CopyOnWriteArraySet<>();


    public HealthStatsDumpJob() {
        logger.info("------------ create new job {}", this.hashCode());
    }

    public void doDump() {

        if(healthStatsInstances.isEmpty()) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("\n------  dump health stats on  ");
        sb.append(new Date());
        sb.append("  ------\n");
        for(HealthStatsInstance instance: healthStatsInstances) {
            sb.append("########### stats for ");
            sb.append( instance.getStatsTitle());
            sb.append("  ###########\n");

            Map<String, String> statsMap = instance.getStatsMap();
            if(statsMap!= null) {
                for(Map.Entry<String, String> entry: statsMap.entrySet()){
                    sb.append( entry.getKey() );
                    sb.append(" : ");
                    sb.append( entry.getValue());
                    sb.append("\n");
                }
            }

            sb.append("\n");

        }
        sb.append("------  end stats -----");

        for(HealthStatsInstance instance: pendingRemovedStats) {
            healthStatsInstances.remove(instance);
            pendingRemovedStats.remove(instance);
        }

        logger.info( sb.toString() );

    }

    public void register(HealthStatsInstance healthStatsInstance) {
        healthStatsInstances.add(healthStatsInstance);
    }

    @Override
    public void unRegisterAfterNextDump(HealthStatsInstance healthStatsInstance) {
        pendingRemovedStats.add(healthStatsInstance);
    }


}
