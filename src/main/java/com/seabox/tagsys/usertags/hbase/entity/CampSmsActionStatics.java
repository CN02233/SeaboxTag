package com.seabox.tagsys.usertags.hbase.entity;

/**
 *  活动的短信执行结果统计
 *
 * @author Changhua, Wu
 *         Created on: 2/21/16,1:20 PM
 */
public interface CampSmsActionStatics {

    /**
     * @return   该活动的原始用户数目(活动提交的时候,保存的: 根据 标签逻辑组合条件, 计算出来的用户数量)
     */
    long getTotalUserCount();

    /**
     * @return   该活动的实际发出短信用户数量 (调用sendMessage()发送时, SMS接口返回正确)
     */
    long getSendCount();


    /**
     * @return   该活动的发短信调用出错用户数量(调用sendMessage()发送时, SMS接口返回错误)
     */
    long getSendFailCount();


    /**
     * @return   该活动的短信发送后, 收到回执是成功的用户数量
     */
    long getRecvCount();

    /**
     * @return   该活动的短信发送后, 收到回执是失败的用户数量
     */
    long getRecvFailCount();


    /**
     * TODO: 暂未实现, 因为短信接口那边,暂时无法将 回复的短信 和发送时的 流水号对应起来, 所以 短信回复暂时无法分析
     *
     * @return   该活动的短信 有回复的用户数量
     */
    long getAckCount();



    /**
     * @return   该活动用户中有有效手机号的用户数量
     */
    long getValidUserCount();

    /**
     * @return   该活动用户中无效用户数量 (在用户信息表中不存在,或者手机号码无效,目前只简单校验手机字串>=11个字符)
     */
    long getInValidUserCount();



    /**
     * @return   该活动用户中 被加入黑名单，被排除的 用户数量
     */
    long getExcludeUsers();

}
