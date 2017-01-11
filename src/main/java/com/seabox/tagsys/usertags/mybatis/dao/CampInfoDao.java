package com.seabox.tagsys.usertags.mybatis.dao;

import com.seabox.tagsys.usertags.entity.CampInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @author Changhua, Wu
 *         Created on: 2/17/16,10:48 PM
 */
public interface CampInfoDao {




    String  SELECT_SQL_FOR_CAMP = "select  " +
            "Camp.camp_id as camp_id," +
            "Camp.camp_nm as camp_nm," +
            "Camp.start_dt as start_dt," +
            "Camp.end_dt as end_dt," +
            "Camp.indv_num as indv_num," +
            "Camp.camp_status_cd as camp_status_cd, " +
            "Camp.camp_chnl_cd as camp_chnl_cd," +
            "Camp.camp_inds_cd as camp_inds_cd," +
            "Camp.templt_id  as templt_id  "  // +
            // "c.created_ts   as created_ts, " +
            // "c.updated_ts   as updated_ts  "
            ;


    /*
'02', '提交审批'
'03', '已审批'
     */
    @Many
    @Select(SELECT_SQL_FOR_CAMP +
            " from h62_campaign_info Camp where templt_id is not null and (camp_status_cd ='03' or camp_status_cd ='02') and UNIX_TIMESTAMP(end_dt) >= UNIX_TIMESTAMP(now()) ")
    @Options(useCache = false)
    List<CampInfo> getCommitOrApprovedCampaigns();


    /*
'03', '已审批'
'04', '执行中'
     */
    @Many
    @Select(SELECT_SQL_FOR_CAMP +
            " from h62_campaign_info Camp where templt_id is not null and (camp_status_cd ='03' or camp_status_cd ='04') and UNIX_TIMESTAMP(start_dt) <= UNIX_TIMESTAMP(now()) and UNIX_TIMESTAMP(end_dt) >= UNIX_TIMESTAMP(now()) ")
    @Options(useCache = false)
    List<CampInfo> getApprovedOrActionCampaignsNeedExec();

    @One
    @Select(SELECT_SQL_FOR_CAMP +
            " from h62_campaign_info Camp  where templt_id is not null and camp_id=#{camp_id} ")
    @Options(useCache = false)
    CampInfo getCampaignInfo(@Param("camp_id") String camp_id);


    @Select("select  p.templt_para_value as templt_para_value " +
            "    from h62_camp_template_paras p " +
            "    where p.camp_id=#{camp_id}  " +
            "    order by p.templt_para_seq ")
    @Options(useCache = false)
    List<String>   getCampSmsTemplateParameters(@Param("camp_id") String camp_id);



    @Update("update h62_campaign_info set camp_status_cd=#{status} where camp_id=#{camp_id}")
    void  updateCampExecutionStatus(@Param("camp_id") String camp_id, @Param("status") String status);


    //TODO: need ask IDC admin, get privilege to create trigger for this and then turn on Cache, to avoid query on NO changes.
    @Select(value = "select contact_id as contact_id " +
            "  from h62_campaign_black where camp_chnl_cd=${camp_chnl_cd} and camp_inds_cd=${camp_inds_cd}")
    @Options(useCache = false, keyProperty = "contact_id", keyColumn = "contact_id")
    Set<String> getExcludeUserList(@Param("camp_chnl_cd") String camp_channel, @Param("camp_inds_cd") String camp_industry);

    /**
     *
     *
     # camp_status_cd, camp_status_nm
     01, 未提交
     02, 提交审批
     10, 审批驳回
     90, 已删除

     * @return  all campaigns belongs to the user groups within N days
     *
     */
    @Many
    @Select(SELECT_SQL_FOR_CAMP +
            " from h62_campaign_info  Camp where camp_status_cd  not in ('01','02', '10', '90') "
            + "\n and  DATE_SUB( now(), INTERVAL ${daysBefore} DAY) <= start_dt "
            + "\n and user_id = ${userId} ) "
    )
    @Options(useCache = false)
    List<CampInfo> getMyUserCampsBeforeDays(@Param("daysBefore") int daysBefore, @Param("userId") int userId);


    /**
     *
     * @return  all campaigns belongs to the user groups within N days
     */
    @Many
    @Select(SELECT_SQL_FOR_CAMP +
            " from h62_campaign_info Camp, h62_sys_user User  where Camp.user_id = User.user_id"
            + "\n and camp_status_cd  not in ('01','02', '10', '90') "
            + "\n and DATE_SUB( now(), INTERVAL ${daysBefore} DAY) <= start_dt "
            + "\n and User.user_group_id = ${userGroupId} ) "
    )
    @Options(useCache = false)
    List<CampInfo> getMyGroupCampsBeforeDays(@Param("daysBefore") int daysBefore, @Param("userGroupId") int userGroupId);


}
