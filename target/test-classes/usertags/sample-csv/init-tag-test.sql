
--DROP TABLE h50_tag_category_info;
--DROP TABLE h50_tag_info;
--DROP TABLE h62_user_tag_favorite;



CREATE TABLE IF NOT EXISTS  `h50_tag_category_info` (
  `tag_ctgy_id` int(11) DEFAULT NULL,
  `tag_ctgy_nm` varchar(200) DEFAULT NULL,
  `tag_desc` varchar(1000) DEFAULT NULL,
  `tag_type_cd` char(1) DEFAULT NULL,
  `up_tag_ctgy_id` char(20) DEFAULT NULL,
  `have_tag_ind` char(1) DEFAULT NULL,
  `tag_ctgy_status` int(11) DEFAULT 2,
  `enabled_dt` date DEFAULT NULL,
  `disabled_dt` date DEFAULT NULL,
  `created_ts` timestamp NULL DEFAULT NULL,
  `updated_ts` timestamp NULL DEFAULT NULL
) AS  SELECT
              tag_ctgy_id,
              tag_ctgy_nm,
              tag_desc,
              tag_type_cd,
              up_tag_ctgy_id,
              have_tag_ind,
              tag_ctgy_status,
              enabled_dt,
              disabled_dt,
        CASEWHEN(created_ts='', NULL, convert(parseDateTime(created_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as created_ts,
        CASEWHEN(updated_ts='', NULL, convert(parseDateTime(updated_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as updated_ts
            FROM CSVREAD('__TAGS_TEST_MOCKDB_DIR__/h50_tag_category_info.csv');


CREATE TABLE IF NOT EXISTS  `h50_tag_info` (
  `tag_id` int(11) DEFAULT NULL,
  `tag_nm` varchar(200) DEFAULT NULL,
  `tag_desc` varchar(1000) DEFAULT NULL,
  `tag_type_cd` char(1) DEFAULT NULL,
  `tag_ctgy_id` int(11) DEFAULT NULL,
  `enabled_dt` date DEFAULT NULL,
  `disabled_dt` date DEFAULT NULL,
  `active_ind` char(1) DEFAULT '1',
  `unknown_ind` char(1) DEFAULT '0',
  `created_ts` timestamp NULL DEFAULT NULL,
  `updated_ts` timestamp NULL DEFAULT NULL
) AS  SELECT
            tag_id,
            tag_nm,
            tag_desc,
            tag_type_cd,
            tag_ctgy_id,
            enabled_dt,
            disabled_dt,
            active_ind,
            unknown_ind,
        CASEWHEN(created_ts='', NULL, convert(parseDateTime(created_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as created_ts,
        CASEWHEN(updated_ts='', NULL, convert(parseDateTime(updated_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as updated_ts
          FROM CSVREAD('__TAGS_TEST_MOCKDB_DIR__/h50_tag_info.csv');






CREATE TABLE IF NOT EXISTS  `h62_user_tag_favorite` (
  `user_id` int(11) NOT NULL,
  `tag_ctgy_id` int(11) NOT NULL,
  `created_ts` datetime DEFAULT NULL,
  `updated_ts` datetime DEFAULT NULL
) AS SELECT
       user_id,
       tag_ctgy_id,
       CASEWHEN(created_ts='', NULL, convert(parseDateTime(created_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as created_ts,
       CASEWHEN(updated_ts='', NULL, convert(parseDateTime(updated_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as updated_ts
     FROM CSVREAD('__TAGS_TEST_MOCKDB_DIR__/h62_user_tag_favorite.csv');



CREATE TABLE IF NOT EXISTS `h62_campaign_black` (
  `contact_id` varchar(60) NOT NULL,
  `camp_chnl_cd` char(2) DEFAULT NULL,
  `camp_inds_cd` char(4) DEFAULT NULL,
  `created_ts` datetime DEFAULT NULL,
  `updated_ts` datetime DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
) AS SELECT
       contact_id,
       camp_chnl_cd,
       camp_inds_cd,
       CASEWHEN(created_ts='', NULL, convert(parseDateTime(created_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as created_ts,
       CASEWHEN(updated_ts='', NULL, convert(parseDateTime(updated_ts,'yyyy-MM-dd hh:mm:ss'),timestamp)) as updated_ts
     FROM CSVREAD('__TAGS_TEST_MOCKDB_DIR__/h62_campaign_black.csv');