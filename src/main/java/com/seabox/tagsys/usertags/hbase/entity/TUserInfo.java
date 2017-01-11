package com.seabox.tagsys.usertags.hbase.entity;

import com.seabox.tagsys.usertags.hbase.HBaseUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Changhua, Wu
 *         Created on: 2/16/16,2:52 PM
 */
public final class TUserInfo {


    private static final Logger logger = LoggerFactory.getLogger(TUserInfo.class);

    private String userName;
    private String mobileNo;
    private String springPoint;

    //add by SongChaoqun start:
    private String hasMarried;
    private String userAge;
    private String userSex;
    private String userGuid;
    //add by SongChaoqun end




    public final static TableName  tableName() {
        return TableName.valueOf("h62_indv_info");
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }


    public final static class _CustInfo {

        public final static byte[] ColFamily() {
            return "cust_info".getBytes();
        }

        public final static byte[] col_name() { return  "indv_nm".getBytes(); }
        public final static byte[] col_mobile() { return  "mov_tel".getBytes(); }
        public final static byte[] col_married() { return  "is_married".getBytes(); }
        public final static byte[] col_age() { return  "user_age".getBytes(); }
        public final static byte[] col_sex() { return  "user_sex".getBytes(); }
        public final static byte[] col_u_nm() { return  "user_nm".getBytes(); }


    }


    public final static class _Spring {

        public final static byte[]  ColFamily() {
            return "spring_ind".getBytes();
        }

        public final static byte[]  col_point() {  return "point".getBytes(); }
    }


    public static Table createTableIfMissing(Connection connection) throws IOException {

        List<byte[]> allColumnFamilies = new ArrayList<>();
        allColumnFamilies.add( _CustInfo.ColFamily() );
        allColumnFamilies.add( _Spring.ColFamily() );

        return HBaseUtil.createOrUpgradeTable(connection, tableName(), allColumnFamilies);

    }

    public static TUserInfo buildFromResult( Result result ) {
        TUserInfo findObj = null;
        if( HBaseUtil.isAnValidResult( result )) {
            findObj = new TUserInfo();

            String mobile = HBaseUtil.getValueAsString(result, _CustInfo.ColFamily(), _CustInfo.col_mobile());
            findObj.setMobileNo( mobile );

            String name = HBaseUtil.getValueAsString(result, _CustInfo.ColFamily(), _CustInfo.col_name());
            if(name==null||"".equals(name))
                name = HBaseUtil.getValueAsString(result, _CustInfo.ColFamily(), _CustInfo.col_u_nm());
            findObj.setUserName(name);

            String hasMarried = HBaseUtil.getValueAsString(result, _CustInfo.ColFamily(), _CustInfo.col_married());
            findObj.setHasMarried(hasMarried);

            String userAge = HBaseUtil.getValueAsString(result, _CustInfo.ColFamily(), _CustInfo.col_age());
            findObj.setUserAge(userAge);

            String userSex = HBaseUtil.getValueAsString(result, _CustInfo.ColFamily(), _CustInfo.col_sex());
            findObj.setUserSex(userSex);

            String point = HBaseUtil.getValueAsString(result, _Spring.ColFamily(), _Spring.col_point());
            findObj.setSpringPoint(point);
        }
        return  findObj;
    }


    public static TUserInfo findById(Table userInfoTable, String userGUID) {

        Get  get = new Get( userGUID.getBytes() );
        get.addFamily( _CustInfo.ColFamily() );
        get.addFamily( _Spring.ColFamily() );

        try {
            Result result = userInfoTable.get(get);

            TUserInfo findObj = buildFromResult( result );

            return findObj;

        } catch (IOException e) {
            logger.error("failed to find record ", e);
        }

        return null;

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getHasMarried() {
        return hasMarried;
    }

    public void setHasMarried(String hasMarried) {
        this.hasMarried = hasMarried;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }


    public String getSpringPoint() {
        return springPoint;
    }

    public void setSpringPoint(String springPoint) {
        this.springPoint = springPoint;
    }



    //////////////////////// Fake Data for DEv Test only,  just change "enableFakeData"  to enable/disable this Mock data.
    public static class DataForDevTest {

        public static boolean enableFakeData = false;

        public static int test_index =10;

        public static TUserInfo findById_createIfMissing(Table userInfoTable, String userGUID) {

            TUserInfo userInfoRecord = TUserInfo.findById(userInfoTable, userGUID);

            if(userInfoRecord == null || userInfoRecord.getMobileNo() == null) {
                userInfoRecord = new TUserInfo();
                userInfoRecord.setMobileNo("01234567891" + test_index++);
                if(test_index>=99) {
                    test_index = 10;
                }
                userInfoRecord.setSpringPoint("12");
                userInfoRecord.setUserName("xxx");
            }

            return userInfoRecord;
        }

    }
    ////////////////////////


    public final static TUserInfo wrapperFindById(Table userInfoTable, String userGUID) {
        TUserInfo userInfoRecord;
        if(DataForDevTest.enableFakeData ) {
            userInfoRecord = DataForDevTest.findById_createIfMissing(userInfoTable, userGUID);
        } else {
            userInfoRecord = TUserInfo.findById(userInfoTable, userGUID);
        }

        return userInfoRecord;
    }




    public final static TUserInfo createOneNewRecord(BufferedMutator userInfoBufferedMutator, String rowKey, String userName, String mobileNo, String springPoint ) throws IOException{

        TUserInfo userInfo = new TUserInfo();
        userInfo.setUserName( userName );
        userInfo.setMobileNo( mobileNo );
        userInfo.setSpringPoint( springPoint );

        Put put = new Put( rowKey.getBytes() );

        put.addColumn(_CustInfo.ColFamily(), _CustInfo.col_name(),       (null==userName) ? null : userName.getBytes());
        put.addColumn(_CustInfo.ColFamily(),  _CustInfo.col_mobile(),    (null==mobileNo) ? null :  mobileNo.getBytes());
        put.addColumn(_Spring.ColFamily(),   _Spring.col_point(),        (null==springPoint) ? null : springPoint.getBytes());

        userInfoBufferedMutator.mutate( put );

        logger.debug("createOneNewRecord() start  for rowKey: {}, userName:{}, mobileNo:{}, springPoint:{}", rowKey, userName, mobileNo, springPoint);

        return userInfo;
    }



    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TUserInfo mobile:").append(mobileNo)
                .append(", springPoint:").append(springPoint);
        return  sb.toString();
    }

}
