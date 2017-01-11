package com.seabox.tagsys.usertags.logicquery;

import java.util.Map;

/**
 * @author Changhua, Wu
 *         Created on: 2/4/16,11:25 AM
 */

/**
 *  综合查询结果, 包括 最终用户数量, 和按照标签的 透视率
 */
public interface UsersDistributeByTag {

    /**
     * 获取标签计算后redis缓存的计算结果key值
     * add by SongChaoqun 2016-12-15
     * @return
     */
    String getKeyOfUsers();

    /**
     *
     * @return   符合 "标签组合查询条件"的 "最终结果" :  满足条件的所有用户的数量
     */
    long  getTotalNumOfUsers();

    /**
     *
     * @return  返回  一个Map, 包含"标签组合查询条件" 出现过的所有 标签 的 透视率 (这里不给百分比, 给的每个标签的用户数量 )
     *          key: Integer 标签ID
     *          value: Long 最终结果当中, 符合该标签的 用户数
     */
    Map<Integer,Long>    getTagToUserCountMap();

}
