package com.demo.cworker.Widget;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;

import com.demo.cworker.Utils.LogUtils;
import com.demo.cworker.Utils.ToastUtil;

/**
 * Description:
 */

public class CustomTextWatcher implements TextWatcher {
    private EditText editText;
    private Drawable drawable;
    private boolean isNumPoint;
    private boolean isPoint;

    public CustomTextWatcher(EditText et, Drawable d, boolean b) {
        this.editText = et;
        this.drawable = d;
        this.isNumPoint = b;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        LogUtils.d("beforeTextChanged: "+ s.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){
        if (editText.getText().length() > 0) {
            editText.setCompoundDrawablePadding(20);
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if (isNumPoint && isPoint){
            if (s.toString().contains(".")){
                int index = s.toString().indexOf(".");
                InputFilter[] filters = {new InputFilter.LengthFilter(index+4)};
                editText.setFilters(filters);
            }else{
                InputFilter[] filters = {new InputFilter.LengthFilter(6)};
                editText.setFilters(filters);
            }
        }

        LogUtils.d("onTextChanged: "+s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        LogUtils.d("afterTextChanged: "+s.toString());
        if (s.toString().length() == 6 && !TextUtils.equals(s.toString().substring(5,5),".") ){
            editText.setText(s.toString().substring(0, 5));
            editText.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            isPoint = true;
        }else{
            isPoint = false;
        }
    }
}
