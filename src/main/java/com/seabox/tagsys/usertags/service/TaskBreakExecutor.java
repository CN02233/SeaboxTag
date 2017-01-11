package com.seabox.tagsys.usertags.service;

import com.seabox.tagsys.usertags.action.job.HealthStatsInstance;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Changhua, Wu
 *         Created on: 2/22/16,11:20 AM
 */
public abstract class TaskBreakExecutor implements TaskWithStatsCounter, HealthStatsInstance{


    /**
     *
     * @param taskId
     * @param index
     */
    public abstract void execOne(int taskId, long index);



    public abstract void onComplete(int taskId);


    private volatile Calendar startTime;
    private volatile Calendar endTime;
    private final String name;
    private final long totalCount;

    public void onStart() {
        startTime = Calendar.getInstance();
    }

    public void onDone() {
        endTime = Calendar.getInstance();
    }

    /**
     *
     * @param taskId
     * @param start
     * @param end
     */
    public abstract void execRange (int taskId, long start, long end);


    public String getName() {
        return name;
    }

    public long getTotalCount() {
        return totalCount;
    }

    protected final int TOTAL_TASK_THREADS;
    private boolean execRange;
    protected final Map<Integer, AtomicLong> startCounterMapByTaskId = new HashMap<>();
    protected final Map<Integer, AtomicLong> completeCounterMapByTaskId = new HashMap<>();
    protected final Map<Integer, AtomicLong> errorCounterMapByTaskId = new HashMap<>();

    public boolean isExecRange() {
        return execRange;
    }

    public TaskBreakExecutor(String name, int totalTaskThreads, long totalCount, boolean execRange) {

        this.name = name;
        this.totalCount = totalCount;

        this.TOTAL_TASK_THREADS = totalTaskThreads;

        for(int i=0; i< totalTaskThreads; ++i) {
            startCounterMapByTaskId.put( i, new AtomicLong(0L));
            completeCounterMapByTaskId.put( i, new AtomicLong(0L));
            errorCounterMapByTaskId.put(i, new AtomicLong(0L));
        }
        this.execRange = execRange;

        init();
    }


    abstract protected void init();

    @Override
    public String toString() {
        return getName() + " objId=" + this.hashCode();
    }

    public String dumpProgress() {
        StringBuffer sb = new StringBuffer();

        sb.append("TaskBreakExecutor task ")
                .append("start: ")
                .append( getCountStart() )
                .append(", done: ")
                .append( getCountComplete() )
                .append(", error: ")
                .append( getCountError() )
                .append(", progress: ")
                .append( 100 * getCountStart() / getTotalCount() )
                .append("%");

        return sb.toString();

    }

    public Map<Integer, AtomicLong> getStartCounterMapByTaskId() {
        return startCounterMapByTaskId;
    }

    public Map<Integer, AtomicLong> getCompleteCounterMapByTaskId() {
        return completeCounterMapByTaskId;
    }

    public Map<Integer, AtomicLong> getErrorCounterMapByTaskId() {
        return errorCounterMapByTaskId;
    }

    @Override
    public void countStart(int taskId) {
        startCounterMapByTaskId.get(taskId).incrementAndGet();
    }


    @Override
    public void countStart(int taskId, int value) {
        startCounterMapByTaskId.get(taskId).getAndAdd( value);
    }


    @Override
    public long getCountStart() {
        long counter = 0;
        for(int i=0; i< TOTAL_TASK_THREADS; ++i) {
            counter += startCounterMapByTaskId.get(i).get();
        }
        return counter;
    }


    @Override
    public void countComplete(int taskId) {
        completeCounterMapByTaskId.get(taskId).incrementAndGet();
    }

    @Override
    public void countComplete(int taskId, int value) {
        completeCounterMapByTaskId.get(taskId).getAndAdd(value);
    }

    public long getCountComplete() {
        long counter = 0;
        for(int i=0; i< TOTAL_TASK_THREADS; ++i) {
            counter += completeCounterMapByTaskId.get(i).get();
        }
        return counter;
    }


    @Override
    public void countError(int taskId) {
        errorCounterMapByTaskId.get( taskId ).incrementAndGet();
    }

    @Override
    public void countError(int taskId, int value) {
        errorCounterMapByTaskId.get( taskId ).getAndAdd(value);
    }


    public long getCountError() {
        long counter = 0;
        for(int i=0; i< TOTAL_TASK_THREADS; ++i) {
            counter += errorCounterMapByTaskId.get(i).get();
        }
        return counter;
    }


    public int getTOTAL_TASK_THREADS() {
        return TOTAL_TASK_THREADS;
    }


    @Override
    public String getStatsTitle(){
        return this.toString();
    }

    @Override
    public Map<String, String>  getStatsMap(){
        Map<String, String> statsMap = new HashMap<>();
        statsMap.put("start", String.valueOf( getCountStart() ));
        statsMap.put("complete", String.valueOf( getCountComplete() ));
        statsMap.put("error", String.valueOf( getCountError() ));
        statsMap.put("totalCount", String.valueOf( totalCount ));

        Calendar checkPoint = endTime;
        if(checkPoint==null) {
            checkPoint = Calendar.getInstance();
            statsMap.put("status", "on-going");
        } else {
            statsMap.put("status", "Done");
        }
        long elapsed = (checkPoint.getTimeInMillis() -  startTime.getTimeInMillis() ) / 1000; //convert milli-seconds to Seconds

        statsMap.put("elapsedSecond", String.valueOf( elapsed ));

        return statsMap;
    }

}