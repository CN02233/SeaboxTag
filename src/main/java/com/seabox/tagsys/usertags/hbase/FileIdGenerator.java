package com.seabox.tagsys.usertags.hbase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This generator will compatible if we want to migrate Import-File to hBase
 *
 * @author Changhua, Wu
 *         Created on: 5/3/16,10:41 AM
 */
public class FileIdGenerator {

    public long id = -1;


    public static final String DATA="abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";


    public static Random random = new Random();


    public static String generateUId() {
        return generateUIdForHBase();
    }

    public static String generateUIdForHBase() {

        StringBuffer sb = new StringBuffer();
        for(int i=0; i<6; ++i) {
            int index = random.nextInt( DATA.length() );
            sb.append( DATA.charAt(index) );
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String timePostfix = dateFormat.format(new Date());

        sb.append( timePostfix );

        return sb.toString();
    }


}
