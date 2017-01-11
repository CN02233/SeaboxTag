package com.seabox.tagsys.usertags.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author Changhua, Wu
 *         Created on: 1/27/16,5:47 PM
 */

public class UserTagUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserTagUtils.class);


    public final static String TAG_PREFIX = "t:";

    public final static String REDIS_KEY_INDEX_TO_USER_GUID_MAP = "n-2-guid";
    public final static String REDIS_KEY_USER_GUID_TO_INDEX_MAP = "guid-2-n";

    public final static String  GLOBAL_EXCLUDE_USERS_KEY = "g:excludeUsers";

    public final static int    DEFAULT_TEMP_CALC_REDIS_KEY_EXPIRE_SECONDS = 180;

    public static String getCacheKeyByTagId(int tagId) {
        StringBuffer bf = new StringBuffer(TAG_PREFIX);
        bf.append(tagId);
        return bf.toString();
    }




    public static String getCampBitSetRedisKey(String campId) {
        return "camp_bits:" + campId;
    }



    private final static Character[] SHORT_ENCODING_CHARS = new Character[]{
            ////  total  64  chars .......
            'a','b','c','d','e','f','g',
            'h','i','j','k','l','m','n',
            'o','p','q','r','s','t',
            'u','v','w','x','y','z', // 26
            '0','1','2','3','4','5','6','7','8','9', //10
            'A','B','C','D','E','F','G',
            'H','I','J','K','L','M','N',
            'O','P','Q','R','S','T',
            'U','V','W','X','Y','Z',  // 26
            '-','_' //2
    };


    private final static int chars_mapping[] = new int[ 256 ];

    static {
        for(int j=0; j< chars_mapping.length; ++j) {
            chars_mapping [j] = -1; // initialize
        }

        for(int i=0; i<SHORT_ENCODING_CHARS.length; ++i) {
            char  valChar = SHORT_ENCODING_CHARS[i];
            chars_mapping[ valChar ] = i;
        }

    }


    public static String encodeTwoDigitsToShortUrl(long digit1, long digit2)
    {
        StringBuffer sbDigit1 = new StringBuffer();
        StringBuffer sbDigit2 = new StringBuffer();

        getShortEncodeStr(sbDigit1, digit1);

        getShortEncodeStr(sbDigit2, digit2);

        StringBuffer result = new StringBuffer();

        result.append( sbDigit1 );
        result.append(":");
        result.append( sbDigit2 );

        return result.toString();

    }


    public static String encodeOneDigitToShortUrl(long digit1)
    {
        StringBuffer sbDigit1 = new StringBuffer();

        getShortEncodeStr(sbDigit1, digit1);

        return sbDigit1.toString();

    }



    public static long decodeOneDigitFromShortUrl(String digitStr)
    {
        long val =0;
        for(int i=0; i< digitStr.length(); ++i) {
            char c = digitStr.charAt( i );

            int  index = chars_mapping[c];
            if(index==-1) {
                logger.error("wrong digitStr: {}", digitStr );
                return -1;
            } else {
                val <<= 6;
                val += index;
            }

        }

        return val;

    }



    private static void getShortEncodeStr (StringBuffer sb, long  val) {

        long sub = val / 64;
        long mod = val % 64;

        if(val<= 64) {
            sb.append( SHORT_ENCODING_CHARS[ (int)mod ] );
        } else {
            getShortEncodeStr(sb, sub);
            sb.append( SHORT_ENCODING_CHARS[ (int)mod ] );
        }

    }




    public static <T> T   jsonToObject(String json, Class<T> classType) throws Exception {
        return getSerializeTool().jsonToObject(json, classType);
    }


    public static String objectToJson(Object object) {
        return getSerializeTool().objectToJson(object);
    }




    private static SerializeTool getSerializeTool() {
        return JacksonTool.instance; //GsonTool.instance;
    }


    private interface SerializeTool {
        String objectToJson(Object object);
        <T> T   jsonToObject(String json, Class<T> classType) throws Exception;
    }


    //########## Gson  implement
    private static class GsonTool implements SerializeTool{

        private static final Gson  gson = new GsonBuilder().setPrettyPrinting().create();

        public static final GsonTool instance = new GsonTool();

        @Override
        public String objectToJson(Object object){
            return gson.toJson( object );
        }

        @Override
        public <T> T   jsonToObject(String json, Class<T> classType) throws Exception {
            T  obj = null;
            try {
                obj = gson.fromJson(json, classType);
            } catch (Throwable e) {
                logger.error("Error on DeSerialize json to Object, json= {},  detail exception:", json, e );
                throw e;
            }
            return obj;
        }
    }


    //########## Jackson  implement
    private static class JacksonTool implements SerializeTool{

        public static final JacksonTool instance = new JacksonTool();

        @Override
        public String objectToJson(Object object){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            String jsonValue = null;
            try {

                jsonValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString( object );

            } catch (JsonProcessingException e) {
                logger.error("Error on Serialize Object to Json, object= {}, detail exception:", object, e );
            }
            return jsonValue;
        }

        @Override
        public <T> T   jsonToObject(String json, Class<T> classType) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            T  obj = null;
            try {
                obj = objectMapper.readValue(json, classType);
            } catch (IOException e) {
                logger.error("Error on DeSerialize json to Object, json= {},  detail exception:", json, e );
                throw e;
            }
            return obj;
        }
    }




}
