package com.seabox.tagsys.usertags.hbase.entity;

/**
 * @author Changhua, Wu
 *         Created on: 2/19/16,3:22 PM
 */
public class CampSmsActionStaticsImpl implements CampSmsActionStatics {

    private long totalUserCount;
    private long validUserCount;
    private long inValidUserCount;
    private long sendCount;
    private long sendFailCount;
    private long recvCount;
    private long recvFailCount;
    private long ackCount;
    private long excludeUsers;

    public void setTotalUserCount(long totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public void setValidUserCount(long validUserCount) {
        this.validUserCount = validUserCount;
    }

    public void setInValidUserCount(long inValidUserCount) {
        this.inValidUserCount = inValidUserCount;
    }

    @Override
    public long getTotalUserCount() {
        return totalUserCount;
    }

    public long getSendCount() {
        return sendCount;
    }

    public void setSendCount(long sendCount) {
        this.sendCount = sendCount;
    }

    public long getRecvCount() {
        return recvCount;
    }

    public void setRecvCount(long recvCount) {
        this.recvCount = recvCount;
    }

    public long getAckCount() {
        return ackCount;
    }

    @Override
    public long getValidUserCount() {
        return validUserCount;
    }

    @Override
    public long getInValidUserCount() {
        return inValidUserCount;
    }

    public void setAckCount(long ackCount) {
        this.ackCount = ackCount;
    }

    public long getRecvFailCount() {
        return recvFailCount;
    }

    public void setRecvFailCount(long recvFailCount) {
        this.recvFailCount = recvFailCount;
    }

    public long getSendFailCount() {
        return sendFailCount;
    }

    public void setSendFailCount(long sendFailCount) {
        this.sendFailCount = sendFailCount;
    }

    public long getExcludeUsers() {
        return excludeUsers;
    }

    public void setExcludeUsers(long excludeUsers) {
        this.excludeUsers = excludeUsers;
    }
}
