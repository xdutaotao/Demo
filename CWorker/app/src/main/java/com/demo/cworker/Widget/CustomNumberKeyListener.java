package com.demo.cworker.Widget;

import android.text.method.NumberKeyListener;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/25 17:06.
 */

public class CustomNumberKeyListener extends NumberKeyListener {
    @Override
    protected char[] getAcceptedChars() {
        char[] num = {'1','2','3','4','5','6','7','8','9','0','.'};
        return num;
    }

    @Override
    public int getInputType() {
        return 0;
    }
}
