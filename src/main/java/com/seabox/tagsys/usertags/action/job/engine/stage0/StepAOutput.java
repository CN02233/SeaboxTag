package com.seabox.tagsys.usertags.action.job.engine.stage0;

import com.seabox.tagsys.usertags.action.job.engine.BaseCampObj;

import java.util.BitSet;

/**
 * Created by wuchh on 3/10/16.
 */
public class StepAOutput extends BaseCampObj {

    StepAInput stepAInput;

    long numsGUIDInKeyForCamp;

    byte[]    redisBitSetInBytes;

    BitSet    redisBitSetInJavaObj;


    public StepAInput getStepAInput() {
        return stepAInput;
    }

    public void setStepAInput(StepAInput stepAInput) {
        this.stepAInput = stepAInput;
    }

    public long getNumsGUIDInKeyForCamp() {
        return numsGUIDInKeyForCamp;
    }

    public void setNumsGUIDInKeyForCamp(long numsGUIDInKeyForCamp) {
        this.numsGUIDInKeyForCamp = numsGUIDInKeyForCamp;
    }


    public byte[] getRedisBitSetInBytes() {
        return redisBitSetInBytes;
    }

    public void setRedisBitSetInBytes(byte[] redisBitSetInBytes) {
        this.redisBitSetInBytes = redisBitSetInBytes;
    }


    public BitSet getRedisBitSetInJavaObj() {
        return redisBitSetInJavaObj;
    }

    public void setRedisBitSetInJavaObj(BitSet redisBitSetInJavaObj) {
        this.redisBitSetInJavaObj = redisBitSetInJavaObj;
    }
}
