package com.seabox.tagsys.usertags.service;

import com.seabox.tagsys.usertags.hbase.entity.CampSmsActionStatics;
import com.seabox.tagsys.usertags.logicquery.TagCondition;
import com.seabox.tagsys.usertags.logicquery.UsersDistributeByTag;

import javax.annotation.Nullable;
import java.util.Collection;


/**
 * 缓存服务 接口定义
 *
 *
 * @author Changhua, Wu
 *         Created on: 1/25/16,2:55 PM
 */
public interface CacheService {



    /**
     * 按照给定的查询条件，得到满足条件的用户数量
     *
     * @param condition, 一个活动所选择的标签组合 之 “逻辑条件”，TagCondition　是接口，需要一个具体Object实例,
     *                   可以是一个　And(代表＂和＂) / Or(代表＂或＂) / Match(代表"匹配"一个具体的标签).
     * @param excludeUserInCamps   (可选,可以为Null)去重排除活动号列表, 如果有则将排除这些活动中的用户
     * @return　long　　满足条件的　用户数量
     *
     */
    long getNumOfUsersByCondition(TagCondition condition, @Nullable Collection<String> excludeUserInCamps);



    /**
     * UI 查询透视率,
     * @param condition   一个活动所选择的标签组合 之 “逻辑条件”
     * @param excludeUserInCamps   (可选,可以为Null)去重排除活动号列表, 如果有则将排除这些活动中的用户
     * @return  结果包括 最终用户数量, 和按照标签的 透视率
     * @see UsersDistributeByTag
     */
    UsersDistributeByTag  getUsersDistributeByTag(TagCondition condition, @Nullable Collection<String> excludeUserInCamps);



    /**
     *  活动提交时，保存活动的缓存快照信息
     *   （为啥要“快照”？ 因为缓存会和Hive 源 同步和更新  ）
     *
     *    活动模块职责： 需要 在 UI 点击 活动 “提交”时调用这个接口, 触发活动缓存的快照保存
     *    缓存模块职责： 将 活动对应的 具体用户列表， 标签逻辑条件， 等信息保存在 hBase里。
     *
     * @param campId     活动号，ID
     * @param condition  该活动所选择的标签组合 之 “逻辑条件”
     * @param excludeUserInCamps   (可选,可以为Null)去重排除活动号列表, 如果有则将排除这些活动中的用户
     */
    void    commitAndSaveCacheForCampaign(int  campId, TagCondition condition, @Nullable Collection<String> excludeUserInCamps);


    /**
     *   获取 活动执行后的统计数据
     *
     * @param campId  活动号ID
     *
     * @return  活动的短信执行结果统计
     */
    CampSmsActionStatics getCampSmsActionStatics(int campId);



}
