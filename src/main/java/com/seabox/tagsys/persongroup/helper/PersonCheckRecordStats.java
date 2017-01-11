package com.seabox.tagsys.persongroup.helper;

public enum PersonCheckRecordStats {
        READY(0),//准备
        WORK(1),//进行中
        DONE(2);//完成

        private int val ;
        PersonCheckRecordStats(int val){
            this.val = val;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }