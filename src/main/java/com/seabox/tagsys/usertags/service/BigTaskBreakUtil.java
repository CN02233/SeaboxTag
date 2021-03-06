package com.seabox.tagsys.usertags.service;

import com.seabox.tagsys.usertags.action.job.HealthStatsManager;
import com.seabox.tagsys.usertags.utils.SingletonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Changhua, Wu
 *         Created on: 2/22/16,11:05 AM
 */
@SingletonBean
public class BigTaskBreakUtil {

    private static final Logger logger = LoggerFactory.getLogger(BigTaskBreakUtil.class);

    @Autowired
    private HealthStatsManager healthStatsManager;

    private static BigTaskBreakUtil springBean;

    @PostConstruct
    public void init() {
        synchronized (BigTaskBreakUtil.class) {
            springBean = this;
        }
    }

    @PreDestroy
    public void unload() {
        synchronized (BigTaskBreakUtil.class) {
            springBean = null;
        }
    }

    public static void breakExec(final TaskBreakExecutor subTask) {
        BigTaskBreakUtil util = springBean;
        if(util==null) { // used for Junit test, no need to bind with Spring
            util = new BigTaskBreakUtil();
        }
        util.breakExecWith(subTask);
    }


    public void breakExecWith(final TaskBreakExecutor subTask) {

        Calendar startTime = Calendar.getInstance();

        long totalCount = subTask.getTotalCount();
        final int THREAD_COUNT = subTask.getTOTAL_TASK_THREADS();

        subTask.onStart();
        if(healthStatsManager!= null) {
            healthStatsManager.register( subTask );
        }


        logger.info("======== begin breakExec({})  todo totalCount={}", subTask, totalCount );

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        long stepRange = totalCount / THREAD_COUNT;
        long splitRemainCount = totalCount % THREAD_COUNT;

        long lastEnd = 0;
        for(int t=0; t<THREAD_COUNT; ++t) { // Note for t= 0 ~ THREAD_COUNT(included), use total THREAD_COUNT +1 to cover all the segment ranges
            final long start = lastEnd;
            long endPos = start + stepRange + ( (splitRemainCount--)>0 ? 1 : 0);

            final int  taskId = t;

            if(endPos> totalCount) {
                endPos = totalCount;
            }
            final long end = endPos;
            if(start>= totalCount || start==end) {
                break;
            }
            lastEnd = end;

            Runnable segmentTask = new Runnable() {

                @Override
                public void run() {


                    logger.info("breakExec( {} ) segmentTask begin taskId={}, {} <= index < {}", subTask, taskId, start, end);

                    if(subTask.isExecRange()) {
                        subTask.execRange( taskId, start, end);
                    } else {
                        for(long i=start; i<end; ++i) {

                            try {
                                subTask.execOne(taskId, i);
                            } catch (Throwable e) {
                                logger.error("breakExec() Exception:", e );
                            }

                        }
                    }


                    subTask.onComplete(taskId);

                    logger.info("breakExec({}) segmentTask Done taskId={}, {} <= index < {}", subTask, taskId, start, end);
                }
            };
            executor.submit(segmentTask);

        }


        executor.shutdown();
        // wait until all tasks are finished
        while( true ) {
            try {
                executor.awaitTermination(30, TimeUnit.SECONDS);

                Calendar checkPoint = Calendar.getInstance();
                long elapsed = (checkPoint.getTimeInMillis() -  startTime.getTimeInMillis() ) / 1000; //convert milli-seconds to Seconds

                if(executor.isTerminated()) {
                    logger.info("======== done breakExec({})  complete totalCount={}, elapsed={} sec", subTask, totalCount, elapsed );
                    break;
                } else {

                    logger.info("======== on-going breakExec({})  todo totalCount={}, elapsed={} sec, execution status: {}", subTask, totalCount, elapsed, subTask.dumpProgress() );
                }

            } catch (InterruptedException e) {
                logger.info("I was Interrupted  " + this + " : " + subTask, e);

                Thread myThread = Thread.currentThread();
                myThread.interrupt();
            }
        }

        subTask.onDone();

        if(healthStatsManager!= null) {
            healthStatsManager.unRegisterAfterNextDump(subTask);
        }

    }

}
