package com.seafire.cworker.Widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.seafire.cworker.Activity.CollectActivity;
import com.seafire.cworker.Utils.Utils;

/**
 * Description:
 */

public class CustomTextWatcher implements TextWatcher {
    private EditText editText;
    private ImageView drawable;
    private boolean isNumPoint;

    public CustomTextWatcher(EditText et, ImageView d, boolean b) {
        this.editText = et;
        this.drawable = d;
        this.isNumPoint = b;
    }

    public CustomTextWatcher(EditText et, boolean b) {
        this.editText = et;
        this.isNumPoint = b;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){
        //不能有两个小数点
        String string = s.toString();
        int len = string.length();

        if (Utils.stringsub(string, ".") == 2){
            editText.setText(string.subSequence(0, len-1));
            return;
        }

        if (editText.getText().length() > 0) {
            int length = editText.getText().length();
            String temp = editText.getText().toString();
            if (length == 2 && TextUtils.equals("0", temp.substring(0, 1))){
                if (!TextUtils.equals(temp.substring(1, 2), ".")){
                    editText.setText("0");
                    return;
                }
            }
            //editText.setCompoundDrawablePadding(20);
            //editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            if(drawable != null) {
                //drawable.setVisibility(View.VISIBLE);
                drawable.setOnClickListener(v -> {
                    editText.setText("");
                    //drawable.setVisibility(View.GONE);
                });
            }
        } else {
            //editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            drawable.setVisibility(View.GONE);
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
                if (!TextUtils.isEmpty(s)){
                    InputFilter[] filters = {new InputFilter.LengthFilter(6)};
                    editText.setFilters(filters);
                }

            }
        }
        editText.setSelection(s.length());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
