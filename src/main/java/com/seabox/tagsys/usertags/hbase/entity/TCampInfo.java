package com.seabox.tagsys.usertags.hbase.entity;

//import com.seabox.tagsys.usertags.action.sms.SmsSequenceGenerator;
import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  Let's maintain comments For hbase object version changes for backward compatibility,
 *
 * ------------------
 * changes history 2016-05-12:
 * - cf:usersKeyInRedis
 * + buffers:usersBitSet
 *
 *
 *
 * @author Changhua, Wu
 *         Created on: 2/16/16,3:10 PM
 */
public final class TCampInfo {


    private static final Logger logger = LoggerFactory.getLogger(TCampInfo.class);



    public final static TableName tableName() {
        return TableName.valueOf("h62_campaign_indv");
    }

    public final static byte[]  ColFamily() {
        return "cf".getBytes();
    }

    private byte[]  usersBitSet;
    private String  tagJson;
    private String  numOfUsers;
    private String  status;



    public final static byte[] col_tagJson()          { return  "tagJson".getBytes(); }
    public final static byte[] col_numOfUsers()       { return  "numOfUsers".getBytes(); }
    public final static byte[] col_status()           { return  "status".getBytes(); }

    // ---------------  stats Counter -----------------------
    public final static byte[] col_validUser()        { return  "validUser".getBytes(); }
    public final static byte[] col_inValidUser()      { return  "inValidUser".getBytes(); }
    public final static byte[] col_excludeUser()      { return  "excludeUser".getBytes(); }
    public final static byte[] col_smsSendCount()     { return  "smsSendCount".getBytes(); }
    public final static byte[] col_smsSendFailCount() { return  "smsSendFailCount".getBytes(); }
    public final static byte[] col_smsRecvCount()     { return  "smsRecvCount".getBytes(); }
    public final static byte[] col_smsRecvFailCount() { return  "smsRecvFailCount".getBytes(); }
    public final static byte[] col_smsAckCount()      { return  "smsAckCount".getBytes(); }

    public final static class _Buffers {

        public final static byte[]  ColFamily() {
            return "buffers".getBytes();
        }

        public final static byte[]  col_usersBitSet()      { return "usersBitSet".getBytes(); }

    }

    public final static class _Users {

        public final static byte[]  ColFamily() {
            return "users".getBytes();
        }

    }

    public final static class _NoSuchUsers {

        public final static byte[]  ColFamily() {
            return "noSuchUsers".getBytes();
        }

    }

    public final static class _ErrorUsers {

        public final static byte[]  ColFamily() {
            return "errorUsers".getBytes();
        }

    }

    public final static class _SentUsers {

        public final static byte[]  ColFamily() {
            return "sentUsers".getBytes();
        }

    }


    public final static class _ExcludeUsers {

        public final static byte[]  ColFamily() {
            return "excludeUsers".getBytes();
        }

    }

    public final static void createOneNewRecord(Table campInfoTable, String campId, byte[] redisBitSet, String tagConditionJson, long numOfUsers) throws IOException{

        logger.info("createOneNewRecord() start  for campId: {}, campUsersKeyInRedis:{}, numOfUsers:{}", campId, numOfUsers);

        String rowKey = String.valueOf(campId);

        Delete del = new Delete( rowKey.getBytes() );
        del.addFamily(_Users.ColFamily());
        del.addFamily(_ErrorUsers.ColFamily());
        del.addFamily(_NoSuchUsers.ColFamily());
        del.addFamily(_SentUsers.ColFamily());
        del.addFamily(_ExcludeUsers.ColFamily());
        campInfoTable.delete( del );

        Put put = new Put( rowKey.getBytes() );

        if(null!= redisBitSet) {
            put.addColumn(_Buffers.ColFamily(), _Buffers.col_usersBitSet(), redisBitSet);
        }

        put.addColumn(ColFamily(),  col_tagJson(),         tagConditionJson.getBytes());
        put.addColumn(ColFamily(),  col_numOfUsers(),      String.valueOf(numOfUsers).getBytes());
        //put.addColumn(ColFamily(), col_version(), HBASE_CAMP_INFO_OBJ_VERSION_1_1_0.getBytes() );

        put.addColumn(ColFamily(),  col_validUser(),        HBaseUtil.ZERO_64BIT_INIT_VALUE);
        put.addColumn(ColFamily(),  col_inValidUser(),      HBaseUtil.ZERO_64BIT_INIT_VALUE);
        put.addColumn(ColFamily(),  col_smsSendCount(),     HBaseUtil.ZERO_64BIT_INIT_VALUE);
        put.addColumn(ColFamily(),  col_smsSendFailCount(), HBaseUtil.ZERO_64BIT_INIT_VALUE);
        put.addColumn(ColFamily(),  col_smsRecvCount(),     HBaseUtil.ZERO_64BIT_INIT_VALUE);
        put.addColumn(ColFamily(),  col_smsRecvFailCount(), HBaseUtil.ZERO_64BIT_INIT_VALUE);
        put.addColumn(ColFamily(),  col_smsAckCount(),      HBaseUtil.ZERO_64BIT_INIT_VALUE);
        put.addColumn(ColFamily(),  col_excludeUser(),      HBaseUtil.ZERO_64BIT_INIT_VALUE);
        // status -> Prepare

        campInfoTable.put( put );

        campInfoTable.close();

        logger.info("createOneNewRecord() Done  for campId: {}, campUsersKeyInRedis:{}, numOfUsers:{}", campId, numOfUsers);


    }




    public final static void addUser(Table campInfoTable,
                                     BufferedMutator campInfoBufferedMutator,
                                     TUserInfo userInfoRecord,
                                     BufferedMutator smsActionBufferedMutator,
                                     String campIdKey,
                                     String userGUID,
                                     final Set<String> excludeUsers) throws IOException {


        if(userInfoRecord == null || userInfoRecord.getMobileNo() == null) {

            HBaseUtil.addColumnWithValueAsync(campInfoBufferedMutator, campIdKey, _NoSuchUsers.ColFamily(), userGUID.getBytes(), "");

            HBaseUtil.increaseCounter( campInfoTable, campIdKey.getBytes(), ColFamily(),  col_inValidUser(), 1);

        } else  {
            //TODO: validate  mobile string is a valid number
            if( userInfoRecord.getMobileNo().length() < 11) {

                HBaseUtil.addColumnWithValueAsync(campInfoBufferedMutator, campIdKey, _ErrorUsers.ColFamily(), userGUID.getBytes(), userInfoRecord.getMobileNo());

                HBaseUtil.increaseCounter( campInfoTable, campIdKey.getBytes(), ColFamily(),  col_inValidUser(), 1);

            } else if(excludeUsers.contains( userInfoRecord.getMobileNo() )) {

                HBaseUtil.addColumnWithValueAsync(campInfoBufferedMutator, campIdKey, _ExcludeUsers.ColFamily(), userGUID.getBytes(), userInfoRecord.getMobileNo());

                HBaseUtil.increaseCounter( campInfoTable, campIdKey.getBytes(), ColFamily(),  col_excludeUser(), 1);

            } else { // OK user mobile num

//                String seqNum = SmsSequenceGenerator.getSmsSequenceNum();
                String seqNum = null;
                HBaseUtil.addColumnWithValueAsync(campInfoBufferedMutator, campIdKey, _Users.ColFamily(), userGUID.getBytes(), seqNum);

                HBaseUtil.increaseCounter( campInfoTable, campIdKey.getBytes(), ColFamily(),  col_validUser(), 1);

                TSmsAction.createOneNewRecord(smsActionBufferedMutator, seqNum, campIdKey, userGUID, userInfoRecord);

            }
        }

    }

    public static TCampInfo findById(Table campInfoTable, String campId, boolean  loadUsersBitSet) {

        TCampInfo findObj = null;
        String campKey = campId;

        Get  get = new Get( campKey.getBytes() );
        get.addFamily(ColFamily());

        if(loadUsersBitSet) {
            get.addFamily( _Buffers.ColFamily());
        }

        try {
            Result result = campInfoTable.get(get);
            if(HBaseUtil.isAnValidResult(result )) {
                findObj = new TCampInfo();

                String status = HBaseUtil.getValueAsString(result, ColFamily(), col_status());
                findObj.setStatus(status);

                String numUsers = HBaseUtil.getValueAsString(result, ColFamily(), col_numOfUsers());
                findObj.setNumOfUsers(numUsers);

                String tagJson = HBaseUtil.getValueAsString(result, ColFamily(), col_tagJson());
                findObj.setTagJson(tagJson);

                if(loadUsersBitSet) {
                    findObj.usersBitSet = result.getValue(_Buffers.ColFamily(), _Buffers.col_usersBitSet());
                }
            }

        } catch (IOException e) {
            logger.error("failed to find record ", e);
        }

        return findObj;
    }


    public static CampSmsActionStaticsImpl getCampSmsActionStatics(Table campInfoTable,  String campId) {

        CampSmsActionStaticsImpl smsActionStatics = new CampSmsActionStaticsImpl();

        if(campInfoTable != null) {

            String campKey = String.valueOf( campId );
            Get  get = new Get( campKey.getBytes() );
            get.addFamily(ColFamily());

            long totalUsers = 0;
            try {
                Result result = campInfoTable.get(get);
                if( HBaseUtil.isAnValidResult(result ) ) {
                    byte[] numOfUsersStr = result.getValue( ColFamily(), col_numOfUsers() );
                    totalUsers = Long.valueOf( new String(numOfUsersStr) );
                } else {
                    logger.warn("No such record for campId={},  getCampSmsActionStatics() will return zero counters.", campId);
                    return smsActionStatics;
                }


            } catch (IOException e) {
                logger.error("failed to get camp record ", e);
            }


            long send =         HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_smsSendCount());
            long sendFail =     HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_smsSendFailCount());
            long recv =         HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_smsRecvCount());
            long recvFail =     HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_smsRecvFailCount());
            long ack =          HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_smsAckCount());
            long validUsers =   HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_validUser());
            long inValidUsers = HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_inValidUser());
            long excludeUsers = HBaseUtil.getStatisticCounterFromHBase(campInfoTable, campId, ColFamily(), col_excludeUser());

            smsActionStatics.setTotalUserCount( totalUsers );
            smsActionStatics.setSendCount( send );
            smsActionStatics.setSendFailCount(sendFail);
            smsActionStatics.setRecvCount( recv );
            smsActionStatics.setRecvFailCount( recvFail );
            smsActionStatics.setAckCount( ack );
            smsActionStatics.setValidUserCount( validUsers );
            smsActionStatics.setInValidUserCount( inValidUsers );
            smsActionStatics.setExcludeUsers( excludeUsers );

        }

        return smsActionStatics;
    }


    public static Table createTableIfMissing(Connection connection)  throws IOException {

        List<byte[]>  allColumnFamilies = new ArrayList<>();
        allColumnFamilies.add( ColFamily() );
        allColumnFamilies.add( _Users.ColFamily() );
        allColumnFamilies.add( _ErrorUsers.ColFamily() );
        allColumnFamilies.add( _NoSuchUsers.ColFamily() );
        allColumnFamilies.add( _SentUsers.ColFamily() );
        allColumnFamilies.add( _ExcludeUsers.ColFamily() );
        allColumnFamilies.add( _Buffers.ColFamily() );

        return HBaseUtil.createOrUpgradeTable(connection, tableName(), allColumnFamilies);
    }


    public String getTagJson() {
        return tagJson;
    }

    public void setTagJson(String tagJson) {
        this.tagJson = tagJson;
    }

    public String getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(String numOfUsers) {
        this.numOfUsers = numOfUsers;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getUsersBitSet() {
        return usersBitSet;
    }

    public void setUsersBitSet(byte[] usersBitSet) {
        this.usersBitSet = usersBitSet;
    }
}
