package com.seabox.tagsys.usertags.entity;

//import com.seabox.tagsys.usertags.action.sms.TemplatePara;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * @author Changhua, Wu
 *         Created on: 2/17/16,10:52 PM
 */
public class CampInfo implements Serializable{


    private static final Logger logger = LoggerFactory.getLogger(CampInfo.class);

    private String camp_id;
    private String camp_nm;
    private Date start_dt;
    private Date end_dt;
    private long indv_num;
    private String camp_status_cd;
    private String templt_id;
    private String camp_chnl_cd;
    private String camp_inds_cd;

    private List<String>    templateParameters;
//    private Map<Integer, Set<TemplatePara>> valuesToWrap = new HashMap<>();
    private List<String> fixedTemplateCacheWithoutParameters;

    public String getCamp_id() {
        return camp_id;
    }

    public void setCamp_id(String camp_id) {
        this.camp_id = camp_id;
    }

    public String getCamp_nm() {
        return camp_nm;
    }

    public void setCamp_nm(String camp_nm) {
        this.camp_nm = camp_nm;
    }

    public Date getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(Date start_dt) {
        this.start_dt = start_dt;
    }

    public Date getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(Date end_dt) {
        this.end_dt = end_dt;
    }

    public long getIndv_num() {
        return indv_num;
    }

    public void setIndv_num(long indv_num) {
        this.indv_num = indv_num;
    }

    public String getCamp_status_cd() {
        return camp_status_cd;
    }

    public void setCamp_status_cd(String camp_status_cd) {
        this.camp_status_cd = camp_status_cd;
    }

    public String getTemplt_id() {
        return templt_id;
    }

    public void setTemplt_id(String templt_id) {
        this.templt_id = templt_id;
    }

    public String getCamp_chnl_cd() {
        return camp_chnl_cd;
    }

    public void setCamp_chnl_cd(String camp_chnl_cd) {
        this.camp_chnl_cd = camp_chnl_cd;
    }

    public String getCamp_inds_cd() {
        return camp_inds_cd;
    }

    public void setCamp_inds_cd(String camp_inds_cd) {
        this.camp_inds_cd = camp_inds_cd;
    }

    public List<String> getTemplateParameters() {
        return templateParameters;
    }

    public void setTemplateParameters(List<String> templateParameters) {
        this.templateParameters = templateParameters;
        preCheckCampTemplateParas();
    }

//    public Map<Integer, Set<TemplatePara>> getValuesToWrap() {
    public Map<Integer, Set<Object>> getValuesToWrap() {
//        return valuesToWrap;
        return null;
    }


    public void    preCheckCampTemplateParas() {

        for(int i=0; i < templateParameters.size(); ++i) {
            String paraValue = templateParameters.get(i);
//            for( TemplatePara templatePara : TemplatePara.values()) {
//
//                if(paraValue.contains( templatePara.getTemplateChar() )) {
//                    Set<TemplatePara>  templateParaSet =  valuesToWrap.get(i);
//                    if(templateParaSet == null) {
//                        templateParaSet = new HashSet<>();
//                        valuesToWrap.put(i, templateParaSet);
//                    }
//                    templateParaSet.add( templatePara );
//                }
//
//            }
        }
    }



    public List<String>  buildRealSmsTemplateParaString (Map<String, String>  realValuesMap) {

//        if(valuesToWrap.isEmpty() && fixedTemplateCacheWithoutParameters != null) {
//             return fixedTemplateCacheWithoutParameters;
//        }

        List<String> result = new ArrayList<>();
//        for(int i=0; i < templateParameters.size(); ++i) {
//
//            String paraValue = templateParameters.get(i);
//            if(valuesToWrap.containsKey( i )) {
//                Set<TemplatePara> templateParaSet = valuesToWrap.get(i);
//                for(TemplatePara templatePara: templateParaSet) {
//                    String newValue = realValuesMap.get( templatePara.getTemplateChar() );
//                    if(newValue == null) {
//                        logger.warn("realValue for template name {} not exists, use empty String instead!", templatePara.getTemplateChar() );
//                        newValue=" ";
//                    }
//                    String valueWrapped = templatePara.replaceValue( paraValue, newValue);
//                    result.add(valueWrapped);
//                }
//
//            } else {
//                result.add(paraValue);
//            }
//
//        }
//        if(valuesToWrap.isEmpty()) {
//            fixedTemplateCacheWithoutParameters = result;
//        }

        return  result;
    }


    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(CampInfo.class.getSimpleName())
                .append(" campId:").append( camp_id)
                .append(" ,camp_status_cd:").append(camp_status_cd)
                .append(" ,start_dt:").append(start_dt)
                .append(" ,end_dt:").append(end_dt);

        return  sb.toString();
    }

}
