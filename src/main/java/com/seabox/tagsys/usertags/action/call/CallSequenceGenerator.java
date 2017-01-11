package com.seabox.tagsys.usertags.action.call;

//import javax.validation.constraints.NotNull;

/**
 * @author Changhua, Wu
 *         Created on: 5/19/16,11:25 AM
 */
public class CallSequenceGenerator {


    public final static String PREFIX_STR_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    public final static char[] PREFIX_CHAR_ARRAY = PREFIX_STR_MAP.toCharArray();

    public static String getRowKey(long campId,  String mobileNo) {
//    public static String getRowKey(long campId, @NotNull String mobileNo) {

        StringBuffer sb = new StringBuffer();

        int mobileId = mobileNo.hashCode();
        if (mobileId < 0) {
            mobileId = 0 - mobileId;
        }


        int index = (mobileId % PREFIX_CHAR_ARRAY.length);
        char prefix = PREFIX_CHAR_ARRAY[index];

        int idxCamp = (int) ((17 + campId) % PREFIX_CHAR_ARRAY.length);
        char prefixCamp = PREFIX_CHAR_ARRAY[idxCamp];

        sb.append(prefix);

        if (!mobileNo.isEmpty()) {
            char lastNum = mobileNo.charAt(mobileNo.length() - 1);
            sb.append(lastNum);
        }

        sb.append(prefixCamp);
        sb.append("_");
        sb.append(campId);
        sb.append("_");
        sb.append(mobileNo);

        return sb.toString();
    }


//    public static String getRowKey(@NotNull String campId, @NotNull String mobileNo) {
    public static String getRowKey( String campId,  String mobileNo) {
        return getRowKey(Long.valueOf(campId), mobileNo);
    }


}
