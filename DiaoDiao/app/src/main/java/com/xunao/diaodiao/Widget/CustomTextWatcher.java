package com.xunao.diaodiao.Widget;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Description:
 */

public class CustomTextWatcher implements TextWatcher {
    private EditText editText;
    private Drawable drawable;
    private boolean isNumPoint;

    public CustomTextWatcher(EditText et, Drawable d, boolean b) {
        this.editText = et;
        this.drawable = d;
        this.isNumPoint = b;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){
        if (editText.getText().length() > 0) {
            int length = editText.getText().length();
            String temp = editText.getText().toString();
            if (length == 2 && TextUtils.equals("0", temp.substring(0, 1))){
                if (!TextUtils.equals(temp.substring(1, 2), ".")){
                    editText.setText("0");
                    return;
                }
            }
            editText.setCompoundDrawablePadding(20);
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if (isNumPoint){
            if (s.toString().length() == 6) {
                //如果第六个不为小数点, 并且前面没有小数点
                if (!TextUtils.equals(s.toString().substring(5, 6), ".") && !s.toString().substring(0,5).contains(".")) {
                    editText.setText(s.toString().substring(0, 5));
                    editText.setSelection(5);
                    return;
                }
            }
            //第一个不能是小数点
            if (TextUtils.equals(s.toString(), ".")){
                editText.setText(s.toString().substring(0, 0));
                return;
            }

            if (s.toString().contains(".")){
                int index = s.toString().indexOf(".");
                InputFilter[] filters = {new InputFilter.LengthFilter(index+4)};
                editText.setFilters(filters);
            }else{
                InputFilter[] filters = {new InputFilter.LengthFilter(6)};
                editText.setFilters(filters);
            }
        }
        editText.setSelection(s.length());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
