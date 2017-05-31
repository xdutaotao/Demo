package com.yankon.smart.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Evan on 14/12/19.
 */
public class CommandBuilder {

    class Attr {
        byte dev_id;
        byte attr_id;
        byte cmd;
        int data_len = 0;
        byte[] data;
    }

    ArrayList<Attr> mAttrList = new ArrayList<>();

    public void append(byte dev_id, byte attr_id, byte cmd, byte[] data) {
        Attr attr = new Attr();
        attr.dev_id = dev_id;
        attr.attr_id = attr_id;
        attr.cmd = cmd;
        if (data != null) {
            attr.data_len = data.length;
        }
        attr.data = data;
        mAttrList.add(attr);
    }

    public byte[] build(int trans_no, int subIP) {
        int len = 0;
        for (Attr attr : mAttrList) {
            len += 5 + attr.data_len;
        }
        byte[] result = new byte[14 + len];
        Arrays.fill(result, (byte) 0);
        result[0] = 0;
        result[1] = (byte) trans_no;
        result[2] = 0;
        result[3] = 2;
        if (subIP != 0) {
            result[8] = (byte) ((subIP >> 24) & 0xFF);
            result[9] = (byte) ((subIP >> 16) & 0xFF);
            result[10] = (byte) ((subIP >> 8) & 0xFF);
            result[11] = (byte) (subIP & 0xFF);
        }
        Utils.Int16ToByte(len, result, 12);                         /* 拼接PDU_len*/
        int pos = 14;
        for (Attr attr : mAttrList) {
            result[pos] = attr.dev_id;
            result[pos + 1] = attr.attr_id;
            result[pos + 2] = attr.cmd;
            Utils.Int16ToByte(attr.data_len, result, pos + 3);      /* 拼接Data_len */
            for (int i = 0; i < attr.data_len; i++) {
                result[pos + 5 + i] = attr.data[i];                 /* 拼接Data 字节copy*/
            }
            pos += 5 + attr.data_len;
        }
        return result;
    }

}
