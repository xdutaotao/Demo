package com.yankon.smart.model;

/**
 * Created by Evan on 15/3/10.
 */
public class TransData {
    public byte[] data;
    public long time;
    public int IP;
    public int retryTimes;
    public int type;// 处理的是什么类型
    public int trans_no;
    public String key;//key = mac
}
